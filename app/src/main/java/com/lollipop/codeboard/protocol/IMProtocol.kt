package com.lollipop.codeboard.protocol

import android.content.Context
import android.view.View
import android.view.inputmethod.CursorAnchorInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.recyclerview.widget.RecyclerView

/**
 * 输入面板的抽象
 * 它需要为UI提供一个View组件
 * 同时提供一个绑定输出的函数
 */
interface InputLayer {

    fun createView(context: Context, owner: LayerOwner): View

    fun onInsetsChange(left: Int, top: Int, right: Int, bottom: Int)

    fun getAlternativeAdapter(): RecyclerView.Adapter<*>? {
        return null
    }

    fun onShow()

    fun onHide()

    fun nextLayer(tag: String)

    fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
    }

    fun onUpdateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo?) {
    }

}

interface LayerOwner {

    fun nextLayer(tag: String)

    fun getProvider(): ConnectionProvider?

}

fun interface ConnectionProvider {

    fun getConnection(): InputConnection?

}

interface Glossary {

    fun onDraftUpdate(draft: String, provider: ConnectionProvider)

    fun register(callback: GlossaryCandidate)

    fun unregister(callback: GlossaryCandidate)

}

interface GlossaryCandidate {

    fun onCandidateUpdate(candidates: List<Candidate>)

}

class Candidate(
    val text: String,
    val level: Int,
    val type: CandidateType
)

enum class CandidateType {

    Word,
    Code,
    Symbol,
    Date,

}


