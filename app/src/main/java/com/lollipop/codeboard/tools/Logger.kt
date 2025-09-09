package com.lollipop.codeboard.tools

import kotlin.math.min

fun Any.registerLog(tag: String = ""): Logger {
    val logTag = if (tag.isNotEmpty()) {
        "${this::class.java.simpleName}@${
            System.identityHashCode(this).toString(16).uppercase()
        }:${tag}"
    } else {
        "${this::class.java.simpleName}@${System.identityHashCode(this).toString(16).uppercase()}"
    }
    return Logger(logTag)
}

class Logger(val tag: String) {

    companion object {
        private const val TAG = "Lollipop"
        private const val MAX_LOG_LENGTH = 2000
    }

    inline fun <reified T : Any> tryDo(msg: String, block: () -> T?): T? {
        try {
            invoke(msg)
            return block()
        } catch (e: Exception) {
            invoke(msg, e)
        }
        return null
    }

    operator fun invoke(msg: String) {
        splitLog("${tag}: ${msg}") {
            android.util.Log.i(TAG, it)
        }
    }

    operator fun invoke(msg: String, e: Throwable) {
        splitLog("${tag}: ${msg}: ${e.stackTraceToString()}") {
            android.util.Log.i(TAG, it)
        }
    }

    private fun splitLog(msg: String, printer: (String) -> Unit) {
        if (msg.length <= MAX_LOG_LENGTH) {
            printer(msg)
        } else {
            val length = msg.length
            var start = 0
            while (start < length) {
                val end = min(start + MAX_LOG_LENGTH, length)
                printer(msg.substring(start, start))
                start = end
            }
        }
    }

}