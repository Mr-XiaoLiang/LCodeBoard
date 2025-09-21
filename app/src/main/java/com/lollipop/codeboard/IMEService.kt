package com.lollipop.codeboard

import android.content.Context
import android.graphics.Rect
import android.inputmethodservice.InputMethodService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewManager
import android.view.inputmethod.InputConnection
import androidx.core.view.isVisible
import com.lollipop.codeboard.databinding.ViewImFrameBinding
import com.lollipop.codeboard.insets.SizeCallback
import com.lollipop.codeboard.protocol.ConnectionProvider
import com.lollipop.codeboard.protocol.InputLayerManager
import com.lollipop.codeboard.tools.registerLog

class IMEService : InputMethodService(), ConnectionProvider {

    private val handlerManager = IMEHandler()

    private val keyboardViewDelegate = KeyboardViewDelegate(this, this)

    override fun onCreate() {
        super.onCreate()
        keyboardViewDelegate.defaultLayer(IMLayerStore.LAYER_QWERTY_EN)
    }

    override fun onCreateInputView(): View? {
        val rootView = keyboardViewDelegate.rootView
        rootView.parent?.let { parent ->
            if (parent is ViewManager) {
                parent.removeView(rootView)
            }
        }
        return rootView
    }

    override fun getConnection(): InputConnection? {
        return currentInputConnection
    }

    private class KeyboardViewDelegate(
        val context: Context,
        private val connectionProvider: ConnectionProvider
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
            return connectionProvider.getConnection()
        }

    }

}