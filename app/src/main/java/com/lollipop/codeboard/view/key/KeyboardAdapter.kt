package com.lollipop.codeboard.view.key

import android.content.Context
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import com.lollipop.codeboard.view.KeyboardView

open class KeyboardAdapter(
    protected val context: Context,
    protected val callback: Callback
) : KeyboardView.KeyViewAdapter, BasicKeyViewHolder.OnKeyClickListener,
    DecorationKeyViewHolder.OnDecorationTouchListener,
    BasicKeyViewHolder.OnKeyTouchListener {

    override fun createHodler(info: KeyInfo): KeyboardView.KeyHolder {
        val decoration = Keys.findDecoration(info.key)
        return when {
            info.key.isEmpty() -> {
                EmptyKeyViewHolder(context)
            }

            decoration != null -> {
                DecorationKeyViewHolder(
                    context,
                    info
                ).also {
                    it.setOnKeyClickListener(this)
                    it.setOnDecorationTouchListener(this)
                }
            }

            else -> {
                SingleKeyViewHolder(
                    context,
                    info
                ).also {
                    it.setOnKeyClickListener(this)
                    it.setOnKeyTouchListener(this)
                }
            }
        }
    }

    override fun onKeyClick(key: Keys.Key?, info: KeyInfo) {
        callback.onKeyClick(key, info)
    }

    override fun onKeyTouch(
        key: Keys.Key?,
        info: KeyInfo,
        isPressed: Boolean
    ) {
        callback.onKeyTouch(key, info, isPressed)
    }

    override fun onDecorationTouch(
        key: DecorationKey,
        isPressed: Boolean
    ) {
        callback.onDecorationTouch(key, isPressed)
    }

    class Callback(
        val onKeyClick: (key: Keys.Key?, info: KeyInfo) -> Unit = { _, _ -> },
        val onDecorationTouch: (DecorationKey, isPressed: Boolean) -> Unit = { _, _ -> },
        val onKeyTouch: (key: Keys.Key?, info: KeyInfo, isPressed: Boolean) -> Unit = { _, _, _ -> },
    )

}