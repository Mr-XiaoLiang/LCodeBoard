package com.lollipop.codeboard.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.lollipop.codeboard.tools.registerLog

class LayerManager(
    private val imService: ImService,
    private val layerProvider: LayerProvider,
    private val viewGroup: ViewGroup
) : LayerOwner {

    private val layers = mutableMapOf<String, LayerHolder>()
    private var currentLayer: LayerHolder? = null

    private val insets = Rect()

    private val log = registerLog()

    fun onInsetsChanged(left: Int, top: Int, right: Int, bottom: Int) {
        insets.set(left, top, right, bottom)
        currentLayer?.onInsetsChange(left, top, right, bottom)
    }

    override fun nextLayer(tag: String) {
        val holder = getOrCreateLayer(tag) ?: return
        if (currentLayer == holder) {
            holder.onInsetsChange(insets.left, insets.top, insets.right, insets.bottom)
            holder.onShow()
            return
        }
        currentLayer?.onHide()
        currentLayer = holder
        holder.onShow()
        holder.onInsetsChange(insets.left, insets.top, insets.right, insets.bottom)
    }

    override fun getProvider(): ConnectionProvider? {
        return imService
    }

    private fun cacheLayer(tag: String, holder: LayerHolder) {
        layers[tag] = holder
    }

    private fun getOrCreateLayer(tag: String): LayerHolder? {
        return log.tryDo("getOrCreateLayer") {

            val holder = layers[tag]
            if (holder != null) {
                return holder
            }
            val layerClass = layerProvider.getLayerClass(tag)
            if (layerClass == null) {
                return@tryDo null
            }
            val instance = layerClass.getDeclaredConstructor().newInstance()
            val context = imService.context()
            val layerView = instance.create(context, this)
            val newHolder = LayerHolder(layerView, instance)
            cacheLayer(tag, newHolder)

            viewGroup.addView(layerView)

            newHolder
        }
    }

    class LayerHolder(
        val view: View,
        val layer: Layer
    ) {

        fun onInsetsChange(left: Int, top: Int, right: Int, bottom: Int) {
            layer.onInsetsChange(left, top, right, bottom)
        }

        fun onShow() {
            layer.onShow()
        }

        fun onHide() {
            layer.onHide()
        }

    }

    interface LayerProvider {

        fun getLayerClass(tag: String): Class<Layer>?

    }

    interface ImService : ConnectionProvider {
        fun context(): Context

    }

}