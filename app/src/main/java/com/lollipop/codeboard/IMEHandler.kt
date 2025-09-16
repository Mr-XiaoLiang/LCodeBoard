package com.lollipop.codeboard

import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.Keys

interface KeyHandler {

    fun onClick(key: Keys.Key)

    fun onKeyPress(key: DecorationKey)

    fun onKeyRelease(key: DecorationKey)

    fun onKeyPress(key: Keys.Key)

    fun onKeyRelease(key: Keys.Key)

}

interface KeyReceiver : KeyHandler {

    override fun onKeyPress(key: Keys.Key) {}

    override fun onKeyRelease(key: Keys.Key) {}

}

class IMEHandler {

    val keyHandler = object : KeyHandler {
        override fun onClick(key: Keys.Key) {
            dispatchKeyClick(key)
        }

        override fun onKeyPress(key: DecorationKey) {
            dispatchKeyPress(key)
        }

        override fun onKeyRelease(key: DecorationKey) {
            dispatchKeyRelease(key)
        }

        override fun onKeyPress(key: Keys.Key) {
            dispatchKeyPress(key)
        }

        override fun onKeyRelease(key: Keys.Key) {
            dispatchKeyRelease(key)
        }
    }

    var keyReceiver: KeyReceiver? = null
        private set

    private val receiverCache = ReceiverCache()

    private fun dispatchKeyClick(key: Keys.Key) {
        keyReceiver?.onClick(key)
    }

    private fun dispatchKeyPress(key: DecorationKey) {
        keyReceiver?.onKeyPress(key)
    }

    private fun dispatchKeyRelease(key: DecorationKey) {
        keyReceiver?.onKeyRelease(key)
    }

    private fun dispatchKeyPress(key: Keys.Key) {
        keyReceiver?.onKeyPress(key)
    }

    private fun dispatchKeyRelease(key: Keys.Key) {
        keyReceiver?.onKeyRelease(key)
    }

    fun changeKeyReceiver(receiver: KeyReceiver?) {
        keyReceiver = receiver
    }

    fun bindKeyReceiver(key: String, receiver: KeyReceiver) {
        receiverCache.bindKeyReceiver(key, receiver)
    }

    fun removeKeyReceiver(key: String) {
        receiverCache.removeKeyReceiver(key)
    }

    fun switchKeyReceiver(key: String) {
        val receiver = receiverCache.getKeyReceiver(key)
        if (receiver != null) {
            keyReceiver = receiver
        }
    }

    private class ReceiverCache {

        private val cacheMap = mutableMapOf<String, KeyReceiver>()

        fun bindKeyReceiver(key: String, receiver: KeyReceiver) {
            cacheMap[key] = receiver
        }

        fun getKeyReceiver(key: String): KeyReceiver? {
            return cacheMap[key]
        }

        fun removeKeyReceiver(key: String) {
            cacheMap.remove(key)
        }

    }

}
