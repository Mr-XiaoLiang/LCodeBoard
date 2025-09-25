package com.lollipop.codeboard.glossary.basic

import android.content.Context
import com.lollipop.codeboard.protocol.Candidate
import com.lollipop.codeboard.protocol.CandidateType
import com.lollipop.codeboard.protocol.Preloadable
import com.lollipop.codeboard.tools.MatchingTool
import com.lollipop.codeboard.tools.doAsync
import com.lollipop.codeboard.tools.onUI
import org.json.JSONArray
import java.io.InputStream

abstract class BasicCodeGlossary : BasicGlossary() {

    abstract fun getStore(): GlossaryStore?

    override fun onDraftUpdate(payload: LoadPayload) {
        val store = getStore() ?: return
        asyncLoad(payload, store::filter)
    }

    class AssetsGlossaryStoreByLine(assetsPath: String) : AssetsGlossaryStore(assetsPath) {
        override fun parseWorlds(input: InputStream, out: (String) -> Unit) {
            val bufferedReader = input.bufferedReader()
            while (true) {
                val line = bufferedReader.readLine()?.trim()
                if (line == null) {
                    return
                }
                if (line.isNotEmpty()) {
                    out(line)
                }
            }
        }
    }

    class AssetsGlossaryStoreByJson(assetsPath: String) : AssetsGlossaryStore(assetsPath) {
        override fun parseWorlds(input: InputStream, out: (String) -> Unit) {
            val json = input.bufferedReader().readText()
            val jsonArray = JSONArray(json)
            val length = jsonArray.length()
            for (i in 0 until length) {
                jsonArray.optString(i)?.let {
                    out(it)
                }
            }
        }
    }

    abstract class AssetsGlossaryStore(private val assetsPath: String) : GlossaryStore(),
        Preloadable {

        private val wordList = ArrayList<String>()

        override fun wordCount(): Int {
            return wordList.size
        }

        override fun getWord(index: Int): String {
            return wordList[index]
        }

        override fun preload(context: Context) {
            doAsync {
                context.assets.open(assetsPath).use { input ->
                    val outSet = HashSet<String>()
                    val outCallback: (String) -> Unit = {
                        outSet.add(it)
                    }
                    parseWorlds(input, outCallback)
                    onUI {
                        wordList.clear()
                        wordList.addAll(outSet)
                    }
                }
            }
        }

        abstract fun parseWorlds(input: InputStream, out: (String) -> Unit)

    }

    abstract class GlossaryStore {

        open fun filter(payload: LoadPayload): List<Candidate> {
            return filter(draft = payload.draft)
        }

        fun filter(draft: String): List<Candidate> {
            val count = wordCount()
            val result = mutableListOf<Candidate>()
            for (i in 0 until count) {
                val word = getWord(i)
                val level = MatchingTool.match(draft, word)
                if (level > 0) {
                    result.add(Candidate(text = word, level = level, CandidateType.Code))
                }
            }
            return result
        }

        abstract fun wordCount(): Int

        abstract fun getWord(index: Int): String

    }

}