package com.lollipop.codeboard.tools

import android.content.Context
import com.lollipop.codeboard.protocol.Preloadable
import java.util.LinkedList

object PreloadManager {

    private val preloadList = LinkedList<Preloadable>()
    private var contextProvider: ContextProvider? = null

    fun preload(provider: ContextProvider) {
        this.contextProvider = provider
        invokePreload(provider)
    }

    fun register(vararg preloadable: Preloadable) {
        if (preloadable.isNotEmpty()) {
            preloadList.addAll(preloadable)
        }
        contextProvider?.let {
            invokePreload(it)
        }
    }

    private fun invokePreload(provider: ContextProvider) {
        while (true) {
            val preloadable = preloadList.poll() ?: break
            // 抛到下一个调用栈
            onUI {
                preloadable.preload(provider.getContext())
            }
        }
    }

    interface ContextProvider {
        fun getContext(): Context
    }

}