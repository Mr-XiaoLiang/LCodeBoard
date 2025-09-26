package com.lollipop.codeboard.ui.skin

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.lollipop.codeboard.tools.onUI
import java.util.concurrent.CopyOnWriteArrayList

interface SkinLifecycleOwner {

    val isDestroy: Boolean
    val isActive: Boolean

    fun register(lifecycle: SkinLifecycle)

    fun unregister(listener: SkinLifecycle)

}

abstract class BaseSkinLifecycleOwner : SkinLifecycleOwner, SkinLifecycle {

    override var isDestroy: Boolean = false
        protected set
    override var isActive: Boolean = false
        protected set

    private val listeners = CopyOnWriteArrayList<SkinLifecycle>()

    override fun register(lifecycle: SkinLifecycle) {
        if (isDestroy) {
            lifecycle.onDestroy(this)
            return
        }
        if (isActive) {
            onUI {
                lifecycle.onResume(this)
            }
        }
        listeners.add(lifecycle)
    }

    override fun unregister(listener: SkinLifecycle) {
        listeners.remove(listener)
    }

    private fun invoke(block: (SkinLifecycle) -> Unit) {
        listeners.forEach {
            onUI { block(it) }
        }
    }

    override fun onResume(owner: SkinLifecycleOwner) {
        resume()
    }

    protected fun resume() {
        isActive = true
        invoke { it.onResume(this) }
    }

    override fun onPause(owner: SkinLifecycleOwner) {
        pause()
    }

    protected fun pause() {
        isActive = false
        invoke { it.onPause(this) }
    }

    override fun onDestroy(owner: SkinLifecycleOwner) {
        destroy()
    }

    protected fun destroy() {
        isDestroy = true
        invoke { it.onDestroy(this) }
        listeners.clear()
    }

}

class SkinLifecycleOwnerByView(
    private val view: View
) : BaseSkinLifecycleOwner() {

    private val lifecycleListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            resume()
        }

        override fun onViewDetachedFromWindow(v: View) {
            pause()
        }
    }

    init {
        view.addOnAttachStateChangeListener(lifecycleListener)
        isDestroy = false
        if (view.isAttachedToWindow) {
            resume()
        }
    }

}

class SkinLifecycleOwnerByPage(
    private val lifecycleOwner: LifecycleOwner
) : BaseSkinLifecycleOwner(), LifecycleEventObserver {

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                lifecycleOwner.lifecycle.removeObserver(this)
                destroy()

            }

            Lifecycle.Event.ON_RESUME -> {
                resume()
            }

            Lifecycle.Event.ON_PAUSE -> {
                pause()
            }

            else -> {
            }
        }
    }

    init {
        initState()
    }

    private fun initState() {
        val currentState = lifecycleOwner.lifecycle.currentState
        if (currentState == Lifecycle.State.DESTROYED) {
            destroy()
            return
        }
        lifecycleOwner.lifecycle.addObserver(this)
        if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            resume()
        }
    }
}

fun LifecycleOwner.toSkinOwner(): SkinLifecycleOwner {
    return SkinLifecycleOwnerByPage(this)
}

fun View.createSkinOwner(): SkinLifecycleOwner {
    return SkinLifecycleOwnerByView(this)
}
