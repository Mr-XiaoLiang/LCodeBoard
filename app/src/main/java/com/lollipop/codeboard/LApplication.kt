package com.lollipop.codeboard

import android.app.Application
import android.content.Context
import com.lollipop.codeboard.glossary.CodeKeywordGlossary
import com.lollipop.codeboard.layer.CodeQwertyCnLayer
import com.lollipop.codeboard.layer.CodeQwertyEnLayer
import com.lollipop.codeboard.layer.Number9Layer
import com.lollipop.codeboard.layer.SymbolEnLayer
import com.lollipop.codeboard.tools.PreloadManager

class LApplication : Application() {

    private val contextProvider = object : PreloadManager.ContextProvider {
        override fun getContext(): Context {
            return this@LApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerLayer()
        preloadCodeKeyword()
        PreloadManager.preload(contextProvider)
    }

    private fun registerLayer() {
        IMLayerStore.register(IMLayerStore.LAYER_QWERTY_EN, CodeQwertyEnLayer::class.java)
        IMLayerStore.register(IMLayerStore.LAYER_QWERTY_CN, CodeQwertyCnLayer::class.java)
        IMLayerStore.register(IMLayerStore.LAYER_SYMBOL_EN, SymbolEnLayer::class.java)
        IMLayerStore.register(IMLayerStore.LAYER_NUMBER_9, Number9Layer::class.java)
    }

    private fun preloadCodeKeyword() {
        PreloadManager.register(
            CodeKeywordGlossary.JAVA,
            CodeKeywordGlossary.KOTLIN,
            CodeKeywordGlossary.LUA,
            CodeKeywordGlossary.HTML
        )
    }

}