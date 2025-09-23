package com.lollipop.codeboard.widget

import com.lollipop.codeboard.KeyboardConfig
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import com.lollipop.codeboard.view.KeyboardAdapter
import com.lollipop.codeboard.view.KeyboardView

abstract class BasicViewLayer : BasicLayer() {

    protected var currentKeyboardView: KeyboardView? = null

    protected var decorationKey: DecorationKey = DecorationKey.Empty

    protected fun bind(keyboardView: KeyboardView) {
        currentKeyboardView = keyboardView
        keyboardView.setKeyViewAdapter(
            KeyboardAdapter(
                context = keyboardView.context,
                onKeyClickCallback = ::onKeyClick,
                onDecorationTouchCallback = ::onDecorationTouch,
                onKeyTouchCallback = ::onKeyTouch
            )
        )
    }

    override fun onInsetsChange(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        currentKeyboardView?.setPadding(left, top, right, bottom)
    }

    protected fun updateDecoration(newKey: DecorationKey) {
        log("updateDecoration: $newKey")
        if (newKey != decorationKey) {
            decorationKey = newKey
            currentKeyboardView?.setDecorationKey(decorationKey, KeyboardConfig.stickyMode)
        }
    }

    protected open fun onKeyTouch(key: Keys.Key?, info: KeyInfo, isPressed: Boolean) {

    }

    protected open fun onDecorationTouch(key: DecorationKey, isPressed: Boolean) {
        if (isPressed) {
            // 处理按下的状态，分为2个情况
            if (decorationKey != key) {
                // 如果按下一个别的键，我们就直接更新
                updateDecoration(key)
            } else {
                // 如果按下一个已经有的键
                if (KeyboardConfig.stickyMode) {
                    // 在粘滞键模式下，我们切换为未选中
                    updateDecoration(DecorationKey.Empty)
                }
            }
        } else {
            if (decorationKey != key) {
                // 如果抬起的key不是当前的Key，那么就不管
            } else {
                // 如果抬起的key是当前的key，那么我们检查是否需要处理粘滞键
                if (!KeyboardConfig.stickyMode) {
                    updateDecoration(DecorationKey.Empty)
                }
            }
        }
    }

}