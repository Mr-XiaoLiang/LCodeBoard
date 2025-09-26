package com.lollipop.codeboard.ui.skin

interface SkinLifecycle {

    fun onResume(owner: SkinLifecycleOwner)

    fun onPause(owner: SkinLifecycleOwner)

    fun onDestroy(owner: SkinLifecycleOwner)

}
