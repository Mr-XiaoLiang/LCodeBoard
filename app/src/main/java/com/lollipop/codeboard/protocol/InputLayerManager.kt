package com.lollipop.codeboard.protocol

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.codeboard.tools.registerLog

class InputLayerManager(
    private val imService: ImService,
    private val layerProvider: LayerProvider,
    private val layerGroup: ViewGroup,
    private val alternativeGroup: RecyclerView
) : LayerOwner {

    private val layers = mutableMapOf<String, LayerHolder>()
    private var currentLayer: LayerHolder? = null

    private val insets = Rect()

    private val log = registerLog()

    private val alternativeDataObserver = AlternativeDataCallback {
        imService.onAlternativeChanged(it)
    }

    fun onInsetsChanged(left: Int, top: Int, right: Int, bottom: Int) {
//        log("onInsetsChanged: $insets, currentLayer = $currentLayer")
        insets.set(left, top, right, bottom)
        currentLayer?.onInsetsChange(left, top, right, bottom)
    }

    fun setDefaultLayer(tag: String) {
        if (currentLayer == null) {
            nextLayer(tag)
        }
    }

    private fun showLayer(holder: LayerHolder) {
//        log("showLayer: $insets")
        alternativeGroup.adapter = holder.alternativeAdapter
        holder.onInsetsChange(insets.left, insets.top, insets.right, insets.bottom)
        holder.bindDataCallback(alternativeDataObserver)
        holder.onShow()
    }

    private fun hideLayer(holder: LayerHolder) {
        alternativeGroup.adapter = null
        holder.bindDataCallback(null)
        holder.onHide()
    }

    override fun nextLayer(tag: String) {
        val holder = getOrCreateLayer(tag) ?: return
        if (currentLayer != null && currentLayer === holder) {
            showLayer(holder)
            return
        }
        currentLayer?.let {
            hideLayer(it)
        }
        currentLayer = holder
        showLayer(holder)
    }

    override fun getProvider(): ConnectionProvider? {
        return imService
    }

    private fun cacheLayer(tag: String, holder: LayerHolder) {
        layers[tag] = holder
    }

    private fun getLayer(tag: String): LayerHolder? {
        return layers[tag]
    }

    private fun getOrCreateLayer(tag: String): LayerHolder? {
        return log.tryDo("getOrCreateLayer") {

            val holder = getLayer(tag)
            if (holder != null) {
                return holder
            }
            val layerClass = layerProvider.getLayerClass(tag)
            if (layerClass == null) {
                return@tryDo null
            }
            val instance = layerClass.getDeclaredConstructor().newInstance()
            val context = imService.context()
            val layerView = instance.createView(context, this)
            val adapter = instance.getAlternativeAdapter()
            val newHolder = LayerHolder(
                view = layerView, alternativeAdapter = adapter, layer = instance
            )
            cacheLayer(tag, newHolder)

            layerGroup.addView(layerView)

            newHolder
        }
    }

    class LayerHolder(
        val view: View,
        val alternativeAdapter: RecyclerView.Adapter<*>?,
        val layer: InputLayer
    ) {

        private var alternativeDataCallback: AlternativeDataCallback? = null
        private val dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                onDataChanged(false)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                onDataChanged(false)
            }

            override fun onItemRangeChanged(
                positionStart: Int,
                itemCount: Int,
                payload: Any?
            ) {
                onDataChanged(false)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onDataChanged(false)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onDataChanged(false)
            }

            override fun onItemRangeMoved(
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                onDataChanged(false)
            }
        }

        private var dataState = false

        private val hashTag by lazy {
            System.identityHashCode(this).toString(16).uppercase()
        }

        init {
            alternativeAdapter?.registerAdapterDataObserver(dataObserver)
        }

        fun bindDataCallback(callback: AlternativeDataCallback?) {
            alternativeDataCallback = callback
        }

        private fun onDataChanged(force: Boolean) {
            val adapter = alternativeAdapter
            if (adapter == null) {
                alternativeDataCallback?.onAlternativeDataChanged(false)
                return
            }
            val oldState = dataState
            dataState = adapter.itemCount > 0
            if (oldState != dataState || force) {
                alternativeDataCallback?.onAlternativeDataChanged(dataState)
            }
        }

        fun onInsetsChange(left: Int, top: Int, right: Int, bottom: Int) {
            layer.onInsetsChange(left, top, right, bottom)
        }

        fun onShow() {
            layer.onShow()
            onDataChanged(true)
        }

        fun onHide() {
            layer.onHide()
        }

        override fun toString(): String {
            return "LayerHolder@${hashTag}{ $view, $alternativeAdapter, $layer }"
        }

    }

    interface LayerProvider {

        fun getLayerClass(tag: String): Class<out InputLayer>?

    }

    interface ImService : ConnectionProvider {
        fun context(): Context
        fun onAlternativeChanged(hasData: Boolean)
    }

    fun interface AlternativeDataCallback {
        fun onAlternativeDataChanged(hasData: Boolean)
    }

}
