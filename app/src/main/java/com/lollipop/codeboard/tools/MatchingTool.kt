package com.lollipop.codeboard.tools

object MatchingTool {
    private const val LOW_CASE_FROM = 'a'.code
    private const val LOW_CASE_TO = 'z'.code
    private const val UPPER_CASE_FROM = 'A'.code
    private const val UPPER_CASE_TO = 'Z'.code
    private const val CASE_OFFSET = (UPPER_CASE_FROM - LOW_CASE_FROM)

    private val lowerRange = LOW_CASE_FROM..LOW_CASE_TO
    private val upperRange = UPPER_CASE_FROM..UPPER_CASE_TO

    @JvmStatic
    @JvmOverloads
    fun match(keyword: String, value: String, ignoreCase: Boolean = true): Boolean {
        if (keyword.length > value.length) {
            return false
        }
        if (keyword.isEmpty()) {
            return false
        }
        if (value.isEmpty()) {
            return false
        }

        val valueLength = value.length
        val keyLength = keyword.length
        var keyIndex = 0

        for (valueIndex in 0 until valueLength) {
            val valueChar = value[valueIndex]
            val keyChar = keyword[keyIndex]
            if (valueChar == keyChar) {
                keyIndex++
            } else if (ignoreCase) {
                val valueCharCode = valueChar.code
                val keyCharCode = keyChar.code
                if (valueCharCode in lowerRange) {
                    // 小写字母
                    if (valueCharCode == (keyCharCode - CASE_OFFSET)) {
                        keyIndex++
                    }
                } else if (valueCharCode in upperRange) {
                    // 大写字母
                    if (valueCharCode == (keyCharCode + CASE_OFFSET)) {
                        keyIndex++
                    }
                }
            }
            if (keyIndex >= keyLength) {
                return true
            }
        }
        return false
    }

}