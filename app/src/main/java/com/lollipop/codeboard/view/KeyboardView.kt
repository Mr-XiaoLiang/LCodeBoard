package com.lollipop.codeboard.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toDrawable
import com.lollipop.codeboard.R
import com.lollipop.codeboard.keyboard.DecorationKey
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.KeyboardInfo
import com.lollipop.codeboard.keyboard.KeyboardInfoFactory
import com.lollipop.codeboard.keyboard.RowInfo
import com.lollipop.codeboard.ui.KeyboardTheme
import com.lollipop.codeboard.ui.Skin
import kotlin.math.min

class KeyboardView(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private var keyboardInfo: KeyboardInfo? = null

    private var horizontalGapWeight: Float = 0F
    private var verticalGapSize: Int = 0
    private var keyHeightSize: Int = 0
    private var keyWidthWeight: Float = 0F

    private var keyViewAdapter: KeyViewAdapter? = null

    private val rowList = mutableListOf<RowLayoutHolder>()

    var decorationKey: DecorationKey = DecorationKey.Empty
        private set
    var isSticky = false
        private set

    var boardTheme: KeyboardTheme = Skin.current.keyboard
        private set

    init {
        context.withStyledAttributes(
            attrs, R.styleable.KeyboardView
        ) {
            val resourceId = getResourceId(R.styleable.KeyboardView_keyboard, 0)
            if (resourceId != 0) {
                keyboardInfo = KeyboardInfoFactory.parse(context, resourceId)
            }
        }
        if (isInEditMode) {
            setKeyViewAdapter(EditModeAdapter(context))
        }
    }

    fun setKeyboardInfo(keyboardInfo: KeyboardInfo) {
        this.keyboardInfo = keyboardInfo
        onKeyboardInfoChanged()
    }

    fun setKeyViewAdapter(adapter: KeyViewAdapter) {
        this.keyViewAdapter = adapter
        onKeyboardInfoChanged()
    }

    private fun onKeyboardInfoChanged() {
        removeAllViews()
        rowList.clear()
        val rootInfo = keyboardInfo
        if (rootInfo == null) {
            horizontalGapWeight = 0F
            keyHeightSize = 0
            keyWidthWeight = 0F
            verticalGapSize = 0
            return
        }
        horizontalGapWeight = rootInfo.horizontalGapWeight
        keyHeightSize = rootInfo.keyHeightSize
        keyWidthWeight = rootInfo.keyWidthWeight
        verticalGapSize = rootInfo.verticalGapSize
        val adapter = keyViewAdapter
        if (adapter == null) {
            return
        }
        val decoration = decorationKey
        val sticky = isSticky
        val theme = boardTheme
        val rowHeightList = Array(rootInfo.rows.size) { 0 }
        val rows = rootInfo.rows
        for (index in rows.indices) {
            val rowInfo = rows[index]
            val rowLayoutHolder = RowLayoutHolder(rowInfo)
            rowList.add(rowLayoutHolder)
            rowInfo.keys.forEach { keyInfo ->
                val keyHolder = adapter.createHodler(keyInfo)
                val keyView = keyHolder.view
                keyHolder.onDecorationKeyChanged(decoration, sticky)
                keyHolder.updateTheme(theme)
                val keyLayoutHolder = KeyLayoutHolder(keyInfo, keyView, keyHolder)
                rowLayoutHolder.add(keyLayoutHolder)
                addView(keyView)
            }
            rowHeightList[index] = rowLayoutHolder.keyHeightSize
        }
        // 更新一次行高，解决一些跨行的组件问题
        for (index in rows.indices) {
            rowList[index].updateKeyHeight(index, rowHeightList, verticalGapSize)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthContent = widthSize - paddingLeft - paddingRight
        val rowsHeight = getRowsHeight()
        var heightContent = rowsHeight
        heightContent += (paddingTop + paddingBottom)
        when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightContent = heightSize
            }

            MeasureSpec.AT_MOST -> {
                if (heightContent > heightSize) {
                    heightContent = heightSize
                }
            }

            else -> {}
        }

        onMeasureKey(
            widthContent = widthContent,
            rowsHeight = rowsHeight,
            heightContent = heightContent - paddingTop - paddingBottom
        )

        setMeasuredDimension(widthSize, heightContent)
    }

    private fun getRowsHeight(): Int {
        var heightContent = 0
        for (index in rowList.indices) {
            val row = rowList[index]
            heightContent += row.keyHeightSize
            if (index != 0) {
                heightContent += verticalGapSize
            }
        }
        return heightContent
    }

    private fun onMeasureKey(widthContent: Int, rowsHeight: Int, heightContent: Int) {
        val heightWeight = if (rowsHeight == heightContent) {
            1F
        } else {
            (rowsHeight.toFloat() / heightContent.toFloat())
        }
        rowList.forEach { row ->
            row.keyLayoutHolders.forEach { key ->
                val keyWidth = (key.info.weight * widthContent).toInt()
                val widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    keyWidth,
                    MeasureSpec.EXACTLY
                )
                val keyHeight = ((key.keyHeightSize * heightWeight) + 0.5F).toInt()
                key.view.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(keyHeight, MeasureSpec.EXACTLY)
                )
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val widthContent = right - left - paddingLeft - paddingRight
        val heightContent = bottom - top - paddingTop - paddingBottom
        onLayoutKey(widthContent, heightContent)
    }

    private fun onLayoutKey(widthContent: Int, heightContent: Int) {
        val rowsHeight = getRowsHeight()
        val heightWeight = if (rowsHeight == heightContent) {
            1F
        } else {
            (rowsHeight.toFloat() / heightContent.toFloat())
        }
        val vGap = (verticalGapSize * heightWeight).toInt()
        val hGap = (horizontalGapWeight * widthContent).toInt()
        var keyTop = paddingTop
        rowList.forEach { row ->
            var rowWidth = row.weight * widthContent
            val rowHeight = (row.keyHeightSize * heightWeight).toInt()
            rowWidth += hGap * (row.size - 1)
            var keyLeft = (paddingLeft + ((widthContent - rowWidth) * 0.5F)).toInt()
            row.keyLayoutHolders.forEach { key ->
                val keyWidth = (key.info.weight * widthContent).toInt()
                val keyHeight = (key.keyHeightSize * heightWeight).toInt()
                key.holder.onSizeChanged(
                    panelWidth = widthContent,
                    panelHeight = heightContent,
                    rowWidth = rowWidth.toInt(),
                    rowHeight = rowHeight,
                    keyWidth = keyWidth,
                    keyHeight = keyHeight
                )
                key.view.layout(
                    keyLeft,
                    keyTop,
                    (keyLeft + keyWidth),
                    keyTop + keyHeight
                )
                keyLeft += keyWidth
                keyLeft += hGap
            }
            keyTop += rowHeight
            keyTop += vGap
        }
    }

    fun setDecorationKey(decorationKey: DecorationKey, sticky: Boolean) {
        this.decorationKey = decorationKey
        this.isSticky = sticky
        updateDecoration()
    }

    fun updateTheme(theme: KeyboardTheme) {
        this.boardTheme = theme
        rowList.forEach { row ->
            row.updateTheme(theme)
        }
    }

    private fun updateDecoration() {
        val decoration = decorationKey
        val sticky = isSticky
        rowList.forEach { row ->
            row.updateDecorationKey(decoration, sticky)
        }
    }

    private class EditModeAdapter(private val context: Context) : KeyViewAdapter {

        override fun createHodler(info: KeyInfo): KeyHolder {
            return EditModeKeyHolder(createView(info))
        }

        private fun createView(info: KeyInfo): View {
            if (info.key.isEmpty()) {
                return Space(context)
            }
            return TextView(context).apply {
                text = info.key
                background = Color.GRAY.toDrawable()
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
            }
        }

    }

    private class EditModeKeyHolder(
        override val view: View
    ) : KeyHolder {
        override fun onSizeChanged(
            panelWidth: Int,
            panelHeight: Int,
            rowWidth: Int,
            rowHeight: Int,
            keyWidth: Int,
            keyHeight: Int
        ) {
        }

        override fun updateTheme(theme: KeyboardTheme) {
        }

        override fun onDecorationKeyChanged(
            key: DecorationKey,
            isSticky: Boolean
        ) {
        }

    }

    interface KeyViewAdapter {

        fun createHodler(info: KeyInfo): KeyHolder

    }

    interface KeyHolder {

        val view: View

        fun onSizeChanged(
            panelWidth: Int,
            panelHeight: Int,
            rowWidth: Int,
            rowHeight: Int,
            keyWidth: Int,
            keyHeight: Int
        )

        fun updateTheme(theme: KeyboardTheme)

        fun onDecorationKeyChanged(key: DecorationKey, isSticky: Boolean)

    }

    private class RowLayoutHolder(
        val row: RowInfo,
    ) {

        val keyHeightSize: Int
            get() {
                return row.keyHeightSize
            }

        var weight: Float = 0F
            private set

        val size: Int
            get() {
                return keyLayoutHolders.size
            }

        val keyLayoutHolders = mutableListOf<KeyLayoutHolder>()

        fun updateTheme(theme: KeyboardTheme) {
            keyLayoutHolders.forEach {
                it.updateTheme(theme)
            }
        }

        fun updateDecorationKey(decorationKey: DecorationKey, isSticky: Boolean) {
            keyLayoutHolders.forEach {
                it.onDecorationKeyChanged(decorationKey, isSticky)
            }
        }

        fun add(keyLayoutHolder: KeyLayoutHolder) {
            weight += keyLayoutHolder.info.weight
            keyLayoutHolders.add(keyLayoutHolder)
        }

        fun updateKeyHeight(rowIndex: Int, rowHeightList: Array<Int>, vGap: Int) {
            keyLayoutHolders.forEach { key ->
                key.updateKeyHeight(rowIndex, rowHeightList, vGap)
            }
        }

    }

    private class KeyLayoutHolder(
        val info: KeyInfo,
        val view: View,
        val holder: KeyHolder
    ) {

        var keyHeightSize: Int = 0
            private set

        val span: Int
            get() {
                return info.span
            }

        var decorationKey: DecorationKey = DecorationKey.Empty

        fun onDecorationKeyChanged(key: DecorationKey, isSticky: Boolean) {
            decorationKey = key
            holder.onDecorationKeyChanged(key, isSticky)
        }

        fun updateTheme(theme: KeyboardTheme) {
            holder.updateTheme(theme)
        }

        fun updateKeyHeight(rowIndex: Int, rowHeightList: Array<Int>, vGap: Int) {
            if (rowIndex < 0 || rowIndex >= rowHeightList.size) {
                keyHeightSize = 0
                return
            }
            if (span <= 1) {
                keyHeightSize = rowHeightList[rowIndex]
                return
            }
            val rowMax = min(rowIndex + span, rowHeightList.size)
            keyHeightSize = 0
            for (i in rowIndex until rowMax) {
                keyHeightSize += rowHeightList[i]
                if (i != rowIndex) {
                    keyHeightSize += vGap
                }
            }
        }

    }

}