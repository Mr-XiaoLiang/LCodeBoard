package com.lollipop.codeboard.tools

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

object TaskHelper {

    val logger by lazy {
        registerLog()
    }

    private val mainThread by lazy {
        Handler(Looper.getMainLooper())
    }

    private val asyncThreadPool by lazy {
        Executors.newCachedThreadPool()
    }

    private val ioThreadPool by lazy {
        Executors.newCachedThreadPool()
    }

    abstract class SafeRunnable(
        private val log: Logger,
        private val taskName: String
    ) : Runnable {

        override fun run() {
            try {
                safeRun()
            } catch (e: Throwable) {
                log("$taskName Error", e)
            }
        }

        fun delay(delay: Long) {
            postMainDelay(delay, this)
        }

        fun onMain() {
            postMain(this)
        }

        fun onIO() {
            postIO(this)
        }

        fun onAsync() {
            postAsync(this)
        }

        fun cancel() {
            removeMain(this)
        }

        abstract fun safeRun()

    }

    open class SafeTask(
        log: Logger,
        taskName: String = "SafeTask",
        private val block: () -> Unit
    ) : SafeRunnable(log, taskName) {
        override fun safeRun() {
            block()
        }
    }

    fun postMain(runnable: Runnable) {
        mainThread.post(runnable)
    }

    fun postMainDelay(delay: Long, runnable: Runnable) {
        mainThread.postDelayed(runnable, delay)
    }

    fun removeMain(runnable: Runnable) {
        mainThread.removeCallbacks(runnable)
    }

    fun postAsync(runnable: Runnable) {
        asyncThreadPool.execute(runnable)
    }

    fun postIO(runnable: Runnable) {
        ioThreadPool.execute(runnable)
    }

}

fun onUI(block: Runnable) {
    TaskHelper.postMain(block)
}

fun doAsync(block: Runnable) {
    TaskHelper.postAsync(block)
}

fun doIO(block: Runnable) {
    TaskHelper.postIO(block)
}

fun postDelay(delay: Long, block: Runnable) {
    TaskHelper.postMainDelay(delay, block)
}

fun onUISafe(log: Logger = TaskHelper.logger, name: String = "onUISafe", block: () -> Unit) {
    TaskHelper.postMain(TaskHelper.SafeTask(log = log, taskName = name, block = block))
}

fun doAsyncSafe(log: Logger = TaskHelper.logger, name: String = "doAsyncSafe", block: () -> Unit) {
    TaskHelper.postAsync(TaskHelper.SafeTask(log = log, taskName = name, block = block))
}

fun doIOSafe(log: Logger = TaskHelper.logger, name: String = "doIOSafe", block: () -> Unit) {
    TaskHelper.postIO(TaskHelper.SafeTask(log = log, taskName = name, block = block))
}

fun postDelaySafe(
    log: Logger = TaskHelper.logger,
    name: String = "postDelaySafe",
    delay: Long,
    block: () -> Unit
) {
    TaskHelper.postMainDelay(delay, TaskHelper.SafeTask(log = log, taskName = name, block = block))
}
