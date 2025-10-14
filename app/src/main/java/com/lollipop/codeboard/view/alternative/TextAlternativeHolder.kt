package com.lollipop.codeboard.view.alternative

import android.view.View
import androidx.core.view.isVisible
import com.lollipop.codeboard.databinding.ItemAlternativeBasicBinding
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.ui.AlternativeTheme

open class TextAlternativeHolder(
    protected val binding: ItemAlternativeBasicBinding
) : BasicAlternativeHolder() {

    override val view: View
        get() {
            return binding.root
        }

    init {
        bindClick(binding.root)
    }

    override fun onBind(
        info: Candidate,
        position: Int,
        theme: AlternativeTheme
    ) {
        val type = info.type
        val hasIcon = type.icon != 0
        binding.typeIconView.isVisible = hasIcon
        binding.root.setBackgroundColor(theme.backgroundColor)
        if (hasIcon) {
            binding.typeIconView.setImageResource(type.icon)
            binding.typeIconView.imageTintList = theme.iconTint
        }
        binding.contentView.text = info.text
        binding.contentView.setTextColor(theme.contentColor)
    }

}