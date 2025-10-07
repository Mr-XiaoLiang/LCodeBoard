package com.lollipop.codeboard.ui

import android.content.res.ColorStateList

class AlternativeTheme(
    val backgroundColor: Int,
    val contentColor: Int,
    val typeIconColor: Int
) {

    val iconTint by lazy {
        ColorStateList.valueOf(typeIconColor)
    }

}