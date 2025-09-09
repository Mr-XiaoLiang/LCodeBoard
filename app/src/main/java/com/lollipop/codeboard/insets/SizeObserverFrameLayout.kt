package com.lollipop.codeboard.insets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class SizeObserverFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), SizeObserver {

    private val sizeObserverHelper = SizeObserverHelper()

    override fun register(callback: SizeCallback) {
        sizeObserverHelper.register(callback)
    }

    override fun unregister(callback: SizeCallback) {
        sizeObserverHelper.unregister(callback)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        sizeObserverHelper.notifySizeChanged(w, h)
    }

}