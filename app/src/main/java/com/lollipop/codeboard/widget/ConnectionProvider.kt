package com.lollipop.codeboard.widget

import android.view.inputmethod.InputConnection

interface ConnectionProvider {

    fun getConnection(): InputConnection?

}