package com.lollipop.codeboard.insets

interface SizeObserver {

    fun register(callback: SizeCallback)

    fun unregister(callback: SizeCallback)

}

fun interface SizeCallback {
    fun onSizeChanged(width: Int, height: Int)
}

class SizeObserverHelper : SizeObserver {

    private val callbacks = mutableListOf<SizeCallback>()

    fun notifySizeChanged(width: Int, height: Int) {
        callbacks.forEach {
            it.onSizeChanged(width, height)
        }
    }

    override fun register(callback: SizeCallback) {
        if (callbacks.contains(callback)) {
            return
        }
        callbacks.add(callback)
    }

    override fun unregister(callback: SizeCallback) {
        callbacks.remove(callback)
    }
}

