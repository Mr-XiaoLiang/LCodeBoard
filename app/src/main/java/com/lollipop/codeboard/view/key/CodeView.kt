package com.lollipop.codeboard.view.key

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.lollipop.codeboard.KeyboardConfig
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.ui.KeyTheme
import kotlin.math.min

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
        var length = textView.text.length
        if (length < 1) {
            length = 1
        }
        val widthSize = width / length
        val heightSize = height
        if (widthSize < 1 || heightSize < 1) {
            return
        }
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
        KeyboardConfig.bindKeyFont(textView)
    }

}
