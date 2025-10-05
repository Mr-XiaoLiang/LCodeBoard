package com.lollipop.codeboard.view.key

import android.content.Context
import android.view.View
import android.widget.Space
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.ui.KeyboardTheme
import com.lollipop.codeboard.view.KeyboardView

class EmptyKeyViewHolder(
    private val context: Context,
) : KeyboardView.KeyHolder {

    companion object {
        private fun getContent(context: Context): View {
            return Space(context)
        }
    }

    private val contentView by lazy {
        getContent(context)
    }

    override val view: View
        get() {
            return contentView
        }

    override fun onSizeChanged(width: Int, height: Int) {
    }

    override fun updateTheme(theme: KeyboardTheme) {
    }

    override fun onDecorationKeyChanged(
        key: DecorationKey,
        isSticky: Boolean
    ) {
    }

}