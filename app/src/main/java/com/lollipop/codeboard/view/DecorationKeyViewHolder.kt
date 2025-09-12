package com.lollipop.codeboard.view

import android.content.Context
import android.view.View
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo

class DecorationKeyViewHolder(
    context: Context,
    private val info: KeyInfo
) : BasicKeyViewHolder() {

    override val view: View
        get() = TODO("Not yet implemented")

    override fun onSizeChanged(width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun onDecorationChanged(
        key: DecorationKey,
        isSticky: Boolean
    ) {
        TODO("Not yet implemented")
    }


}