package com.lollipop.codeboard.view

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import com.lollipop.codeboard.keyboard.DecorationKey

abstract class BasicKeyViewHolder : KeyboardView.KeyHolder {

    protected var decorationKey: DecorationKey = DecorationKey.Empty

    override fun onDecorationKeyChanged(key: DecorationKey) {
        this.decorationKey = key
        onDecorationChanged(key)
    }

    protected abstract fun onDecorationChanged(key: DecorationKey)

    protected abstract class KeyBackground : StateListDrawable() {

        fun addPressedState(drawable: Drawable) {
            addState(intArrayOf(android.R.attr.state_pressed), drawable)
        }

        fun addNormalState(drawable: Drawable) {
            addState(intArrayOf(), drawable)
        }

    }

}