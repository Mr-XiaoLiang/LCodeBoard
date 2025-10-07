package com.lollipop.codeboard.view.alternative

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.ui.AlternativeTheme

abstract class BasicAlternativeHolder(view: View) : RecyclerView.ViewHolder(view) {

    protected var candidate: Candidate? = null
        private set

    protected var clickListener: OnAlternativeClickListener? = null
        private set

    fun bindClickListener(listener: OnAlternativeClickListener) {
        this.clickListener = listener
    }

    protected fun bindClick(view: View) {
        view.setOnClickListener {
            notifyClick()
        }
    }

    protected fun notifyClick() {
        candidate?.let {
            clickListener?.onAlternativeClick(it)
        }
    }

    fun bind(info: Candidate, theme: AlternativeTheme) {
        this.candidate = info
        onBind(info, theme)
    }

    abstract fun onBind(info: Candidate, theme: AlternativeTheme)

    fun interface OnAlternativeClickListener {
        fun onAlternativeClick(info: Candidate)
    }

}