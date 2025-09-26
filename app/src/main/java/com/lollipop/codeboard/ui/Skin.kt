package com.lollipop.codeboard.ui

import com.lollipop.codeboard.ui.skin.BasicSkinContainer
import com.lollipop.codeboard.ui.skin.SkinContainer
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
        listener: SkinObserver
    ): SkinContainer {
        val container = SkinLifecycleContainer(lifecycleOwner, null)
        container.register(listener)
        staticContainer.register(container)
        return container
    }

    fun unregister(listener: SkinObserver) {
        staticContainer.unregister(listener)
    }

    private class StaticSkinContainer : BasicSkinContainer(), SkinObserver {

        override fun onSkinChanged(skin: SkinInfo) {
            invoke { it.onSkinChanged(skin) }
        }

    }

}




