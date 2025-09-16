package com.lollipop.codeboard

import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import com.lollipop.codeboard.tools.registerLog
import com.lollipop.codeboard.view.KeyboardView

class IMEDelegate(
    private var keyHandler: KeyHandler?
) {

    private val log = registerLog()

    var keyboardView: KeyboardView? = null

//    private var decorationKey: DecorationKey = DecorationKey.Empty

    private fun input(block: (KeyHandler) -> Unit) {
        keyHandler?.also {
            block(it)
        }
    }

    fun changeKeyHandler(handler: KeyHandler?) {
        keyHandler = handler
    }

    fun onViewCreated(view: KeyboardView) {
        keyboardView = view
    }

    fun onKeyClick(key: Keys.Key?, info: KeyInfo) {
        val k = key ?: return
        if (k is Keys.Decoration) {
            return
        }
        if (!KeyboardConfig.simulationMode) {
            input {
                it.onClick(k)
            }
        }
    }

    fun onDecorationTouch(
        key: DecorationKey,
        isPressed: Boolean
    ) {
        log("onDecorationTouch: key = $key, isPressed = $isPressed")
        input {
            if (isPressed) {
                it.onKeyPress(key)
            } else {
                it.onKeyRelease(key)
            }
        }
//        if (isPressed) {
//            // 处理按下的状态，分为2个情况
//            if (decorationKey != key) {
//                // 如果按下一个别的键，我们就直接更新
//                updateDecoration(key)
//            } else {
//                // 如果按下一个已经有的键
//                if (KeyboardConfig.stickyMode) {
//                    // 在粘滞键模式下，我们切换为未选中
//                    updateDecoration(DecorationKey.Empty)
//                }
//            }
//        } else {
//            if (decorationKey != key) {
//                // 如果抬起的key不是当前的Key，那么就不管
//            } else {
//                // 如果抬起的key是当前的key，那么我们检查是否需要处理粘滞键
//                if (!KeyboardConfig.stickyMode) {
//                    updateDecoration(DecorationKey.Empty)
//                }
//            }
//        }
    }

//    private fun updateDecoration(newKey: DecorationKey) {
//        log("updateDecoration: $newKey")
//        if (newKey != decorationKey) {
//            decorationKey = newKey
//            keyboardView?.setDecorationKey(decorationKey, KeyboardConfig.stickyMode)
//        }
//    }

    fun onKeyTouch(key: Keys.Key?, info: KeyInfo, isPressed: Boolean) {
        log("onKeyTouch: $key, $info, $isPressed")
        key ?: return
        input {
            if (isPressed) {
                it.onKeyPress(key)
            } else {
                it.onKeyRelease(key)
            }
        }
//        if (KeyboardConfig.simulationMode) {
//            val action = if (isPressed) {
//                KeyEvent.ACTION_DOWN
//            } else {
//                KeyEvent.ACTION_UP
//            }
//            input {
//                it.sendKeyEvent(KeyEvent(action, key.keyCode))
//            }
//        }
    }

}