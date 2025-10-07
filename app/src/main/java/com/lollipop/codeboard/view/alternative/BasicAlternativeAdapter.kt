package com.lollipop.codeboard.view.alternative

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.ui.AlternativeTheme
import com.lollipop.codeboard.ui.Skin

abstract class BasicAlternativeAdapter<T : BasicAlternativeHolder>(
    val candidates: List<Candidate>,
    val clickListener: BasicAlternativeHolder.OnAlternativeClickListener
) : RecyclerView.Adapter<T>() {

    protected var theme: AlternativeTheme = Skin.current.alternative

    private var layoutInflater: LayoutInflater? = null

    protected fun optLayoutInflater(context: Context): LayoutInflater {
        return layoutInflater ?: LayoutInflater.from(context).also {
            layoutInflater = it
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onThemeUpdate(theme: AlternativeTheme) {
        this.theme = theme
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.bindClickListener(clickListener)
        val info = candidates[position]
        holder.bind(info, theme)
    }

    override fun getItemCount(): Int {
        return candidates.size
    }

}