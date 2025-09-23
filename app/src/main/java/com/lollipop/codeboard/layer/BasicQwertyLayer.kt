package com.lollipop.codeboard.layer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.lollipop.codeboard.databinding.ViewImQwertyBinding
import com.lollipop.codeboard.widget.BasicViewLayer

/**
 * 负责Qwerty键盘的UI，提供基础的UI交互逻辑
 */
abstract class BasicQwertyLayer : BasicViewLayer() {

    protected var binding: ViewImQwertyBinding? = null


    override fun createView(context: Context): View {
        val layout = ViewImQwertyBinding.inflate(LayoutInflater.from(context))
        bind(layout.keyboardView)
        onBindingCreated(layout)
        binding = layout
        return layout.root
    }

    protected open fun onBindingCreated(binding: ViewImQwertyBinding) {}

}