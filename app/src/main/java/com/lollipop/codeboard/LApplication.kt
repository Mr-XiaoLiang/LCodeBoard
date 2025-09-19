package com.lollipop.codeboard

import android.app.Application
import com.lollipop.codeboard.layer.CodeQwertyCnLayer
import com.lollipop.codeboard.layer.CodeQwertyEnLayer
import com.lollipop.codeboard.layer.Number9Layer
import com.lollipop.codeboard.layer.SymbolEnLayer

class LApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        registerLayer()
    }

    private fun registerLayer() {
        IMLayerStore.register(IMLayerStore.LAYER_QWERTY_EN, CodeQwertyEnLayer::class.java)
        IMLayerStore.register(IMLayerStore.LAYER_QWERTY_CN, CodeQwertyCnLayer::class.java)
        IMLayerStore.register(IMLayerStore.LAYER_SYMBOL_EN, SymbolEnLayer::class.java)
        IMLayerStore.register(IMLayerStore.LAYER_NUMBER_9, Number9Layer::class.java)
    }

}