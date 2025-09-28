package com.lollipop.codeboard.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.lollipop.codeboard.ui.KeyTheme

class RoundedBackground(
    rStyle: RoundStyle
) : Drawable() {

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isDither = true
        isAntiAlias = true
    }

    var roundStyle: RoundStyle = rStyle
        private set

    private val roundBounds = Path()

    var pressColor: Int = Color.GRAY

    var defaultColor: Int = Color.WHITE

    fun setColor(color: Int) {
        setColor(defaultColor = color, pressColor = color)
    }

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
        return pressColor != defaultColor
    }

    override fun onStateChange(state: IntArray): Boolean {
        if (pressColor == defaultColor) {
            return false
        }
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