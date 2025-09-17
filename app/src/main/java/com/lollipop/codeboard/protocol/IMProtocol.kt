package com.lollipop.codeboard.protocol

import android.content.Context
import android.view.View
import android.view.inputmethod.InputConnection
import androidx.recyclerview.widget.RecyclerView

/**
 * 输入面板的抽象
 * 它需要为UI提供一个View组件
 * 同时提供一个绑定输出的函数
 */
interface InputLayer {

    fun createView(context: Context, owner: LayerOwner): View

    fun onInsetsChange(left: Int, top: Int, right: Int, bottom: Int)

    fun getAlternativeAdapter(): RecyclerView.Adapter<*>? {
        return null
    }

    fun onShow()

    fun onHide()

    fun nextLayer(tag: String)

}

interface LayerOwner {

    fun nextLayer(tag: String)

    fun getProvider(): ConnectionProvider?

}

interface ConnectionProvider {

    fun getConnection(): InputConnection?

}

interface Glossary {

    fun bindConnection(provider: ConnectionProvider)

}


