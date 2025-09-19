package com.lollipop.codeboard

import com.lollipop.codeboard.protocol.InputLayer
import com.lollipop.codeboard.protocol.InputLayerManager

object IMLayerStore : InputLayerManager.LayerProvider {

    const val LAYER_QWERTY_EN = "LCB_QWERTY_EN"
    const val LAYER_QWERTY_CN = "LCB_QWERTY_CN"
    const val LAYER_SYMBOL_EN = "LCB_SYMBOL_EN"
    const val LAYER_NUMBER_9 = "LCB_NUMBER_9"

    private val layerClassList = HashMap<String, Class<out InputLayer>>()

    override fun getLayerClass(tag: String): Class<out InputLayer>? {
        return layerClassList[tag]
    }

    fun register(tag: String, clazz: Class<out InputLayer>) {
        layerClassList[tag] = clazz
    }

}