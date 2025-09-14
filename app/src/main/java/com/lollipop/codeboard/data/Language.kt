package com.lollipop.codeboard.data

import android.content.Context
import androidx.annotation.StringRes

class Language(
    val code: String,
    val name: String,
    @StringRes
    val nameRes: Int
) {

    companion object {
        val Empty = Language("", "", 0)
    }

    fun optName(context: Context): String {
        return if (nameRes != 0) {
            context.getString(nameRes)
        } else {
            name
        }
    }

}