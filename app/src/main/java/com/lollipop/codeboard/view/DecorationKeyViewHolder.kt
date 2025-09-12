package com.lollipop.codeboard.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
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

    private val iconView by lazy {
        AppCompatImageView(context).apply {
            background = keyBackground
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

    override val view: View
        get() {
            return iconView
        }

    private var stateIconDrawable: KeyStateDrawable? = null
    private var stickyIconDrawable: Drawable? = null

    private val keyType by lazy {
        Keys.findKey(info.key)
    }

    private val decorationType by lazy {
        toDecoration()
    }

    init {
        bindInfo()
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
        updateIcon()
    }

    override fun onThemeChanged(theme: BoardTheme) {
        val keyTheme = theme.decorationTheme
        keyBackground.setTheme(keyTheme)
        iconView.imageTintList = keyTheme.contentStateList
    }

    private fun bindInfo() {
        updateIcon()
    }

    private fun updateIcon() {
        val key = keyType
        if (key is Keys.Decoration) {
            iconView.setImageDrawable(getIconRes(key, isSticky && decorationType == decorationKey))
        } else {
            TODO("Not yet implemented")
        }
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

    private fun getIconRes(key: Keys.Decoration, isSticky: Boolean): Drawable {
        when (key) {
            Keys.Decoration.Shift -> {
                return optIconDrawable(
                    isSticky,
                    def = R.drawable.ic_shift_outline_24,
                    press = R.drawable.ic_shift_fill_24,
                    sticky = R.drawable.ic_shift_lock_24
                )
            }

            Keys.Decoration.Command -> TODO()
            Keys.Decoration.Option -> TODO()
            Keys.Decoration.Backspace -> TODO()
            Keys.Decoration.Enter -> TODO()
            Keys.Decoration.Space -> TODO()
            Keys.Decoration.Delete -> TODO()
            Keys.Decoration.Tab -> TODO()
            Keys.Decoration.Escape -> TODO()
            Keys.Decoration.CapsLock -> TODO()
            Keys.Decoration.ArrowUp -> TODO()
            Keys.Decoration.ArrowDown -> TODO()
            Keys.Decoration.ArrowLeft -> TODO()
            Keys.Decoration.ArrowRight -> TODO()
        }
    }

    private fun optIconDrawable(isSticky: Boolean, def: Int, press: Int, sticky: Int): Drawable {
        if (isSticky) {
            return optStickyDrawable(sticky)
        } else {
            return optIconDrawable(def, press)
        }
    }

    private fun optStickyDrawable(resId: Int): Drawable {
        val drawable = stickyIconDrawable
        if (drawable != null) {
            return drawable
        }
        val newDrawable = ContextCompat.getDrawable(view.context, resId)!!
        stickyIconDrawable = newDrawable
        return newDrawable
    }

    private fun optIconDrawable(def: Int, press: Int): KeyStateDrawable {
        val stateDrawable = stateIconDrawable
        if (stateDrawable != null) {
            return stateDrawable
        }
        val newDrawable = createStateIcon(def, press)
        stateIconDrawable = newDrawable
        return newDrawable
    }

}