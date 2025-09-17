package com.lollipop.codeboard.widget

import android.content.Context
import android.view.View
import com.lollipop.codeboard.protocol.InputLayer
import com.lollipop.codeboard.protocol.LayerOwner

abstract class BasicLayer : InputLayer {

    private var layerView: View? = null
    private var context: Context? = null
    private var owner: LayerOwner? = null

    override fun createView(
        context: Context,
        owner: LayerOwner
    ): View {
        this.context = context
        this.owner = owner
        val newView = createView(context)
        layerView = newView
        onViewCreated(newView)
        return newView
    }

    abstract fun createView(context: Context): View

    open fun onViewCreated(view: View) {
    }

    override fun onShow() {
        showByAlpha(300)
    }

    override fun onHide() {
        hideByAlpha(300)
    }

    override fun nextLayer(tag: String) {
        owner?.nextLayer(tag)
    }

    protected fun showByAlpha(duration: Long) {
        layerView?.let {
            it.alpha = 0f
            it.visibility = View.VISIBLE
            it.animate().cancel()
            it.animate().alpha(1f).setDuration(duration)
        }
    }

    protected fun hideByAlpha(duration: Long) {
        layerView?.let {
            it.animate()
                .alpha(0f)
                .setDuration(duration)
                .withEndAction {
                    layerView?.visibility = View.GONE
                }
        }
    }

}