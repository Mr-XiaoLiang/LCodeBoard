package com.lollipop.codeboard.view.alternative

import android.view.View
import com.lollipop.codeboard.protocol.AlternativeHolder
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.ui.AlternativeTheme

abstract class BasicAlternativeHolder : AlternativeHolder {

    protected var clickListener: OnCandidateClickListener? = null
        private set

    protected var currentCandidate: Candidate? = null

    fun setClickCallback(listener: OnCandidateClickListener) {
        this.clickListener = listener
    }

    protected fun bindClick(view: View) {
        view.setOnClickListener {
            notifyClick()
        }
    }

    protected fun notifyClick() {
        val info = currentCandidate ?: return
        this.clickListener?.onCandidateClick(info)
    }

    fun bind(candidate: Candidate, position: Int, theme: AlternativeTheme) {
        this.currentCandidate = candidate
        onBind(candidate, position, theme)
    }

    protected abstract fun onBind(info: Candidate, position: Int, theme: AlternativeTheme)

    fun interface OnCandidateClickListener {
        fun onCandidateClick(info: Candidate)
    }

}