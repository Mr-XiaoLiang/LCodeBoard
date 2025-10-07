package com.lollipop.codeboard.view.alternative

import androidx.core.view.isVisible
import com.lollipop.codeboard.databinding.ItemAlternativeBasicBinding
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.ui.AlternativeTheme

open class TextAlternativeHolder(
    protected val binding: ItemAlternativeBasicBinding
) : BasicAlternativeHolder(binding.root) {

    init {
        bindClick(binding.root)
    }

    override fun onBind(info: Candidate, theme: AlternativeTheme) {
        val type = info.type
        val hasIcon = type.icon != 0
        binding.typeIconView.isVisible = hasIcon
        if (hasIcon) {
            binding.typeIconView.setImageResource(type.icon)
        }
        binding.contentView.text = info.text
    }

}