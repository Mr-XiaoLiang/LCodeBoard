package com.lollipop.codeboard.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.lollipop.codeboard.R
import com.lollipop.codeboard.keyboard.BoardTheme
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import kotlin.math.min

class DecorationKeyViewHolder(
    context: Context,
    private val info: KeyInfo
) : BasicKeyViewHolder() {

    private val keyBackground by lazy {
        createBackground()
    }

    private val dotDrawable by lazy {
        RoundedKeyBackground(RoundStyle.Relative(0.5F))
    }

    private val iconView by lazy {
        AppCompatImageView(context).apply {
            background = keyBackground
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

    private val stickyDotView by lazy {
        AppCompatImageView(context).apply {
            background = dotDrawable
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

    private val keyView by lazy {
        FrameLayout(context).apply {
            addView(
                iconView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            val dp6 = dp(6).toInt()
            addView(
                stickyDotView,
                FrameLayout.LayoutParams(dp6, dp6).apply {
                    gravity = Gravity.END or Gravity.TOP
                })
        }
    }

    override val view: View
        get() {
            return keyView
        }

    private val keyType by lazy {
        Keys.findKey(info.key)
    }

    private val decorationType by lazy {
        toDecoration()
    }

    init {
        val key = keyType
        if (key is Keys.Decoration) {
            iconView.setImageDrawable(getIconRes(key))
        } else {
            iconView.setImageDrawable(null)
        }
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
        iconView.setPadding(paddingH.toInt(), paddingV.toInt(), paddingH.toInt(), paddingV.toInt())
    }

    override fun onDecorationChanged(
        key: DecorationKey,
        isSticky: Boolean
    ) {
        stickyDotView.isVisible = isSticky && key == decorationType
    }

    override fun onThemeChanged(theme: BoardTheme) {
        val keyTheme = theme.decorationTheme
        keyBackground.setTheme(keyTheme)
        iconView.imageTintList = keyTheme.contentStateList
        dotDrawable.color = keyTheme.stickyColor
    }

    private fun toDecoration(): DecorationKey? {
        if (keyType is Keys.Decoration) {
            when (keyType) {
                Keys.Decoration.Shift -> {
                    return DecorationKey.Shift
                }

                Keys.Decoration.Command -> {
                    return DecorationKey.Command
                }

                Keys.Decoration.Option -> {
                    return DecorationKey.Option
                }
            }
        }
        return null
    }

    private fun getIconRes(key: Keys.Decoration): Drawable? {
        when (key) {
            Keys.Decoration.Shift -> {
                return createIconDrawable(icon = R.drawable.ic_shift_24)
            }

            Keys.Decoration.Command -> {
                return createIconDrawable(icon = R.drawable.ic_keyboard_command_key_24)
            }

            Keys.Decoration.Option -> {
                return createIconDrawable(icon = R.drawable.ic_keyboard_option_key_24)
            }

            Keys.Decoration.Backspace -> {
                return createIconDrawable(icon = R.drawable.ic_backspace_24)
            }

            Keys.Decoration.Enter -> {
                return createIconDrawable(icon = R.drawable.ic_keyboard_return_24)
            }

            Keys.Decoration.Space -> {
                // TODO 显示语言
            }

            Keys.Decoration.Delete -> {
                return createIconDrawable(icon = R.drawable.ic_backspace_24)
            }

            Keys.Decoration.Tab -> {
                return createIconDrawable(icon = R.drawable.ic_keyboard_tab_24)
            }

            Keys.Decoration.Escape -> {
                return null
            }

            Keys.Decoration.CapsLock -> {
                return null
            }

            Keys.Decoration.ArrowUp -> {
                return null
            }

            Keys.Decoration.ArrowDown -> {
                return null
            }

            Keys.Decoration.ArrowLeft -> {
                return null
            }

            Keys.Decoration.ArrowRight -> {
                return null
            }
        }
        return null
    }

}