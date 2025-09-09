package com.lollipop.codeboard.widget

import android.content.Context
import android.view.View

interface Layer {

    fun create(context: Context, owner: LayerOwner): View

    fun onInsetsChange(left: Int, top: Int, right: Int, bottom: Int)

    fun onShow()

    fun onHide()

    fun nextLayer(tag: String)

}

interface LayerOwner {

    fun nextLayer(tag: String)

    fun getProvider(): ConnectionProvider?

}
