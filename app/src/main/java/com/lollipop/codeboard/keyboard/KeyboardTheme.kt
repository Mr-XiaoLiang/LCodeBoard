package com.lollipop.codeboard.keyboard

import android.content.res.ColorStateList
import androidx.annotation.ColorInt

object KeyboardTheme {

    val DEFAULT = BoardTheme(
        keyTheme = KeyTheme(
            backgroundDefault = 0xFFF5F5F5.toInt(),
            backgroundPress = 0xFFE0E0E0.toInt(),
            contentDefault = 0xFF000000.toInt(),
            contentPress = 0xFF000000.toInt(),
        ),
        decorationTheme = KeyTheme(
            backgroundDefault = 0xFFF5F5F5.toInt(),
            backgroundPress = 0xFFE0E0E0.toInt(),
            contentDefault = 0xFF000000.toInt(),
            contentPress = 0xFF000000.toInt(),
        ),
        0xFFF5F5F5.toInt(),
        ""
    )

    var theme: BoardTheme = DEFAULT
        private set

    private val listeners = mutableListOf<ThemeChangeListener>()

    fun addThemeChangeListener(listener: ThemeChangeListener) {
        listeners.add(listener)
    }

    fun removeThemeChangeListener(listener: ThemeChangeListener) {
        listeners.remove(listener)
    }

    fun updateTheme(theme: BoardTheme) {
        this.theme = theme
        listeners.forEach { it.onThemeChanged(theme) }
    }

    fun interface ThemeChangeListener {
        fun onThemeChanged(theme: BoardTheme)
    }

}

class BoardTheme(
    val keyTheme: KeyTheme,
    val decorationTheme: KeyTheme,
    @ColorInt
    val backgroundColor: Int,
    val backgroundDrawable: String
)

class KeyTheme(
    @ColorInt
    val backgroundDefault: Int,
    @ColorInt
    val backgroundPress: Int,
    @ColorInt
    val contentDefault: Int,
    @ColorInt
    val contentPress: Int,
    @ColorInt
    val stickyColor: Int
) {

    val contentStateList: ColorStateList by lazy {
        ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf()
            ),
            intArrayOf(
                contentPress,
                contentDefault
            )
        )
    }

    val backgroundStateList: ColorStateList by lazy {
        ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf()
            ),
            intArrayOf(
                backgroundPress,
                backgroundDefault
            )
        )
    }

}
