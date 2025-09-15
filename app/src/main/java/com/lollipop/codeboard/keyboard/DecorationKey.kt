package com.lollipop.codeboard.keyboard

import android.view.KeyEvent

enum class DecorationKey(val keyCode: Int) {
    Shift(keyCode = KeyEvent.KEYCODE_SHIFT_LEFT),
    Command(keyCode = KeyEvent.KEYCODE_CTRL_LEFT),
    Option(keyCode = KeyEvent.KEYCODE_ALT_LEFT),
    Empty(keyCode = 0)
}