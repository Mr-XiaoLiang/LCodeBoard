package com.lollipop.codeboard.view

import android.content.Context
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys

class KeyboardAdapter(
    private val context: Context,
    private val onKeyClickCallback: (key: Keys.Key?, info: KeyInfo) -> Unit,
    private var onDecorationChange: (DecorationKey) -> Unit
) : KeyboardView.KeyViewAdapter, BasicKeyViewHolder.OnKeyClickListener,
    DecorationKeyViewHolder.OnDecorationTouchListener {

    private var decorationKey: DecorationKey = DecorationKey.Empty

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
            }
        }
    }

    override fun onKeyClick(key: Keys.Key?, info: KeyInfo) {
        onKeyClickCallback(key, info)
    }

    override fun onDecorationTouch(
        key: DecorationKey,
        isPressed: Boolean
    ) {
        if (decorationKey != key) {
            if (isPressed || decorationKey == DecorationKey.Empty) {
                decorationKey = key
            }
        } else {
            if (isPressed) {
                decorationKey = key
            } else {
                decorationKey = DecorationKey.Empty
            }
        }
    }

}