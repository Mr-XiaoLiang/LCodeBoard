package com.lollipop.codeboard.layer

import android.annotation.SuppressLint
import com.lollipop.codeboard.glossary.CodeGlossaryDelegate
import com.lollipop.codeboard.protocol.AlternativeAdapter
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.protocol.GlossaryCandidate
import com.lollipop.codeboard.view.alternative.BasicAlternativeAdapter
import com.lollipop.codeboard.view.alternative.TextAlternativeAdapter

class CodeQwertyEnLayer : BasicQwertyLayer(), GlossaryCandidate,
    BasicAlternativeAdapter.OnAlternativeClickListener {

    private val codeGlossaryDelegate = CodeGlossaryDelegate(this)

    private val codeGlossaryAdapter by lazy {
        TextAlternativeAdapter(this)
    }

    override fun onDraftBufferChanged(value: String) {
        super.onDraftBufferChanged(value)
        codeGlossaryAdapter.clear()
        val inputProvider = inputProvider() ?: return
        codeGlossaryDelegate.onDraftUpdate(value, inputProvider)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCandidateUpdate(candidates: List<Candidate>) {
        codeGlossaryAdapter.addAll(candidates)
    }

    override fun onShow() {
        super.onShow()
        codeGlossaryDelegate.resetByConfig()
    }

    override fun getAlternativeAdapter(): AlternativeAdapter? {
        return codeGlossaryAdapter
    }

    override fun onAlternativeClick(info: Candidate) {
        commitAndClearDraft(info.text)
    }

}