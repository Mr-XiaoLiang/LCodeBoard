package com.lollipop.codeboard.view.key

import android.content.Context
import android.view.View
import com.lollipop.codeboard.drawable.RoundedBackground
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import com.lollipop.codeboard.ui.KeyboardTheme

open class SingleKeyViewHolder(
    context: Context,
    private val info: KeyInfo
) : BasicKeyViewHolder(context) {

    companion object {
        private fun getContent(context: Context, key: Keys.Key?, info: KeyInfo): CodeView {
            return CodeView(context = context, text = info.key)
        }
    }

    private val keyBackground by lazy {
        createKeyBackground()
    }

    private val contentView by lazy {
        getContent(context, keyType, info).also {
            it.textView.background = keyBackground
        }
    }

    override val view: View
        get() {
            return contentView.textView
        }

    private val keyType by lazy {
        Keys.findKey(info.key)
    }

    private val shiftKeyType by lazy {
        Keys.findKey(info.shiftCase)
    }

    private val commandKeyType by lazy {
        Keys.findKey(info.commandCase)
    }

    private val optionKeyType by lazy {
        Keys.findKey(info.optionCase)
    }

    init {
        contentView.textView.setOnClickListener {
            onKeyClick()
        }
        bindKeyTouch(contentView.textView, keyType, info)
    }

    protected open fun createKeyBackground(): RoundedBackground? {
        return createSingleKeyBackground()
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
        contentView.onSizeChanged(keyWidth, keyHeight)
    }

    override fun onDecorationChanged(
        key: DecorationKey,
        isSticky: Boolean
    ) {
        contentView.updateText(info = info, decorationKey = key)
    }

    override fun onThemeChanged(theme: KeyboardTheme) {
        val keyTheme = theme.keyTheme
        keyBackground?.setTheme(keyTheme)
        contentView.onThemeChanged(keyTheme)
    }

    private fun onKeyClick() {
        when (decorationKey) {
            DecorationKey.Shift -> {
                onKeyClick(shiftKeyType, info)
            }

            DecorationKey.Command -> {
                onKeyClick(commandKeyType, info)
            }

            DecorationKey.Option -> {
                onKeyClick(optionKeyType, info)
            }

            DecorationKey.Empty -> {
                onKeyClick(keyType, info)
            }
        }
    }

}