package com.lollipop.codeboard.glossary.basic

import android.view.inputmethod.InputConnection
import com.lollipop.codeboard.KeyboardConfig
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.protocol.ConnectionProvider
import com.lollipop.codeboard.protocol.Glossary
import com.lollipop.codeboard.protocol.GlossaryCandidate
import com.lollipop.codeboard.tools.Logger
import com.lollipop.codeboard.tools.TaskHelper
import com.lollipop.codeboard.tools.onUISafe
import com.lollipop.codeboard.tools.registerLog

abstract class BasicGlossary : Glossary {

    protected val log = registerLog()

    protected var connectionProvider: ConnectionProvider? = null

    private val candidates = mutableListOf<GlossaryCandidate>()

    protected var draftMode = 0
        private set
    protected var draftValue = ""

    private val loadTask by lazy {
        TaskHelper.SafeTask(log, "GlossaryBackpressureTask") {
            postInvoke()
        }
    }

    protected val connectionProviderWrapper = object : ConnectionProvider {
        override fun getConnection(): InputConnection? {
            return optConnection()
        }
    }

    override fun onDraftUpdate(draft: String, provider: ConnectionProvider) {
        this.connectionProvider = provider
        this.draftValue = draft
        loadTask.cancel()
        loadTask.delay(KeyboardConfig.GLOSSARY_BACKPRESSURE_DELAY)
    }

    override fun register(callback: GlossaryCandidate) {
        candidates.add(callback)
    }

    override fun unregister(callback: GlossaryCandidate) {
        candidates.remove(callback)
    }

    protected fun optConnection(): InputConnection? {
        return connectionProvider?.getConnection()
    }

    protected fun postInvoke() {
        val draft = draftValue
        if (draft.isNotEmpty()) {
            val payload = LoadPayload(
                log = log,
                mode = nextMode(),
                draft = draftValue,
                connectionProvider = connectionProviderWrapper
            )
            onDraftUpdate(payload)
        }
    }

    protected abstract fun onDraftUpdate(payload: LoadPayload)

    protected fun asyncLoad(payload: LoadPayload, loadBlock: (LoadPayload) -> List<Candidate>) {
        AsyncLoadTask(payload = payload, glossary = this, loadBlock = loadBlock).onAsync()
    }

    protected fun notifyCandidateUpdate(mode: Int, candidates: List<Candidate>) {
        if (isCurrentMode(mode)) {
            onUISafe(log, "notifyCandidateUpdate") {
                for (callback in this.candidates) {
                    try {
                        callback.onCandidateUpdate(candidates)
                    } catch (e: Throwable) {
                        log("notifyCandidateUpdate", e)
                    }
                }
            }
        } else {
            log("notifyCandidateUpdate: currentMode = ${draftMode}, dataMode = $mode")
        }
    }

    protected fun isCurrentMode(mode: Int): Boolean {
        return draftMode == mode
    }

    protected fun nextMode(): Int {
        var modeNext = draftMode + 1
        if (modeNext == Int.MAX_VALUE) {
            modeNext = Int.MIN_VALUE
        }
        draftMode = modeNext
        return modeNext
    }

    class LoadPayload(
        val log: Logger,
        val mode: Int,
        val draft: String,
        val connectionProvider: ConnectionProvider
    )

    protected class AsyncLoadTask(
        private val payload: LoadPayload,
        private val glossary: BasicGlossary,
        private val loadBlock: (LoadPayload) -> List<Candidate>
    ) : TaskHelper.SafeRunnable(payload.log, "AsyncLoadTask") {
        override fun safeRun() {
            val candidates = loadBlock(payload)
            glossary.notifyCandidateUpdate(payload.mode, candidates)
        }
    }

}