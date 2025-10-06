package com.lollipop.codeboard.view.key

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.lollipop.codeboard.KeyboardConfig
import com.lollipop.codeboard.R
import com.lollipop.codeboard.drawable.RoundStyle
import com.lollipop.codeboard.drawable.RoundedBackground
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import com.lollipop.codeboard.ui.KeyboardTheme

class DecorationKeyViewHolder(
    context: Context,
    private val info: KeyInfo
) : BasicKeyViewHolder(context) {

    companion object {
        private fun getContent(context: Context, key: Keys.Key?, info: KeyInfo): DecorationView {
            when (key) {
                Keys.Decoration.Shift -> {
                    return iconButton(context = context, icon = R.drawable.ic_shift_24)
                }

                Keys.Decoration.Command -> {
                    return iconButton(
                        context = context,
                        icon = R.drawable.ic_keyboard_command_key_24
                    )
                }

                Keys.Decoration.Option -> {
                    return iconButton(
                        context = context,
                        icon = R.drawable.ic_keyboard_option_key_24
                    )
                }

                Keys.Decoration.Backspace -> {
                    return iconButton(context = context, icon = R.drawable.ic_backspace_24)
                }

                Keys.Decoration.Enter -> {
                    return iconButton(context = context, icon = R.drawable.ic_keyboard_return_24)
                }

                Keys.Decoration.Space -> {
                    return textButton(
                        context = context,
                        text = KeyboardConfig.language.optName(context = context)
                    )
                }

                Keys.Decoration.Delete -> {
                    return iconButton(context = context, icon = R.drawable.ic_backspace_24)
                }

                Keys.Decoration.Tab -> {
                    return iconButton(context = context, icon = R.drawable.ic_keyboard_tab_24)
                }

                Keys.Decoration.Escape -> {
                    return textButton(context = context, text = "Esc")
                }

                Keys.Decoration.CapsLock -> {
                    return textButton(context = context, text = "Caps")
                }

                Keys.Decoration.ArrowUp -> {
                    return iconButton(context = context, icon = R.drawable.ic_keyboard_arrow_up_24)
                }

                Keys.Decoration.ArrowDown -> {
                    return iconButton(
                        context = context,
                        icon = R.drawable.ic_keyboard_arrow_down_24
                    )
                }

                Keys.Decoration.ArrowLeft -> {
                    return iconButton(
                        context = context,
                        icon = R.drawable.ic_keyboard_arrow_left_24
                    )
                }

                Keys.Decoration.ArrowRight -> {
                    return iconButton(
                        context = context,
                        icon = R.drawable.ic_keyboard_arrow_right_24
                    )
                }

                Keys.Decoration.Language -> {
                    return iconButton(
                        context = context,
                        icon = R.drawable.ic_language_24
                    )
                }

                Keys.Decoration.Symbol -> {
                    return iconButton(
                        context = context,
                        icon = R.drawable.ic_emoji_symbols_24
                    )
                }
            }
            return textButton(context = context, text = info.key)
        }

        private fun iconButton(context: Context, @DrawableRes icon: Int): DecorationView {
            return DecorationView.Icon(context, icon)
        }

        private fun textButton(context: Context, text: String): DecorationView {
            return DecorationView.Text(context, text)
        }
    }

    private val keyBackground by lazy {
        createBackground(getKeyStyle(false))
    }

    private val dotDrawable by lazy {
        RoundedBackground(RoundStyle.Relative(0.5F))
    }

    private val iconView by lazy {
        getContent(context, keyType, info).also {
            it.view.background = keyBackground
        }
    }

    private val stickyDotView by lazy {
        AppCompatImageView(context).also {
            it.background = dotDrawable
            it.scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

    private val keyView by lazy {
        FrameLayout(context).apply {
            addView(
                iconView.view,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            val dp6 = dp(6).toInt()
            addView(
                stickyDotView,
                FrameLayout.LayoutParams(dp6, dp6).apply {
                    gravity = Gravity.END or Gravity.TOP
                    val margin = dp(2).toInt()
                    marginStart = margin
                    topMargin = margin
                    bottomMargin = margin
                    marginEnd = margin
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

    private var onDecorationTouchListener: OnDecorationTouchListener? = null

    init {
        iconView.view.setOnClickListener {
            onKeyClick(keyType, info)
        }
        val decoration = decorationType
        if (decoration != null) {
            iconView.view.setOnTouchListener(
                DecorationTouchListener(
                    decoration,
                    ::onDecorationTouch
                )
            )
        } else {
            bindKeyTouch(iconView.view, keyType, info)
        }
    }

    fun setOnDecorationTouchListener(listener: OnDecorationTouchListener) {
        onDecorationTouchListener = listener
    }

    private fun onDecorationTouch(dk: DecorationKey, isPressed: Boolean) {
        onDecorationTouchListener?.onDecorationTouch(dk, isPressed)
    }

    override fun onSizeChanged(
        panelWidth: Int,
        panelHeight: Int,
        rowWidth: Int,
        rowHeight: Int,
        keyWidth: Int,
        keyHeight: Int
    ) {
        super.onSizeChanged(panelWidth, panelHeight, rowWidth, rowHeight, keyWidth, keyHeight)
        iconView.onSizeChanged(keyWidth, keyHeight)
    }

    override fun onDecorationChanged(
        key: DecorationKey,
        isSticky: Boolean
    ) {
        stickyDotView.isVisible = isSticky && key == decorationType
    }

    override fun onThemeChanged(theme: KeyboardTheme) {
        val keyTheme = theme.decorationTheme
        keyBackground.setTheme(keyTheme)
        iconView.onThemeChanged(keyTheme)
        dotDrawable.setColor(keyTheme.stickyColor)
        iconView.let {
            if (it is DecorationView.Text && keyType == Keys.Decoration.Space) {
                val text = KeyboardConfig.language.optName(context = view.context)
                it.textView.text = text
            }
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

    private class DecorationTouchListener(
        private val decorationKey: DecorationKey,
        val callback: (DecorationKey, isPressed: Boolean) -> Unit
    ) : View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(
            v: View?,
            event: MotionEvent?
        ): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    callback(decorationKey, true)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    callback(decorationKey, false)
                }
            }
            return false
        }
    }

    fun interface OnDecorationTouchListener {
        fun onDecorationTouch(key: DecorationKey, isPressed: Boolean)
    }

}