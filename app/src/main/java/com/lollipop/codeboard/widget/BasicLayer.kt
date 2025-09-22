package com.lollipop.codeboard.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputConnection
import com.lollipop.codeboard.KeyboardConfig
import com.lollipop.codeboard.protocol.InputLayer
import com.lollipop.codeboard.protocol.LayerOwner

abstract class BasicLayer : InputLayer {

    private var layerView: View? = null
    private var context: Context? = null
    private var owner: LayerOwner? = null

    protected val layerHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    protected val keyBuffer by lazy {
        KeyBuffer(::inputConnection)
    }

    override fun createView(
        context: Context,
        owner: LayerOwner
    ): View {
        this.context = context
        this.owner = owner
        val newView = createView(context)
        layerView = newView
        onViewCreated(newView)
        return newView
    }

    abstract fun createView(context: Context): View

    open fun onViewCreated(view: View) {
    }

    override fun onShow() {
        showByAlpha()
    }

    override fun onHide() {
        hideByAlpha()
    }

    override fun nextLayer(tag: String) {
        owner?.nextLayer(tag)
    }

    protected fun optContext(block: (Context) -> Unit) {
        context?.let(block)
    }

    protected fun input(block: (InputConnection) -> Unit) {
        inputConnection()?.let(block)
    }

    protected fun inputConnection(): InputConnection? {
        return owner?.getProvider()?.getConnection()
    }

    protected fun showByAlpha(duration: Long = 300) {
        layerView?.let {
            it.alpha = 0f
            it.visibility = View.VISIBLE
            it.animate().cancel()
            it.animate().alpha(1f).setDuration(duration)
        }
    }

    protected fun hideByAlpha(duration: Long = 300) {
        layerView?.let {
            it.animate()
                .alpha(0f)
                .setDuration(duration)
                .withEndAction {
                    layerView?.visibility = View.GONE
                }
        }
    }

    protected fun postText(text: String) {
        keyBuffer.postNext(layerHandler)
        keyBuffer.push(text)
    }

    protected fun postText(text: Char) {
        keyBuffer.postNext(layerHandler)
        keyBuffer.push(text)
    }

    protected fun postDelete(length: Int = 1) {
        keyBuffer.postNext(layerHandler)
        keyBuffer.delete(length)
    }


    protected class KeyBuffer(
        private val inputProvider: () -> InputConnection?
    ) : Runnable {

        private val buffer = StringBuilder()
        private var deleteCount = 0

        private var lasterFlushTime = 0L

        private fun now(): Long {
            return System.currentTimeMillis()
        }

        fun postNext(handler: Handler) {
            if (now() - lasterFlushTime < KeyboardConfig.INPUT_BUFFER_DELAY) {
                return
            }
            handler.postDelayed(this, KeyboardConfig.INPUT_BUFFER_DELAY)
        }

        fun push(key: String) {
            buffer.append(key)
        }

        fun push(key: Char) {
            buffer.append(key)
        }

        fun delete(length: Int) {
            deleteCount += length
        }

        override fun run() {
            lasterFlushTime = now()
            val deleteLength = deleteCount
            if (deleteLength > 0) {
                val bufferLength = buffer.length
                if (bufferLength > deleteLength) {
                    inputProvider()?.commitText(buffer.substring(0, bufferLength - deleteLength), 1)
                } else if (deleteLength == bufferLength) {
                    // 删除了全部，就什么也不做
                } else {
                    inputProvider()?.deleteSurroundingText(deleteLength - bufferLength, 0)
                }
            }
            deleteCount = 0
            buffer.clear()
        }

    }

}