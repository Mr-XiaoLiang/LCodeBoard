package com.lollipop.codeboard.ui.skin

import com.lollipop.codeboard.ui.Skin
import com.lollipop.codeboard.ui.SkinInfo

fun interface SkinObserver {
    fun onSkinChanged(skin: SkinInfo)
}

abstract class SkinLifecycleObserver : SkinLifecycle, SkinObserver {

    var isDestroy: Boolean = false
        protected set

    var isActive: Boolean = false
        protected set

    protected var skinMode = 0
        private set
    protected var currentMode = 0
        private set

    protected var pendingSkin: SkinInfo? = null

    override fun onResume(owner: SkinLifecycleOwner) {
        isActive = true
        dispatchSkin()
    }

    private fun dispatchSkin() {
        if (currentMode != skinMode) {
            currentMode = skinMode
            notifySkinChanged(pendingSkin ?: Skin.current)
        }
    }

    override fun onPause(owner: SkinLifecycleOwner) {
        isActive = false
    }

    override fun onDestroy(owner: SkinLifecycleOwner) {
        isDestroy = true
    }

    protected fun onSkinUpdate() {
        skinMode++
        if (skinMode == Int.MAX_VALUE) {
            skinMode = 0
        }
    }

    override fun onSkinChanged(skin: SkinInfo) {
        if (isDestroy) {
            return
        }
        onSkinUpdate()
        pendingSkin = skin
        if (isActive) {
            dispatchSkin()
        }
    }

    abstract fun notifySkinChanged(skin: SkinInfo)

}

class SimpleSkinLifecycleObserver(
    val observer: SkinObserver
) : SkinLifecycleObserver() {
    override fun notifySkinChanged(skin: SkinInfo) {
        observer.onSkinChanged(skin)
    }
}
