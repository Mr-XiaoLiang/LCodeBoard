package com.lollipop.codeboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.codeboard.protocol.AlternativeAdapter
import com.lollipop.codeboard.protocol.AlternativeHolder
import com.lollipop.codeboard.ui.AlternativeTheme
import com.lollipop.codeboard.ui.Skin

private class AlternativeAdapterHelper {

    val liteAdapter = AlternativeAdapterWrapper()
    val fullAdapter = AlternativeAdapterWrapper()

    fun setAdapter(adapter: AlternativeAdapter?) {
        liteAdapter.setAdapter(adapter)
        fullAdapter.setAdapter(adapter)
    }

}

private class AlternativeAdapterWrapper() : RecyclerView.Adapter<AlternativeHolderWrapper>() {

    private val holderTypeMap = HashMap<Class<AlternativeHolder>, Int>()

    var theme: AlternativeTheme = Skin.current.alternative
        private set

    var adapter: AlternativeAdapter? = null
        private set

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(adapter: AlternativeAdapter?) {
        this.adapter = adapter
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTheme(theme: AlternativeTheme) {
        this.theme = theme
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlternativeHolderWrapper {
        return AlternativeHolderWrapper(createHolder(parent, viewType))
    }

    private fun createHolder(parent: ViewGroup, viewType: Int): AlternativeHolder {
        return adapter?.createView(parent, viewType) ?: EmptyAlternativeHolder(parent.context)
    }

    override fun onBindViewHolder(
        holder: AlternativeHolderWrapper,
        position: Int
    ) {
        adapter?.bindData(holder.impl, theme, position)
    }

    override fun getItemCount(): Int {
        return adapter?.itemCount ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    private fun getItemType(position: Int): Int {
        return adapter?.getItemType(position) ?: 0
    }

}

private class EmptyAlternativeHolder(context: Context) : AlternativeHolder {

    val emptyView = Space(context).apply {
        layoutParams = ViewGroup.LayoutParams(0, 0)
    }

    override val view: View
        get() {
            return emptyView
        }
}

private class AlternativeHolderWrapper(
    val impl: AlternativeHolder
) : RecyclerView.ViewHolder(impl.view)