package com.lollipop.codeboard.layer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import com.lollipop.codeboard.databinding.ViewImNumber9Binding
import com.lollipop.codeboard.drawable.RoundedBackground
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.ui.KeyboardTheme
import com.lollipop.codeboard.view.KeyboardView
import com.lollipop.codeboard.view.key.BasicKeyViewHolder
import com.lollipop.codeboard.view.key.KeyboardAdapter
import com.lollipop.codeboard.view.key.SingleKeyViewHolder
import com.lollipop.codeboard.widget.BasicViewLayer

class Number9Layer : BasicViewLayer() {

    companion object {
        const val KEY_SYMBOL_LIST = "SymbolList"

        @JvmStatic
        private fun symbolKey(key: String): KeyInfo {
            return KeyInfo(
                key = key,
                shiftCase = key,
                commandCase = key,
                optionCase = key,
                weight = 1f,
                span = 1
            )
        }

        // 暂时先写死一个符号列表
        val symbolList = listOf(
            symbolKey("+"),
            symbolKey("-"),
            symbolKey("*"),
            symbolKey("/"),
            symbolKey("("),
            symbolKey(")"),
        )
    }

    private var binding: ViewImNumber9Binding? = null

    private var keyAdapter: Number9Adapter? = null

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
        val newAdapter = Number9Adapter(context, callback)
        keyAdapter = newAdapter
        newAdapter.resetKey(symbolList)
        return newAdapter
    }

    private fun onBindingCreated(binding: ViewImNumber9Binding) {}

    override fun onShow() {
        super.onShow()
        // 按理说这里应该来更新符号列表
    }

    private class Number9Adapter(
        context: Context,
        callback: Callback
    ) : KeyboardAdapter(context, callback) {

        private var symbolListViewHolder: SymbolListViewHolder? = null
        private val symbolList = mutableListOf<KeyInfo>()

        override fun createHodler(info: KeyInfo): KeyboardView.KeyHolder {
            if (info.key == KEY_SYMBOL_LIST) {
                val viewHolder = SymbolListViewHolder(context)
                symbolListViewHolder = viewHolder
                viewHolder.resetKey(symbolList)
                return viewHolder
            }
            return super.createHodler(info)
        }

        fun resetKey(list: List<KeyInfo>) {
            symbolListViewHolder?.resetKey(list)
            symbolList.clear()
            symbolList.addAll(list)
        }

    }

    private class SymbolListViewHolder(context: Context) : BasicKeyViewHolder(context) {

        private val keyList = mutableListOf<KeyInfo>()

        private val groupBackground by lazy {
            createSingleKeyBackground()
        }

        private val viewGroup by lazy {
            SymbolListView(context).also {
                it.root.background = groupBackground
            }
        }

        fun resetKey(list: List<KeyInfo>) {
            keyList.clear()
            keyList.addAll(list)
            viewGroup.removeAllKeys()
            removeAllKeys()
            for (key in keyList) {
                val child = SymbolHolder(context, key)
                addKey(child)
                viewGroup.addKey(child.view)
            }
        }

        override val view: View
            get() {
                return viewGroup.root
            }

        override fun onDecorationChanged(
            key: DecorationKey,
            isSticky: Boolean
        ) {
        }

        override fun onThemeChanged(theme: KeyboardTheme) {
            groupBackground.setTheme(theme.keyTheme)
        }

        override fun onSizeChanged(
            panelWidth: Int,
            panelHeight: Int,
            rowWidth: Int,
            rowHeight: Int,
            keyWidth: Int,
            keyHeight: Int
        ) {
            super.onSizeChanged(panelWidth, panelHeight, rowWidth, rowHeight, keyWidth, keyHeight)
            viewGroup.updateChildHeight(rowHeight)
        }

        override fun dispatchOnSizeChanged(
            panelWidth: Int,
            panelHeight: Int,
            rowWidth: Int,
            rowHeight: Int,
            keyWidth: Int,
            keyHeight: Int
        ) {
            super.dispatchOnSizeChanged(
                panelWidth,
                panelHeight,
                rowWidth,
                rowHeight,
                keyWidth,
                keyHeight = rowHeight
            )
        }

    }

    private class SymbolListView(private val context: Context) {

        private val childGroup by lazy {
            LinearLayout(context).also {
                it.orientation = LinearLayout.VERTICAL
            }
        }

        private val scrollView by lazy {
            NestedScrollView(context).also {
                it.addView(
                    childGroup,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
        }

        val root: View
            get() {
                return scrollView
            }

        fun removeAllKeys() {
            childGroup.removeAllViews()
        }

        fun addKey(view: View) {
            childGroup.addView(
                view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        fun updateChildHeight(childHeight: Int) {
            val childCount = childGroup.childCount
            for (i in 0 until childCount) {
                val child = childGroup.getChildAt(i)
                child.updateLayoutParams {
                    height = childHeight
                }
            }
        }

        fun removeKey(view: View) {
            childGroup.removeView(view)
        }

    }

    private class SymbolHolder(
        context: Context,
        info: KeyInfo
    ) : SingleKeyViewHolder(context, info) {

        override fun createKeyBackground(): RoundedBackground? {
            return null
        }

    }

}