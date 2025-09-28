package com.lollipop.codeboard.ui

import android.content.Context
import android.util.TypedValue
import android.view.View
import com.lollipop.codeboard.data.GlobalCache
import com.lollipop.codeboard.databinding.ViewImFrameBinding
import com.lollipop.codeboard.drawable.RoundStyle
import com.lollipop.codeboard.drawable.RoundedBackground

object FrameThemeHelper {

    var floatingStyle: FrameStyle = FrameStyle(
        radius = 10,
        margin = FrameInsets(6, 6, 6, 6),
        padding = FrameInsets(2, 2, 2, 2)
    )
        private set

    var normalStyle: FrameStyle = FrameStyle(
        radius = 0,
        margin = FrameInsets(0, 0, 0, 0),
        padding = FrameInsets(0, 0, 0, 0)
    )
        private set

    fun update(binding: ViewImFrameBinding) {
        val theme = Skin.current.keyboard
        val isFloating = GlobalCache.isFloating
        val style = if (isFloating) {
            floatingStyle
        } else {
            normalStyle
        }
        val frameContent = binding.frameContent
        updateInsets(binding, style)
        updateBackground(frameContent, isFloating, style, theme)
    }

    private fun updateInsets(binding: ViewImFrameBinding, style: FrameStyle) {
        binding.frameContent.setPadding(
            style.padding.leftPx(binding.root.context),
            style.padding.topPx(binding.root.context),
            style.padding.rightPx(binding.root.context),
            style.padding.bottomPx(binding.root.context)
        )
        binding.frameRoot.setPadding(
            style.margin.leftPx(binding.root.context),
            style.margin.topPx(binding.root.context),
            style.margin.rightPx(binding.root.context),
            style.margin.bottomPx(binding.root.context)
        )
    }

    private fun updateBackground(
        frameContent: View,
        isFloating: Boolean,
        style: FrameStyle,
        theme: KeyboardTheme
    ) {
        val contentBackground = frameContent.background
        val roundStyle = if (isFloating) {
            RoundStyle.Absolute(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    style.radius.toFloat(),
                    frameContent.context.resources.displayMetrics
                )
            )
        } else {
            RoundStyle.Absolute(0F)
        }
        if (contentBackground is RoundedBackground) {
            contentBackground.setColor(theme.backgroundColor)
            contentBackground.setStyle(roundStyle)
            contentBackground.invalidateSelf()
        } else {
            val newBackground = RoundedBackground(roundStyle)
            newBackground.setColor(theme.backgroundColor)
            frameContent.background = newBackground
        }
    }

    class FrameStyle(
        val radius: Int,
        val margin: FrameInsets,
        val padding: FrameInsets
    )

    class FrameInsets(val left: Int, val top: Int, val right: Int, val bottom: Int) {

        private fun dp2px(context: Context, dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }

        fun leftPx(context: Context): Int {
            return dp2px(context, left)
        }

        fun topPx(context: Context): Int {
            return dp2px(context, top)
        }

        fun rightPx(context: Context): Int {
            return dp2px(context, right)
        }

        fun bottomPx(context: Context): Int {
            return dp2px(context, bottom)
        }

    }

}