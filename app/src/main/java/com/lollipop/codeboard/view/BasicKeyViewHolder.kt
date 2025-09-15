package com.lollipop.codeboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.lollipop.codeboard.keyboard.BoardTheme
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.KeyTheme
import com.lollipop.codeboard.keyboard.KeyboardTheme
import com.lollipop.codeboard.keyboard.Keys
import kotlin.math.min

abstract class BasicKeyViewHolder(protected val context: Context) : KeyboardView.KeyHolder {

    companion object {
        private const val KEY_RADIUS = 6
    }

    protected var decorationKey: DecorationKey = DecorationKey.Empty
    protected var isSticky = false

    protected var theme: BoardTheme? = null

    protected var clickListener: OnKeyClickListener? = null

    protected var touchListener: OnKeyTouchListener? = null

    private val keyTouchCallbackWrapper = object : OnKeyTouchListener {
        override fun onKeyTouch(key: Keys.Key?, info: KeyInfo, isPressed: Boolean) {
            notifyKeyTouch(key, info, isPressed)
        }
    }

    protected fun dp(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics
        )
    }

    protected fun getKeyStyle(circle: Boolean): RoundStyle {
        return if (circle) {
            RoundStyle.Relative(0.5F)
        } else {
            RoundStyle.Absolute(dp(KEY_RADIUS))
        }
    }

    protected fun createDecorationKeyBackground(circle: Boolean): RoundedKeyBackground {
        return createBackground(getKeyStyle(circle)).apply {
            setTheme(KeyboardTheme.theme.decorationTheme)
        }
    }

    protected fun createSingleKeyBackground(): RoundedKeyBackground {
        return createBackground(getKeyStyle(false)).apply {
            setTheme(KeyboardTheme.theme.keyTheme)
        }
    }

    protected fun createBackground(style: RoundStyle): RoundedKeyBackground {
        return RoundedKeyBackground(style)
    }

    fun setOnKeyClickListener(listener: OnKeyClickListener) {
        clickListener = listener
    }

    fun setOnKeyTouchListener(listener: OnKeyTouchListener) {
        touchListener = listener
    }

    protected fun onKeyClick(key: Keys.Key?, info: KeyInfo) {
        clickListener?.onKeyClick(key, info)
    }

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

    protected fun bindKeyTouch(view: View, key: Keys.Key?, info: KeyInfo) {
        view.setOnTouchListener(KeyTouchListener(key, info, keyTouchCallbackWrapper))
    }

    protected fun notifyKeyTouch(key: Keys.Key?, info: KeyInfo, isPressed: Boolean) {
        touchListener?.onKeyTouch(key, info, isPressed)
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

        var pressColor: Int = Color.GRAY

        var defaultColor: Int = Color.WHITE

        fun setColor(defaultColor: Int, pressColor: Int) {
            this.defaultColor = defaultColor
            this.pressColor = pressColor
            invalidateSelf()
        }

        fun setTheme(theme: KeyTheme) {
            setColor(defaultColor = theme.backgroundDefault, pressColor = theme.backgroundPress)
        }

        fun setStyle(style: RoundStyle) {
            roundStyle = style
            buildPath()
        }

        override fun isStateful(): Boolean {
            return true
        }

        override fun onStateChange(state: IntArray): Boolean {
            if (state.contains(android.R.attr.state_pressed)) {
                paint.color = pressColor
            } else {
                paint.color = defaultColor
            }
            invalidateSelf()
            return true
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
            if (roundBounds.isEmpty) {
                return
            }
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

    protected class KeyTouchListener(
        val key: Keys.Key?,
        val info: KeyInfo,
        val callback: OnKeyTouchListener
    ) : View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(
            v: View?,
            event: MotionEvent?
        ): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    callback.onKeyTouch(key, info, true)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    callback.onKeyTouch(key, info, false)
                }
            }
            return false
        }

    }

    interface OnKeyClickListener {
        fun onKeyClick(key: Keys.Key?, info: KeyInfo)
    }

    interface OnKeyTouchListener {
        fun onKeyTouch(key: Keys.Key?, info: KeyInfo, isPressed: Boolean)
    }

}