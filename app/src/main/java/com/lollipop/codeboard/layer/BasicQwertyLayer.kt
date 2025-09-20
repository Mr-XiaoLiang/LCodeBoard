package com.lollipop.codeboard.layer

import android.content.Context
import android.view.View
import com.lollipop.codeboard.widget.BasicLayer

/**
 * 负责Qwerty键盘的UI，提供基础的UI交互逻辑
 */
abstract class BasicQwertyLayer: BasicLayer() {

    override fun createView(context: Context): View {
        TODO("Not yet implemented")
    }

    override fun onInsetsChange(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        TODO("Not yet implemented")
    }

}