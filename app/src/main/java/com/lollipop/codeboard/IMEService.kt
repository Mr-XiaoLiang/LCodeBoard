package com.lollipop.codeboard

import android.inputmethodservice.InputMethodService
import android.view.View
import com.lollipop.codeboard.databinding.ViewImQwertyBinding
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import com.lollipop.codeboard.view.KeyboardAdapter
import com.lollipop.codeboard.view.KeyboardView

class IMEService : InputMethodService() {

    private var keyboardView: KeyboardView? = null

    override fun onCreateInputView(): View? {
        val layout = ViewImQwertyBinding.inflate(layoutInflater)
        layout.keyboardView.setKeyViewAdapter(
            KeyboardAdapter(this, ::onKeyClick, ::onDecorationTouch)
        )
        keyboardView = layout.keyboardView
//        layout.keyboardView.setKeyViewAdapter(EditModeAdapter(this))
        return layout.root
    }

    private fun onKeyClick(key: Keys.Key?, info: KeyInfo) {
        val k = key ?: return
        if (k is Keys.Decoration) {
            return
        }
        currentInputConnection.commitText(k.keyValue, 1)
    }

    private fun onDecorationTouch(decorationKey: DecorationKey) {
        keyboardView?.setDecorationKey(decorationKey, false)
    }

    private fun toDecorationKey(key: Keys.Decoration): DecorationKey? {
        return when (key) {
            Keys.Decoration.Shift -> DecorationKey.Shift
            Keys.Decoration.Command -> DecorationKey.Command
            Keys.Decoration.Option -> DecorationKey.Option
            else -> {
                return null
            }
        }
    }

}