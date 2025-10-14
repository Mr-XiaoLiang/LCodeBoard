package com.lollipop.codeboard.view.alternative

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lollipop.codeboard.protocol.AlternativeAdapter
import com.lollipop.codeboard.protocol.AlternativeAdapterCallback
import com.lollipop.codeboard.protocol.AlternativeHolder
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.ui.AlternativeTheme
import com.lollipop.codeboard.ui.Skin

abstract class BasicAlternativeAdapter(
    val clickListener: OnAlternativeClickListener
) : AlternativeAdapter, BasicAlternativeHolder.OnCandidateClickListener {

    protected val dataList = mutableListOf<Candidate>()

    protected var theme: AlternativeTheme = Skin.current.alternative

    protected var adapterCallback: AlternativeAdapterCallback? = null

    private var layoutInflater: LayoutInflater? = null

    override val itemCount: Int
        get() {
            return dataList.size
        }

    protected fun optLayoutInflater(context: Context): LayoutInflater {
        return layoutInflater ?: LayoutInflater.from(context).also {
            layoutInflater = it
        }
    }


    override fun setCallback(callback: AlternativeAdapterCallback?) {
        this.adapterCallback = callback
    }

    fun notifyDataSetChanged() {
        sortData()
        adapterCallback?.onAlternativeDataChanged()
    }

    protected fun sortData() {
        if (dataList.size < 2) {
            return
        }
        dataList.sortWith { o1, o2 ->
            o2.level - o1.level
        }
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Candidate>) {
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun add(data: Candidate) {
        this.dataList.add(data)
        notifyDataSetChanged()
    }

    fun reset(dataList: List<Candidate>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun createView(parent: ViewGroup, viewType: Int): AlternativeHolder {
        val holder = onCreateHolder(parent, viewType)
        holder.setClickCallback(this)
        return holder
    }

    override fun bindData(holder: AlternativeHolder, theme: AlternativeTheme, position: Int) {
        if (holder is BasicAlternativeHolder) {
            holder.bind(dataList[position], position, theme)
        }
    }

    abstract fun onCreateHolder(parent: ViewGroup, viewType: Int): BasicAlternativeHolder

    abstract fun onBindHolder(
        holder: BasicAlternativeHolder,
        theme: AlternativeTheme,
        position: Int
    )

    override fun onCandidateClick(info: Candidate) {
        clickListener.onAlternativeClick(info)
    }

    fun interface OnAlternativeClickListener {
        fun onAlternativeClick(info: Candidate)
    }

}