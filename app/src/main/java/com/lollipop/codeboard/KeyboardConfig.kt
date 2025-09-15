package com.lollipop.codeboard

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.lollipop.codeboard.data.Language

object KeyboardConfig {

    /**
     * 编码语言
     */
    var codeLanguage: Language = Language.Empty

    /**
     * 输入语言
     */
    var language: Language = Language.Empty

    /**
     * 使用系统字体
     */
    var useSystemFont = false

    /**
     * 模拟模式,
     * 此模式下，将会直接发送键值，而不会处理事件
     */
    var simulationMode = false

    /**
     * 粘性按键模式
     */
    var stickyMode = false

    private var keyFontCache: Typeface? = null

    fun bindKeyFont(textView: TextView) {
        if (useSystemFont) {
            textView.typeface = null
        } else {
            textView.typeface = loadKeyFont(textView.context)
        }
    }

    private fun loadKeyFont(context: Context): Typeface {
        val cache = keyFontCache
        if (cache != null) {
            return cache
        }
        val newFont = ResourcesCompat.getFont(
            context,
            R.font.roboto_variable_font_wdth_wght
        )
        keyFontCache = newFont
        return newFont!!
    }

}