package com.lollipop.codeboard.layer

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.codeboard.glossary.CodeGlossaryDelegate
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.protocol.GlossaryCandidate
import com.lollipop.codeboard.view.alternative.BasicAlternativeHolder
import com.lollipop.codeboard.view.alternative.TextAlternativeAdapter

class CodeQwertyEnLayer : BasicQwertyLayer(), GlossaryCandidate,
    BasicAlternativeHolder.OnAlternativeClickListener {

    private val codeGlossaryDelegate = CodeGlossaryDelegate(this)

    private val textAlternativeList = mutableListOf<Candidate>()

    private val codeGlossaryAdapter by lazy {
        TextAlternativeAdapter(textAlternativeList, this)
    }

    override fun onDraftBufferChanged(value: String) {
        super.onDraftBufferChanged(value)
        textAlternativeList.clear()
        val inputProvider = inputProvider() ?: return
        codeGlossaryDelegate.onDraftUpdate(value, inputProvider)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCandidateUpdate(candidates: List<Candidate>) {
        textAlternativeList.addAll(candidates)
        codeGlossaryAdapter.notifyDataSetChanged()
    }

    override fun onShow() {
        super.onShow()
        codeGlossaryDelegate.resetByConfig()
    }

    override fun getAlternativeAdapter(): RecyclerView.Adapter<*>? {
        return codeGlossaryAdapter
    }

    override fun onAlternativeClick(info: Candidate) {
        commitAndClearDraft(info.text)
    }


}