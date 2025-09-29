package com.lollipop.codeboard.view.key

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.lollipop.codeboard.drawable.RoundStyle
import com.lollipop.codeboard.drawable.RoundedBackground
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import com.lollipop.codeboard.ui.KeyboardTheme
import com.lollipop.codeboard.ui.Skin
import com.lollipop.codeboard.view.KeyboardView

abstract class BasicKeyViewHolder(protected val context: Context) : KeyboardView.KeyHolder {

    companion object {
        private const val KEY_RADIUS = 6
    }

    protected var decorationKey: DecorationKey = DecorationKey.Empty
    protected var isSticky = false

    protected var theme: KeyboardTheme? = null

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

    protected fun createDecorationKeyBackground(circle: Boolean): RoundedBackground {
        return createBackground(getKeyStyle(circle)).apply {
            setTheme(Skin.current.keyboard.decorationTheme)
        }
    }

    protected fun createSingleKeyBackground(): RoundedBackground {
        return createBackground(getKeyStyle(false)).apply {
            setTheme(Skin.current.keyboard.keyTheme)
        }
    }

    protected fun createBackground(style: RoundStyle): RoundedBackground {
        return RoundedBackground(style)
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

    override fun updateTheme(theme: KeyboardTheme) {
        this.theme = theme
        onThemeChanged(theme)
    }

    protected abstract fun onDecorationChanged(key: DecorationKey, isSticky: Boolean)

    protected abstract fun onThemeChanged(theme: KeyboardTheme)

    protected fun bindKeyTouch(view: View, key: Keys.Key?, info: KeyInfo) {
        view.setOnTouchListener(KeyTouchListener(key, info, keyTouchCallbackWrapper))
    }

    protected fun notifyKeyTouch(key: Keys.Key?, info: KeyInfo, isPressed: Boolean) {
        touchListener?.onKeyTouch(key, info, isPressed)
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