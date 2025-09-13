package com.lollipop.codeboard.view

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.lollipop.codeboard.keyboard.BoardTheme
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyTheme
import com.lollipop.codeboard.keyboard.KeyboardTheme
import kotlin.math.min

abstract class BasicKeyViewHolder : KeyboardView.KeyHolder {

    companion object {
        private const val KEY_RADIUS = 6
    }

    protected var decorationKey: DecorationKey = DecorationKey.Empty
    protected var isSticky = false

    protected var theme: BoardTheme? = null

    override fun onDecorationKeyChanged(key: DecorationKey, isSticky: Boolean) {
        this.decorationKey = key
        this.isSticky = isSticky
        onDecorationChanged(key, isSticky)
    }

    override fun updateTheme(theme: BoardTheme) {
        this.theme = theme
        onThemeChanged(theme)
    }

    protected abstract fun onDecorationChanged(key: DecorationKey, isSticky: Boolean)

    protected abstract fun onThemeChanged(theme: BoardTheme)

    protected fun getKeyStyle(circle: Boolean): RoundStyle {
        return if (circle) {
            RoundStyle.Relative(0.5F)
        } else {
            RoundStyle.Absolute(dp(KEY_RADIUS))
        }
    }

    protected fun createDecorationKeyBackground(circle: Boolean): KeyStateDrawable {
        return createBackground().apply {
            setStyle(getKeyStyle(circle))
            setTheme(KeyboardTheme.theme.decorationTheme)
        }
    }

    protected fun createSingleKeyBackground(): KeyStateDrawable {
        return createBackground().apply {
            setStyle(getKeyStyle(false))
            setTheme(KeyboardTheme.theme.keyTheme)
        }
    }

    protected fun createBackground(): ColorKeyBackground {
        return ColorKeyBackground()
    }

    protected fun dp(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            view.resources.displayMetrics
        )
    }

    protected fun createStateIcon(
        @DrawableRes defaultIcon: Int,
        @DrawableRes pressIcon: Int
    ): KeyStateDrawable {
        return KeyStateDrawable().apply {
            createIconDrawable(pressIcon)?.let {
                addPressedState(it)
            }
            createIconDrawable(defaultIcon)?.let {
                addNormalState(it)
            }
        }
    }

    protected fun createIconDrawable(@DrawableRes icon: Int): Drawable? {
        return ContextCompat.getDrawable(view.context, icon)
    }

    protected open class ColorKeyBackground : KeyStateDrawable() {

        val pressDrawable = RoundedKeyBackground(RoundStyle.Relative(0.5F))
        val normalDrawable = RoundedKeyBackground(RoundStyle.Relative(0.5F))

        init {
            addPressedState(pressDrawable)
            addNormalState(normalDrawable)
        }

        fun setColor(defaultColor: Int, pressColor: Int) {
            normalDrawable.color = defaultColor
            pressDrawable.color = pressColor
        }

        fun setTheme(theme: KeyTheme) {
            setColor(defaultColor = theme.backgroundDefault, pressColor = theme.backgroundPress)
        }

        fun setStyle(style: RoundStyle) {
            pressDrawable.roundStyle = style
            normalDrawable.roundStyle = style
        }

    }

    protected open class KeyStateDrawable : StateListDrawable() {

        fun addPressedState(drawable: Drawable) {
            addState(intArrayOf(android.R.attr.state_pressed), drawable)
        }

        fun addNormalState(drawable: Drawable) {
            addState(intArrayOf(), drawable)
        }

    }

    protected class RoundedKeyBackground(
        var roundStyle: RoundStyle,
    ) : Drawable() {

        private val paint = Paint().apply {
            style = Paint.Style.FILL
            isDither = true
            isAntiAlias = true
        }

        private val roundBounds = Path()

        var color: Int
            get() {
                return paint.color
            }
            set(value) {
                paint.color = value
                invalidateSelf()
            }

        override fun onBoundsChange(bounds: Rect) {
            super.onBoundsChange(bounds)
            buildPath()
        }

        private fun buildPath() {
            roundBounds.reset()
            val rect = bounds
            if (rect.isEmpty) {
                return
            }
            val radius = roundStyle.getRadius(rect)
            roundBounds.addRoundRect(
                rect.left.toFloat(),
                rect.top.toFloat(),
                rect.right.toFloat(),
                rect.bottom.toFloat(),
                radius,
                radius,
                Path.Direction.CW
            )
        }

        override fun draw(canvas: Canvas) {
            canvas.drawPath(roundBounds, paint)
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSPARENT
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

    }

    protected sealed class RoundStyle {

        abstract fun getRadius(bounds: Rect): Float

        class Absolute(private val radius: Float) : RoundStyle() {

            override fun getRadius(bounds: Rect): Float {
                return radius
            }

        }

        class Relative(private val percent: Float) : RoundStyle() {

            override fun getRadius(bounds: Rect): Float {
                return min(bounds.width(), bounds.height()) * percent
            }

        }

    }

}