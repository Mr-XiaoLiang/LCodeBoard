package com.lollipop.codeboard.view.alternative

import android.view.ViewGroup
import com.lollipop.codeboard.databinding.ItemAlternativeBasicBinding
import com.lollipop.codeboard.protocol.Candidate

open class TextAlternativeAdapter(
    candidates: List<Candidate>,
    clickListener: BasicAlternativeHolder.OnAlternativeClickListener
) : BasicAlternativeAdapter<TextAlternativeHolder>(
    candidates,
    clickListener
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TextAlternativeHolder {
        return TextAlternativeHolder(
            ItemAlternativeBasicBinding.inflate(
                optLayoutInflater(parent.context),
                parent,
                false
            )
        )
    }
}