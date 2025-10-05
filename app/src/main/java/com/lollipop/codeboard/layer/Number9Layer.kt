package com.lollipop.codeboard.layer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.lollipop.codeboard.databinding.ViewImNumber9Binding
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.view.KeyboardView
import com.lollipop.codeboard.view.key.KeyboardAdapter
import com.lollipop.codeboard.widget.BasicViewLayer

class Number9Layer : BasicViewLayer() {

    companion object {
        const val KEY_SYMBOL_LIST = "SymbolList"
    }

    protected var binding: ViewImNumber9Binding? = null

    override fun createView(context: Context): View {
        val layout = ViewImNumber9Binding.inflate(LayoutInflater.from(context))
        bind(layout.keyboardView)
        onBindingCreated(layout)
        binding = layout
        return layout.root
    }

    override fun createAdapter(
        context: Context,
        callback: KeyboardAdapter.Callback
    ): KeyboardView.KeyViewAdapter {
        return Number9Adapter(context, callback)
    }

    private fun onBindingCreated(binding: ViewImNumber9Binding) {}

    private class Number9Adapter(
        context: Context,
        callback: Callback
    ) : KeyboardAdapter(context, callback) {

        override fun createHodler(info: KeyInfo): KeyboardView.KeyHolder {
            if (info.key == KEY_SYMBOL_LIST) {
                TODO()
            }
            return super.createHodler(info)
        }

    }

}