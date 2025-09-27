package com.lollipop.codeboard.ui

import android.content.res.ColorStateList
import androidx.annotation.ColorInt

class KeyboardTheme(
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
