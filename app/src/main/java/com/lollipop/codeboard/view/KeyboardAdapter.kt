package com.lollipop.codeboard.view

import android.content.Context
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys

class KeyboardAdapter(
    private val context: Context,
    private val onKeyClickCallback: (key: Keys.Key?, info: KeyInfo) -> Unit,
    private val onDecorationTouchCallback: (DecorationKey, isPressed: Boolean) -> Unit,
    private val onKeyTouchCallback: (key: Keys.Key?, info: KeyInfo, isPressed: Boolean) -> Unit
) : KeyboardView.KeyViewAdapter, BasicKeyViewHolder.OnKeyClickListener,
    DecorationKeyViewHolder.OnDecorationTouchListener,
    BasicKeyViewHolder.OnKeyTouchListener {

    override fun createHodler(info: KeyInfo): KeyboardView.KeyHolder {
        val decoration = Keys.findDecoration(info.key)
        return if (decoration != null) {
            DecorationKeyViewHolder(
                context,
                info
            ).also {
                it.setOnKeyClickListener(this)
                it.setOnDecorationTouchListener(this)
            }
        } else {
            SingleKeyViewHolder(
                context,
                info
            ).also {
                it.setOnKeyClickListener(this)
                it.setOnKeyTouchListener(this)
            }
        }
    }

    override fun onKeyClick(key: Keys.Key?, info: KeyInfo) {
        onKeyClickCallback(key, info)
    }

    override fun onKeyTouch(
        key: Keys.Key?,
        info: KeyInfo,
        isPressed: Boolean
    ) {
        onKeyTouchCallback(key, info, isPressed)
    }

    override fun onDecorationTouch(
        key: DecorationKey,
        isPressed: Boolean
    ) {
        onDecorationTouchCallback(key, isPressed)
    }

}