package com.lollipop.codeboard.drawable

import android.graphics.Rect
import kotlin.math.min

sealed class RoundStyle {

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