package com.lollipop.codeboard.ui

import com.lollipop.codeboard.ui.skin.SkinContainer
import com.lollipop.codeboard.ui.skin.SkinContainerDelegate
import com.lollipop.codeboard.ui.skin.SkinLifecycleContainer
import com.lollipop.codeboard.ui.skin.SkinLifecycleOwner
import com.lollipop.codeboard.ui.skin.SkinObserver

object Skin {

    private val staticContainer = StaticSkinContainer()

    var current: SkinInfo = SkinInfo.DEFAULT
        private set

    fun updateSkin(skin: SkinInfo) {
        this.current = skin
        staticContainer.onSkinChanged(skin)
    }

    fun register(
        lifecycleOwner: SkinLifecycleOwner,
    ): SkinContainer {
        val container = SkinLifecycleContainer()
        lifecycleOwner.register(container)
        staticContainer.register(container)
        return container
    }

    fun unregister(listener: SkinObserver) {
        staticContainer.unregister(listener)
    }

    private class StaticSkinContainer : SkinContainer, SkinObserver {

        private val delegate = SkinContainerDelegate()

        override fun onSkinChanged(skin: SkinInfo) {
            delegate.invoke { it.onSkinChanged(skin) }
        }

        override fun register(listener: SkinObserver) {
            delegate.register(listener)
        }

        override fun unregister(listener: SkinObserver) {
            delegate.unregister(listener)
        }

    }

}




