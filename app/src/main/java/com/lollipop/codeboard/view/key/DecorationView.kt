package com.lollipop.codeboard.view.key

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.lollipop.codeboard.KeyboardConfig
import com.lollipop.codeboard.ui.KeyTheme
import kotlin.math.min

sealed class DecorationView {

    abstract val view: View

    abstract fun onSizeChanged(width: Int, height: Int)

    abstract fun onThemeChanged(theme: KeyTheme)

    protected fun dp(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            view.resources.displayMetrics
        )
    }

    class Icon(context: Context, @DrawableRes icon: Int) : DecorationView() {

        val iconView = AppCompatImageView(context).apply {
            setImageResource(icon)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        override val view: View
            get() {
                return iconView
            }

        override fun onSizeChanged(width: Int, height: Int) {
            val maxThreshold = dp(36)
            val minThreshold = dp(28)
            val keySize = min(width, height)
            val iconSize = if (keySize > maxThreshold) {
                keySize * 0.5F
            } else if (keySize < minThreshold) {
                keySize * 0.8F
            } else {
                dp(24)
            }
            val paddingH = (width - iconSize) / 2
            val paddingV = (height - iconSize) / 2
            iconView.setPadding(
                paddingH.toInt(),
                paddingV.toInt(),
                paddingH.toInt(),
                paddingV.toInt()
            )
        }

        override fun onThemeChanged(theme: KeyTheme) {
            iconView.imageTintList = theme.contentStateList
        }

    }

    class Text(context: Context, text: String) : DecorationView() {

        val textView = TextView(context).apply {
            this.text = text
            gravity = Gravity.CENTER
        }

        override val view: View
            get() {
                return textView
            }

        override fun onSizeChanged(width: Int, height: Int) {
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

        override fun onThemeChanged(theme: KeyTheme) {
            textView.setTextColor(theme.contentStateList)
            KeyboardConfig.bindKeyFont(textView)
        }

    }

}