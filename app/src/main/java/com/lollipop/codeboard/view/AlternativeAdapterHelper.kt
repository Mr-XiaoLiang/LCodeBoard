package com.lollipop.codeboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.codeboard.protocol.AlternativeAdapter
import com.lollipop.codeboard.protocol.AlternativeAdapterCallback
import com.lollipop.codeboard.protocol.AlternativeHolder
import com.lollipop.codeboard.ui.AlternativeTheme
import com.lollipop.codeboard.ui.Skin

class AlternativeAdapterHelper {

    private val adapterCallback = object : AlternativeAdapterCallback {
        override fun onAlternativeDataChanged() {
            notifyDataSetChanged()
        }
    }

    var callback: Callback? = null

    val liteAdapter = AlternativeAdapterWrapper()
    val fullAdapter = AlternativeAdapterWrapper()

    var current: AlternativeAdapter? = null
        private set

    fun setAdapter(adapter: AlternativeAdapter?) {
        // 清空历史的回调函数
        liteAdapter.adapter?.setCallback(null)
        fullAdapter.adapter?.setCallback(null)
        // 重新绑定回调函数
        adapter?.setCallback(adapterCallback)
        // 绑定适配器
        liteAdapter.setAdapter(adapter)
        fullAdapter.setAdapter(adapter)
    }

    fun updateTheme(theme: AlternativeTheme) {
        liteAdapter.updateTheme(theme)
        fullAdapter.updateTheme(theme)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSetChanged() {
        liteAdapter.notifyDataSetChanged()
        fullAdapter.notifyDataSetChanged()
        val itemCount = current?.itemCount ?: 0
        callback?.onAlternativeDataChanged(itemCount > 0)
    }

    interface Callback {
        fun onAlternativeDataChanged(hasData: Boolean)
    }

}

class AlternativeAdapterWrapper() : RecyclerView.Adapter<AlternativeHolderWrapper>() {

    var theme: AlternativeTheme = Skin.current.alternative
        private set

    var adapter: AlternativeAdapter? = null
        private set

    private val holderTypeManager = HolderTypeManager()

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(adapter: AlternativeAdapter?) {
        this.adapter = adapter
        holderTypeManager.changeStore(adapter)
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
        return holderTypeManager.getType(adapter?.getItemType(position) ?: 0)
    }

}

private class HolderTypeManager {

    private val emptyStore = TypeStore(0)
    private var adapterStore = HashMap<String, TypeStore>()
    private var currentStore = emptyStore

    fun changeStore(adapter: AlternativeAdapter?) {
        if (adapter == null) {
            currentStore = emptyStore
        } else {
            val storeToken = System.identityHashCode(adapter).toString(16)
            val store = adapterStore[storeToken]
            if (store == null) {
                val maskLevel = 100000
                val newStore = TypeStore((adapterStore.size + 1) * maskLevel)
                adapterStore[storeToken] = newStore
                currentStore = newStore
            } else {
                currentStore = store
            }
        }
    }

    fun getType(type: Int): Int {
        return currentStore.getTypeId(type)
    }

}

private class TypeStore(
    val mask: Int
) {

    val store = HashMap<Int, Int>()

    private var index = 0

    fun getTypeId(src: Int): Int {
        val typeId = store[src]
        if (typeId != null) {
            return typeId
        }
        index++
        val id = index or mask
        store[src] = id
        return id
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

class AlternativeHolderWrapper(
    val impl: AlternativeHolder
) : RecyclerView.ViewHolder(impl.view)