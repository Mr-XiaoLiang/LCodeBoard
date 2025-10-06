package com.lollipop.codeboard.view.key

import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.tools.registerLog
import com.lollipop.codeboard.ui.KeyboardTheme
import com.lollipop.codeboard.view.KeyboardView.KeyHolder

abstract class KeyHolderWrapper : KeyHolder {

    protected var decorationKey: DecorationKey = DecorationKey.Empty
    protected var isSticky = false

    protected var theme: KeyboardTheme? = null

    protected val childList = mutableListOf<KeyHolder>()

    protected val log = registerLog()

    protected fun addKey(child: KeyHolder) {
        childList.add(child)
    }

    protected fun removeKey(child: KeyHolder) {
        childList.remove(child)
    }

    protected fun removeAllKeys() {
        childList.clear()
    }

    protected fun dispatch(block: (KeyHolder) -> Unit) {
        for (child in childList) {
            try {
                block(child)
            } catch (e: Exception) {
                log("dispatch", e)
            }
        }
    }

    override fun onSizeChanged(
        panelWidth: Int,
        panelHeight: Int,
        rowWidth: Int,
        rowHeight: Int,
        keyWidth: Int,
        keyHeight: Int
    ) {
        dispatchOnSizeChanged(
            panelWidth = panelWidth,
            panelHeight = panelHeight,
            rowWidth = rowWidth,
            rowHeight = rowHeight,
            keyWidth = keyWidth,
            keyHeight = keyHeight
        )
    }

    override fun updateTheme(theme: KeyboardTheme) {
        this.theme = theme
        dispatchUpdateTheme(theme)
    }

    override fun onDecorationKeyChanged(key: DecorationKey, isSticky: Boolean) {
        this.decorationKey = key
        this.isSticky = isSticky
        dispatchOnDecorationKeyChanged(key, isSticky)
    }

    protected open fun dispatchOnSizeChanged(
        panelWidth: Int,
        panelHeight: Int,
        rowWidth: Int,
        rowHeight: Int,
        keyWidth: Int,
        keyHeight: Int
    ) {
        dispatch {
            it.onSizeChanged(
                panelWidth = panelWidth,
                panelHeight = panelHeight,
                rowWidth = rowWidth,
                rowHeight = rowHeight,
                keyWidth = keyWidth,
                keyHeight = keyHeight
            )
        }
    }

    protected open fun dispatchUpdateTheme(theme: KeyboardTheme) {
        dispatch {
            it.updateTheme(theme)
        }
    }

    protected open fun dispatchOnDecorationKeyChanged(key: DecorationKey, isSticky: Boolean) {
        dispatch {
            it.onDecorationKeyChanged(key, isSticky)
        }
    }
}