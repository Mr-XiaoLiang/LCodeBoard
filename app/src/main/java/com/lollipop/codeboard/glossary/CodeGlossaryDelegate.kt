package com.lollipop.codeboard.glossary

import com.lollipop.codeboard.KeyboardConfig
import com.lollipop.codeboard.glossary.basic.BasicCodeGlossary
import com.lollipop.codeboard.protocol.ConnectionProvider
import com.lollipop.codeboard.protocol.GlossaryCandidate
import java.util.concurrent.CopyOnWriteArrayList

class CodeGlossaryDelegate(
    private val glossaryCandidate: GlossaryCandidate
) {

    private val codeGlossary = CopyOnWriteArrayList<BasicCodeGlossary>()

    fun reset() {
        codeGlossary.forEach {
            it.unregister(glossaryCandidate)
        }
        codeGlossary.clear()
    }

    fun put(codeGlossary: BasicCodeGlossary) {
        codeGlossary.register(glossaryCandidate)
        this.codeGlossary.add(codeGlossary)
    }

    fun putAll(codeGlossaries: List<BasicCodeGlossary>) {
        codeGlossaries.forEach {
            it.register(glossaryCandidate)
        }
        this.codeGlossary.addAll(codeGlossaries)
    }

    fun remove(codeGlossary: BasicCodeGlossary) {
        codeGlossary.unregister(glossaryCandidate)
        this.codeGlossary.remove(codeGlossary)
    }

    fun resetByConfig() {
        reset()
        val language = KeyboardConfig.codeLanguage
        val glossaries = CodeGlossaryFactory.filter(language)
        putAll(glossaries)
    }

    fun onDraftUpdate(draft: String, provider: ConnectionProvider) {
        codeGlossary.forEach {
            it.onDraftUpdate(draft, provider)
        }
    }

}