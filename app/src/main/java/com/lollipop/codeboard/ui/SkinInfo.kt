package com.lollipop.codeboard.ui

class SkinInfo(
    val keyboard: KeyboardTheme
) {

    companion object {
        val DEFAULT = SkinInfo(
            keyboard = KeyboardTheme(
                keyTheme = KeyTheme(
                    backgroundDefault = 0xFFE5E5E5.toInt(),
                    backgroundPress = 0xFFE0E0E0.toInt(),
                    contentDefault = 0xFF000000.toInt(),
                    contentPress = 0xFF000000.toInt(),
                    stickyColor = 0xFF03A9F4.toInt()
                ),
                decorationTheme = KeyTheme(
                    backgroundDefault = 0xFFE5E5E5.toInt(),
                    backgroundPress = 0xFFE0E0E0.toInt(),
                    contentDefault = 0xFF000000.toInt(),
                    contentPress = 0xFF000000.toInt(),
                    stickyColor = 0xFF03A9F4.toInt()
                ),
                backgroundColor = 0xFFF5F5F5.toInt(),
                backgroundDrawable = ""
            )
        )
    }

}