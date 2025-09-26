package com.lollipop.codeboard.ui.skin

import com.lollipop.codeboard.tools.onUI
import com.lollipop.codeboard.ui.SkinInfo
import java.util.concurrent.CopyOnWriteArrayList

interface SkinContainer {
    fun register(listener: SkinObserver)
    fun unregister(listener: SkinObserver)
}

class SkinContainerDelegate : SkinContainer {

    private val listeners = CopyOnWriteArrayList<SkinObserver>()

    override fun register(listener: SkinObserver) {
        listeners.add(listener)
    }

    override fun unregister(listener: SkinObserver) {
        listeners.remove(listener)
    }

    fun invoke(block: (SkinObserver) -> Unit) {
        listeners.forEach {
            onUI { block(it) }
        }
    }

    fun invokeSync(block: (SkinObserver) -> Unit) {
        listeners.forEach {
            block(it)
        }
    }

    fun clear() {
        listeners.clear()
    }

}

abstract class BasicSkinLifecycleContainer : SkinLifecycleObserver(), SkinContainer {

    private val containerDelegate = SkinContainerDelegate()

    override fun register(listener: SkinObserver) {
        containerDelegate.register(listener)
    }

    override fun unregister(listener: SkinObserver) {
        containerDelegate.unregister(listener)
    }

    protected fun invoke(block: (SkinObserver) -> Unit) {
        containerDelegate.invoke(block)
    }

    protected fun invokeSync(block: (SkinObserver) -> Unit) {
        containerDelegate.invokeSync(block)
    }

    protected fun clear() {
        containerDelegate.clear()
    }

}

class SkinLifecycleContainer() : BasicSkinLifecycleContainer(), SkinObserver, SkinLifecycle {

    override fun notifySkinChanged(skin: SkinInfo) {
        invoke { it.onSkinChanged(skin) }
    }

    override fun onDestroy(owner: SkinLifecycleOwner) {
        super.onDestroy(owner)
        clear()
    }
}



