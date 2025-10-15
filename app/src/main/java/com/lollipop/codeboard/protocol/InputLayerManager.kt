package com.lollipop.codeboard.protocol

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.CursorAnchorInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.codeboard.tools.registerLog
import com.lollipop.codeboard.view.AlternativeAdapterHelper

class InputLayerManager(
    private val imService: ImService,
    private val layerProvider: LayerProvider,
    private val layerGroup: ViewGroup,
) : LayerOwner {

    private val layers = mutableMapOf<String, LayerHolder>()
    private var currentLayer: LayerHolder? = null

    private val insets = Rect()

    private val log = registerLog()

    private var editorInfo: EditorInfo? = null
    private var editorRestarting: Boolean = false

    val alternativeAdapterHelper = AlternativeAdapterHelper()

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
        alternativeAdapterHelper.setAdapter(holder.alternativeAdapter)
        holder.onInsetsChange(insets.left, insets.top, insets.right, insets.bottom)
        dispatchEditorInfo()
        holder.onShow()
        imService.getConnection()?.requestCursorUpdates(InputConnection.CURSOR_UPDATE_MONITOR)
    }

    private fun hideLayer(holder: LayerHolder) {
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

    fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
        this.editorInfo = editorInfo
        this.editorRestarting = restarting
        dispatchEditorInfo()
    }

    fun onUpdateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo?) {
        currentLayer?.onUpdateCursorAnchorInfo(cursorAnchorInfo)
    }

    private fun dispatchEditorInfo() {
        editorInfo?.let {
            currentLayer?.onStartInputView(it, editorRestarting)
        }
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
            val newHolder =
                LayerHolder(view = layerView, alternativeAdapter = adapter, layer = instance)
            cacheLayer(tag, newHolder)

            layerGroup.addView(layerView)

            newHolder
        }
    }

    class LayerHolder(
        val view: View,
        val alternativeAdapter: AlternativeAdapter?,
        val layer: InputLayer
    ) {

        private val hashTag by lazy {
            System.identityHashCode(this).toString(16).uppercase()
        }

        fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
            layer.onStartInputView(editorInfo, restarting)
        }

        fun onUpdateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo?) {
            layer.onUpdateCursorAnchorInfo(cursorAnchorInfo)
        }

        fun onInsetsChange(left: Int, top: Int, right: Int, bottom: Int) {
            layer.onInsetsChange(left, top, right, bottom)
        }

        fun onShow() {
            layer.onShow()
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
    }

}
