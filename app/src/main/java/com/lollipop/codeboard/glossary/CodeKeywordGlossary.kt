package com.lollipop.codeboard.glossary

import android.content.Context
import com.lollipop.codeboard.data.Language
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

    abstract val language: Language

    object JAVA : CodeKeywordGlossary("keywords/java.k") {

        override val language = Language("java", "Java", 0)

    }

    object KOTLIN : CodeKeywordGlossary("keywords/kotlin.k") {
        override val language = Language("kotlin", "Kotlin", 0)
    }

    object HTML : CodeKeywordGlossary("keywords/html.k") {
        override val language = Language("html", "HTML", 0)
    }

    object LUA : CodeKeywordGlossary("keywords/lua.k") {
        override val language = Language("lua", "Lua", 0)
    }

}