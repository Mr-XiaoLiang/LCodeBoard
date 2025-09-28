package com.lollipop.codeboard

import android.content.Context
import android.graphics.Rect
import android.inputmethodservice.InputMethodService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewManager
import android.view.inputmethod.CursorAnchorInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.core.view.isVisible
import com.lollipop.codeboard.databinding.ViewImFrameBinding
import com.lollipop.codeboard.insets.SizeCallback
import com.lollipop.codeboard.protocol.ConnectionProvider
import com.lollipop.codeboard.protocol.InputLayerManager
import com.lollipop.codeboard.tools.registerLog
import com.lollipop.codeboard.ui.FrameThemeHelper

class IMEService : InputMethodService(), ConnectionProvider {

    private var lastLayer = IMLayerStore.LAYER_QWERTY_EN

    private var keyboardViewDelegate: KeyboardViewDelegate? = null

    override fun onCreateInputView(): View? {
        // 既然让我创建，那就重新创建
        destroyDelegate()
        val rootView = optDelegate().rootView
        rootView.parent?.let { parent ->
            if (parent is ViewManager) {
                parent.removeView(rootView)
            }
        }
        return rootView
    }

    override fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(editorInfo, restarting)
        optDelegate().onStartInputView(editorInfo, restarting)
    }

    override fun onUpdateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo?) {
        super.onUpdateCursorAnchorInfo(cursorAnchorInfo)
        optDelegate().onUpdateCursorAnchorInfo(cursorAnchorInfo)
    }

    override fun getConnection(): InputConnection? {
        return currentInputConnection
    }

    private fun optDelegate(): KeyboardViewDelegate {
        val delegate = keyboardViewDelegate
        if (delegate != null) {
            return delegate
        }
        val newDelegate = KeyboardViewDelegate(this, this)
        newDelegate.defaultLayer(lastLayer)
        keyboardViewDelegate = newDelegate
        return newDelegate
    }

    private fun destroyDelegate() {
        keyboardViewDelegate?.destroy()
        keyboardViewDelegate = null
    }

    private class KeyboardViewDelegate(
        val context: Context,
        private var connectionProvider: ConnectionProvider?
    ) : InputLayerManager.ImService {

        val binding by lazy {
            ViewImFrameBinding.inflate(LayoutInflater.from(context)).apply {
                topBar.register(topBarSizeCallback)
                bottomBar.register(bottomBarSizeCallback)
            }
        }

        val layerManager by lazy {
            InputLayerManager(
                imService = this,
                layerProvider = IMLayerStore,
                layerGroup = binding.layerContainer,
                alternativeGroup = binding.alternativeList
            )
        }

        val rootView: View
            get() {
                return binding.root
            }

        private val log = registerLog()

        private val insetsBounds = Rect()

        private val topBarSizeCallback = object : SizeCallback {
            override fun onSizeChanged(width: Int, height: Int) {
                log("topBarSizeCallback.onSizeChanged: $width, $height")
                updateTopInsets(height)
            }
        }

        private val bottomBarSizeCallback = object : SizeCallback {
            override fun onSizeChanged(width: Int, height: Int) {
                log("bottomBarSizeCallback.onSizeChanged: $width, $height")
                updateBottomInsets(height)
            }
        }

        fun destroy() {
            connectionProvider = null
            rootView.let {
                val parent = it.parent
                if (parent is ViewManager) {
                    parent.removeView(it)
                }
            }
        }

        fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
            FrameThemeHelper.update(binding)
            layerManager.onStartInputView(editorInfo, restarting)
        }

        fun onUpdateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo?) {
            layerManager.onUpdateCursorAnchorInfo(cursorAnchorInfo)
        }

        fun nextLayer(tag: String) {
            layerManager.nextLayer(tag)
        }

        fun defaultLayer(tag: String) {
            layerManager.setDefaultLayer(tag)
        }

        override fun context(): Context {
            return context
        }

        private fun updateTopInsets(height: Int) {
            insetsBounds.top = height
            onInsetsChanged()
        }

        private fun updateBottomInsets(height: Int) {
            insetsBounds.bottom = height
            onInsetsChanged()
        }

        private fun onInsetsChanged() {
            layerManager.onInsetsChanged(
                insetsBounds.left,
                insetsBounds.top,
                insetsBounds.right,
                insetsBounds.bottom
            )
        }

        override fun onAlternativeChanged(hasData: Boolean) {
            binding.alternativeList.isVisible = hasData
            binding.optionBar.isVisible = !hasData
        }

        override fun getConnection(): InputConnection? {
            return connectionProvider?.getConnection()
        }

    }

}