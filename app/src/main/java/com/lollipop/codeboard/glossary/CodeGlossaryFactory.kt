package com.lollipop.codeboard.glossary

import com.lollipop.codeboard.data.Language
import com.lollipop.codeboard.glossary.basic.BasicCodeGlossary

object CodeGlossaryFactory {

    fun filter(language: Language): List<BasicCodeGlossary> {
        val resultList = ArrayList<BasicCodeGlossary>()
        filterPresetKeywordsGlossary(language, resultList)
        return resultList
    }

    private fun filterPresetKeywordsGlossary(
        language: Language,
        outList: MutableList<BasicCodeGlossary>
    ) {
        when (language.code) {
            CodeKeywordGlossary.JAVA.language.code -> {
                outList.add(CodeKeywordGlossary.JAVA)
            }

            CodeKeywordGlossary.KOTLIN.language.code -> {
                outList.add(CodeKeywordGlossary.KOTLIN)
            }

            CodeKeywordGlossary.HTML.language.code -> {
                outList.add(CodeKeywordGlossary.HTML)
            }

            CodeKeywordGlossary.LUA.language.code -> {
                outList.add(CodeKeywordGlossary.LUA)
            }

            else -> {
                // 什么也不做
            }
        }
    }

}