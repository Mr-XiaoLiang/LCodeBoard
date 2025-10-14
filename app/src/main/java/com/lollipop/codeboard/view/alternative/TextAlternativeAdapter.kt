package com.lollipop.codeboard.view.alternative

import android.view.ViewGroup
import com.lollipop.codeboard.databinding.ItemAlternativeBasicBinding
import com.lollipop.codeboard.ui.AlternativeTheme

open class TextAlternativeAdapter(
    clickListener: OnAlternativeClickListener
) : BasicAlternativeAdapter(clickListener) {

    override fun onCreateHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasicAlternativeHolder {
        return TextAlternativeHolder(
            ItemAlternativeBasicBinding.inflate(
                optLayoutInflater(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindHolder(
        holder: BasicAlternativeHolder,
        theme: AlternativeTheme,
        position: Int
    ) {
        val info = dataList[position]
        holder.bind(info, position, theme)
    }
}