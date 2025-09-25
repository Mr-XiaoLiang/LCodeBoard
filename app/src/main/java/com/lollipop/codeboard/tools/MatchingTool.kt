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
    fun match(keyword: String, value: String, ignoreCase: Boolean = true): Int {
        if (keyword.length > value.length) {
            // 长度超了，分数就是负数
            return -1
        }
        if (keyword.isEmpty()) {
            // 空串，分数就是0
            return 0
        }
        if (value.isEmpty()) {
            // 空串，分数就是0
            return 0
        }

        val valueLength = value.length
        val keyLength = keyword.length
        var keyIndex = 0
        var resultScore = 0

        for (valueIndex in 0 until valueLength) {
            val valueChar = value[valueIndex]
            val keyChar = keyword[keyIndex]
            val keyMode = keyIndex
            if (valueChar == keyChar) {
                keyIndex++
                // 字符完全一样，就加2分
                resultScore += 2
            } else if (ignoreCase) {
                val valueCharCode = valueChar.code
                val keyCharCode = keyChar.code
                if (valueCharCode in lowerRange) {
                    // 小写字母
                    if (valueCharCode == (keyCharCode - CASE_OFFSET)) {
                        keyIndex++
                        resultScore++
                    }
                } else if (valueCharCode in upperRange) {
                    // 大写字母
                    if (valueCharCode == (keyCharCode + CASE_OFFSET)) {
                        keyIndex++
                        resultScore++
                    }
                }
            }
            // 如果本次匹成功了, 并且位置一样，那么 score +1
            if (keyIndex > keyMode && keyMode == valueIndex) {
                resultScore++
            }
            if (keyIndex >= keyLength) {
                return resultScore
            }
        }
        return resultScore
    }

}