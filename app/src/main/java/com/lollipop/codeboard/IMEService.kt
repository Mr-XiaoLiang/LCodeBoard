package com.lollipop.codeboard

import android.inputmethodservice.InputMethodService
import android.view.View
import com.lollipop.codeboard.databinding.ViewImQwertyBinding
import com.lollipop.codeboard.view.KeyboardAdapter

class IMEService : InputMethodService() {

    private val delegate = IMEDelegate(this)

    override fun onCreateInputView(): View? {
        val layout = ViewImQwertyBinding.inflate(layoutInflater)
        layout.keyboardView.setKeyViewAdapter(
            KeyboardAdapter(
                context = this,
                onKeyClickCallback = delegate::onKeyClick,
                onDecorationTouchCallback = delegate::onDecorationTouch,
                onKeyTouchCallback = delegate::onKeyTouch
            )
        )
        delegate.onViewCreated(layout.keyboardView)
        return layout.root
    }


}