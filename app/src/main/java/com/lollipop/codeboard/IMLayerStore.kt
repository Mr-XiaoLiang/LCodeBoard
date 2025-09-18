package com.lollipop.codeboard

import com.lollipop.codeboard.layer.CodeQwertyEnLayer
import com.lollipop.codeboard.protocol.InputLayer
import com.lollipop.codeboard.protocol.InputLayerManager

object IMLayerStore : InputLayerManager.LayerProvider {

    const val LAYER_QWERTY_EN = "LCB_QWERTY_EN"
    const val LAYER_SYMBOL = "LCB_SYMBOL_EN"
    const val LAYER_NUMBER = "LCB_NUMBER_9"

    private val layerClassList = HashMap<String, Class<out InputLayer>>()

    init {
        register(LAYER_QWERTY_EN, CodeQwertyEnLayer::class.java)
    }

    override fun getLayerClass(tag: String): Class<out InputLayer>? {
        return layerClassList[tag]
    }

    fun register(tag: String, clazz: Class<out InputLayer>) {
        layerClassList[tag] = clazz
    }

}