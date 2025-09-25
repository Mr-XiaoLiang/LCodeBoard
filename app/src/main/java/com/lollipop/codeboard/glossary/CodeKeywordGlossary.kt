package com.lollipop.codeboard.glossary

import android.content.Context
import com.lollipop.codeboard.glossary.basic.BasicCodeGlossary
import com.lollipop.codeboard.protocol.Preloadable

sealed class CodeKeywordGlossary(private val assetsPath: String) : BasicCodeGlossary(),
    Preloadable {

    private val glossaryStore = AssetsGlossaryStoreByLine(assetsPath)

    override fun preload(context: Context) {
        glossaryStore.preload(context)
    }

    override fun getStore(): GlossaryStore? {
        return glossaryStore
    }

    object JAVA : CodeKeywordGlossary("keywords/java.k")

    object KOTLIN : CodeKeywordGlossary("keywords/kotlin.k")

    object HTML : CodeKeywordGlossary("keywords/html.k")

    object LUA : CodeKeywordGlossary("keywords/lua.k")

}