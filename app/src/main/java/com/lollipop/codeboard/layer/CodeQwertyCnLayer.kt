package com.lollipop.codeboard.layer

import com.lollipop.codeboard.glossary.CodeGlossaryDelegate
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.protocol.GlossaryCandidate

class CodeQwertyCnLayer : BasicQwertyLayer(), GlossaryCandidate {

    private val codeGlossaryDelegate = CodeGlossaryDelegate(this)

    override fun onCandidateUpdate(candidates: List<Candidate>) {
        // TODO 事件触发和事件接收需要处理
    }

    override fun onShow() {
        super.onShow()
        codeGlossaryDelegate.resetByConfig()
    }

}