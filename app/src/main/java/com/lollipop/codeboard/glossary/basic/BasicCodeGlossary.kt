package com.lollipop.codeboard.glossary.basic

import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.protocol.Glossary
import com.lollipop.codeboard.protocol.GlossaryCandidate
import com.lollipop.codeboard.tools.registerLog

abstract class BasicCodeGlossary : Glossary {

    protected val log = registerLog()

    private val candidates = mutableListOf<GlossaryCandidate>()

    override fun register(callback: GlossaryCandidate) {
        candidates.add(callback)
    }

    override fun unregister(callback: GlossaryCandidate) {
        candidates.remove(callback)
    }

    protected fun notifyCandidateUpdate(candidates: List<Candidate>) {
        for (callback in this.candidates) {
            try {
                callback.onCandidateUpdate(candidates)
            } catch (e: Throwable) {
                log("notifyCandidateUpdate", e)
            }
        }
    }

}