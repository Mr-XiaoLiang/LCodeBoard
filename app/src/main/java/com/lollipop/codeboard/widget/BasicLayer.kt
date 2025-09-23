package com.lollipop.codeboard.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import com.lollipop.codeboard.KeyboardConfig
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.Keys
import com.lollipop.codeboard.protocol.InputLayer
import com.lollipop.codeboard.protocol.LayerOwner
import com.lollipop.codeboard.tools.registerLog

abstract class BasicLayer : InputLayer {

    private var layerView: View? = null
    private var context: Context? = null
    private var owner: LayerOwner? = null

    protected val log = registerLog()

    protected val layerHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    protected val keyBuffer by lazy {
        KeyBuffer(::inputConnection)
    }

    protected val draftBuffer by lazy {
        DraftBuffer(::inputConnection)
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

    protected open fun onKeyClick(key: Keys.Key?, info: KeyInfo) {
        key ?: return
        if (key is Keys.Letter) {
            postText(key.keyValue)
        }
        if (key is Keys.Number) {
            postText(key.keyValue)
        }
        if (key is Keys.Symbol) {
            postText(key.keyValue)
        }
        if (key is Keys.Function) {
            // 不处理
        }
        if (key is Keys.Option) {
            dispatchOptionClick(key)
        }
        if (key is Keys.Decoration) {
            dispatchDecorationClick(key)
        }
    }

    protected fun dispatchOptionClick(key: Keys.Option) {
        when (key) {
            Keys.Option.Copy -> {
                // TODO()
            }

            Keys.Option.Paste -> {
                // TODO()
            }

            Keys.Option.SelectAll -> {
                // TODO()
            }

            Keys.Option.Cut -> {
                // TODO()
            }

            Keys.Option.Undo -> {
                // TODO()
            }

            Keys.Option.Redo -> {
                // TODO()
            }

            Keys.Option.Find -> {
                // TODO()
            }

            Keys.Option.Replace -> {
                // TODO()
            }

            Keys.Option.Print -> {
                // TODO()
            }

            Keys.Option.Save -> {
                // TODO()
            }

            Keys.Option.Open -> {
                // TODO()
            }

            Keys.Option.Help -> {
                // TODO()
            }

            Keys.Option.About -> {
                // TODO()
            }

            Keys.Option.Quit -> {
                // TODO()
            }

            Keys.Option.FullScreen -> {
                // TODO()
            }

            Keys.Option.Minimize -> {
                // TODO()
            }

            Keys.Option.Maximize -> {
                // TODO()
            }

            Keys.Option.Close -> {
                // TODO()
            }
        }
    }

    protected fun dispatchDecorationClick(key: Keys.Decoration) {
        when (key) {
            Keys.Decoration.Shift -> {
                // 不处理
            }

            Keys.Decoration.Command -> {
                // 不处理
            }

            Keys.Decoration.Option -> {
                // 不处理
            }

            Keys.Decoration.Backspace -> {
                postDelete()
            }

            Keys.Decoration.Enter -> {
                postText("\n")
            }

            Keys.Decoration.Space -> {
                postText(" ")
            }

            Keys.Decoration.Delete -> {
                postDelete()
            }

            Keys.Decoration.Tab -> {
                // 不处理
            }

            Keys.Decoration.Escape -> {
                // 不处理
            }

            Keys.Decoration.CapsLock -> {
                // 不处理
            }

            Keys.Decoration.ArrowUp -> {
                // 不处理
            }

            Keys.Decoration.ArrowDown -> {
                // 不处理
            }

            Keys.Decoration.ArrowLeft -> {
                // 不处理
            }

            Keys.Decoration.ArrowRight -> {
                // 不处理
            }

            Keys.Decoration.Symbol -> {
                // 不处理
            }

            Keys.Decoration.Language -> {
                // 切换语言
                optContext {
                    val systemService = it.getSystemService(Context.INPUT_METHOD_SERVICE)
                    if (systemService is InputMethodManager) {
                        systemService.showInputMethodPicker()
                    }
                }
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

    protected fun postDraft(text: String) {
        draftBuffer.postNext(layerHandler)
        draftBuffer.push(text)
    }

    protected fun postDraftDelete(length: Int = 1) {
        draftBuffer.postNext(layerHandler)
        draftBuffer.delete(length)
    }

    protected fun draftCommit() {
        draftBuffer.commit(layerHandler)
    }

    protected open class KeyBuffer(
        protected val inputProvider: () -> InputConnection?
    ) : Runnable {

        protected val buffer = StringBuilder()
        protected var deleteCount = 0

        protected var lasterFlushTime = 0L

        protected fun now(): Long {
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
                    commitText(buffer.substring(0, bufferLength - deleteLength))
                } else if (deleteLength == bufferLength) {
                    // 删除了全部，就什么也不做
                } else {
                    deleteText(deleteLength - bufferLength)
                }
            }
            deleteCount = 0
            buffer.clear()
        }

        protected open fun commitText(text: String) {
            inputProvider()?.commitText(text, 1)
        }

        protected open fun deleteText(length: Int) {
            inputProvider()?.deleteSurroundingText(length, 0)
        }

    }

    protected open class DraftBuffer(
        inputProvider: () -> InputConnection?
    ) : KeyBuffer(inputProvider) {

        protected var pendingText = ""

        protected val commitTask = Runnable { commitPending() }

        override fun commitText(text: String) {
            pendingText = text
            inputProvider()?.setComposingText(text, 1)
        }

        private fun commitPending() {
            if (pendingText.isEmpty()) {
                return
            }
            inputProvider()?.commitText(pendingText, 1)
        }

        fun commit(handler: Handler) {
            handler.post(commitTask)
        }

    }

}