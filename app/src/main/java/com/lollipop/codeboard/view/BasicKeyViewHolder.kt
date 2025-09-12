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
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyTheme
import com.lollipop.codeboard.keyboard.KeyboardTheme
import kotlin.math.min

abstract class BasicKeyViewHolder : KeyboardView.KeyHolder {

    companion object {
        private const val KEY_RADIUS = 6F
    }

    protected var decorationKey: DecorationKey = DecorationKey.Empty
    protected var isSticky = false

    override fun onDecorationKeyChanged(key: DecorationKey, isSticky: Boolean) {
        this.decorationKey = key
        this.isSticky = isSticky
        onDecorationChanged(key, isSticky)
    }

    protected abstract fun onDecorationChanged(key: DecorationKey, isSticky: Boolean)

    protected fun createDecorationKeyBackground(circle: Boolean): KeyBackground {
        val style = if (circle) {
            RoundStyle.Relative(0.5F)
        } else {
            RoundStyle.Absolute(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    KEY_RADIUS,
                    view.resources.displayMetrics
                )
            )
        }
        return createBackground(
            roundStyle = style,
            keyTheme = KeyboardTheme.theme.decorationTheme
        )
    }

    protected fun createSingleKeyBackground(): KeyBackground {
        val radius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            KEY_RADIUS,
            view.resources.displayMetrics
        )
        return createBackground(
            roundStyle = RoundStyle.Absolute(radius),
            keyTheme = KeyboardTheme.theme.keyTheme
        )
    }

    protected fun createBackground(
        roundStyle: RoundStyle,
        keyTheme: KeyTheme,
    ): KeyBackground {
        return KeyBackground().apply {
            addPressedState(
                RoundedKeyBackground(roundStyle).apply {
                    color = keyTheme.backgroundPress
                }
            )
            addNormalState(
                RoundedKeyBackground(roundStyle).apply {
                    color = keyTheme.backgroundDefault
                }
            )
        }
    }

    protected open class KeyBackground : StateListDrawable() {

        fun addPressedState(drawable: Drawable) {
            addState(intArrayOf(android.R.attr.state_pressed), drawable)
        }

        fun addNormalState(drawable: Drawable) {
            addState(intArrayOf(), drawable)
        }

    }

    protected class RoundedKeyBackground(
        val roundStyle: RoundStyle,
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