package com.lollipop.codeboard

import android.content.Context
import android.graphics.Rect
import android.inputmethodservice.InputMethodService
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import androidx.core.view.isVisible
import com.lollipop.codeboard.databinding.ViewImFrameBinding
import com.lollipop.codeboard.insets.SizeCallback
import com.lollipop.codeboard.protocol.ConnectionProvider
import com.lollipop.codeboard.protocol.InputLayerManager

class IMEService : InputMethodService(), ConnectionProvider {

    private val handlerManager = IMEHandler()
    private val delegate = IMEDelegate(handlerManager.keyHandler)

    private val keyboardViewDelegate = KeyboardViewDelegate(this, this)

    override fun onCreate() {
        super.onCreate()
        keyboardViewDelegate.defaultLayer(IMLayerStore.LAYER_QWERTY_EN)
    }

    override fun onCreateInputView(): View? {
//        val layout = ViewImQwertyBinding.inflate(layoutInflater)
//        layout.keyboardView.setKeyViewAdapter(
//            KeyboardAdapter(
//                context = this,
//                onKeyClickCallback = delegate::onKeyClick,
//                onDecorationTouchCallback = delegate::onDecorationTouch,
//                onKeyTouchCallback = delegate::onKeyTouch
//            )
//        )
//        delegate.onViewCreated(layout.keyboardView)
//        return layout.root
        return keyboardViewDelegate.rootView
    }

    override fun getConnection(): InputConnection? {
        return currentInputConnection
    }

    private class KeyboardViewDelegate(
        val context: Context,
        private val connectionProvider: ConnectionProvider
    ) : InputLayerManager.ImService {

        val binding = ViewImFrameBinding.inflate(LayoutInflater.from(context))

        val layerManager = InputLayerManager(
            imService = this,
            layerProvider = IMLayerStore,
            layerGroup = binding.layerContainer,
            alternativeGroup = binding.alternativeList
        )

        val rootView: View
            get() {
                return binding.root
            }

        private val insetsBounds = Rect()

        private val topBarSizeCallback = object : SizeCallback {
            override fun onSizeChanged(width: Int, height: Int) {
                updateTopInsets(height)
            }
        }

        private val bottomBarSizeCallback = object : SizeCallback {
            override fun onSizeChanged(width: Int, height: Int) {
                updateBottomInsets(height)
            }
        }

        init {
            binding.topBar.register(topBarSizeCallback)
            binding.bottomBar.register(bottomBarSizeCallback)
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