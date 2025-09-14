package com.lollipop.codeboard.view

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.lollipop.codeboard.keyboard.BoardTheme
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.KeyTheme
import com.lollipop.codeboard.keyboard.Keys
import kotlin.math.min

class SingleKeyViewHolder(
    context: Context,
    private val info: KeyInfo
) : BasicKeyViewHolder(context) {

    companion object {
        private fun getContent(context: Context, key: Keys.Key?, info: KeyInfo): CodeView {
            return CodeView(context = context, text = info.key)
        }
    }

    private val keyBackground by lazy {
        createSingleKeyBackground()
    }

    private val contentView by lazy {
        getContent(context, keyType, info).also {
            it.textView.background = keyBackground
//                RoundedKeyBackground(roundStyle = RoundStyle.Absolute(radius = 0F))
//            it.textView.setBackgroundColor(Color.GRAY)
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
    }

    override fun onSizeChanged(width: Int, height: Int) {
        contentView.onSizeChanged(width, height)
    }

    override fun onDecorationChanged(
        key: DecorationKey,
        isSticky: Boolean
    ) {
        contentView.updateText(info = info, decorationKey = key)
    }

    override fun onThemeChanged(theme: BoardTheme) {
        val keyTheme = theme.keyTheme
        keyBackground.setTheme(keyTheme)
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

    class CodeView(private val context: Context, text: String) {

        val textView = TextView(context).apply {
            this.text = text
            gravity = Gravity.CENTER
        }

        fun updateText(info: KeyInfo, decorationKey: DecorationKey) {
            textView.text = when (decorationKey) {
                DecorationKey.Shift -> {
                    info.shiftCase
                }

                DecorationKey.Command -> {
                    info.commandCase
                }

                DecorationKey.Option -> {
                    info.optionCase
                }

                DecorationKey.Empty -> {
                    info.key
                }
            }
        }

        private fun dp(value: Int): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),
                textView.resources.displayMetrics
            )
        }

        fun onSizeChanged(width: Int, height: Int) {
            val widthSize = width / textView.text.length
            val heightSize = height
            val size = min(widthSize, heightSize)
            val maxSize = dp(18)
            val minSize = dp(12)

            val textSize = if (size > maxSize) {
                size * 0.8F
            } else if (size < minSize) {
                size * 0.9F
            } else {
                minSize
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }

        fun onThemeChanged(theme: KeyTheme) {
            textView.setTextColor(theme.contentStateList)
        }

    }


}