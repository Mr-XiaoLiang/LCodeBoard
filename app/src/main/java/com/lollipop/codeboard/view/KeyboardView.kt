package com.lollipop.codeboard.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.lollipop.codeboard.R
import com.lollipop.codeboard.keyboard.KeyInfo
import com.lollipop.codeboard.keyboard.KeyboardInfo
import com.lollipop.codeboard.keyboard.KeyboardInfoFactory
import com.lollipop.codeboard.keyboard.RowInfo

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
        rootInfo.rows.forEach { rowInfo ->
            val rowLayoutHolder = RowLayoutHolder(rowInfo)
            rowList.add(rowLayoutHolder)
            rowInfo.keys.forEach { keyInfo ->
                val keyHolder = adapter.createHodler(keyInfo)
                val keyView = keyHolder.view
                val keyLayoutHolder = KeyLayoutHolder(keyInfo, keyView, keyHolder)
                rowLayoutHolder.add(keyLayoutHolder)
                addView(keyView)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthContent = widthSize - paddingLeft - paddingRight
        var heightContent = (rowList.size * (keyHeightSize + verticalGapSize)) - verticalGapSize
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

        onMeasureKey(widthContent, heightContent - paddingTop - paddingBottom)

        setMeasuredDimension(widthSize, heightContent)
    }

    private fun onMeasureKey(widthContent: Int, heightContent: Int) {
        val keyHeight = ((heightContent + verticalGapSize) / rowList.size) - verticalGapSize
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(keyHeight, MeasureSpec.EXACTLY)
        rowList.forEach { row ->
            row.keyLayoutHolders.forEach { key ->
                val widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (key.info.weight * widthContent).toInt(),
                    MeasureSpec.EXACTLY
                )
                key.view.measure(widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val widthContent = right - left - paddingLeft - paddingRight
        val heightContent = bottom - top - paddingTop - paddingBottom
        onLayoutKey(widthContent, heightContent)
    }

    private fun onLayoutKey(widthContent: Int, heightContent: Int) {
        val vGap = verticalGapSize
        val keyHeight = ((heightContent + vGap) / rowList.size) - vGap
        val hGap = (horizontalGapWeight * widthContent).toInt()
        var keyTop = paddingTop
        rowList.forEach { row ->
            var rowWidth = row.weight * widthContent
            rowWidth += hGap * (row.size - 1)
            var keyLeft = paddingLeft + ((widthContent - rowWidth) * 0.5F)
            row.keyLayoutHolders.forEach { key ->
                val keyWidth = key.info.weight * widthContent
                key.view.layout(
                    keyLeft.toInt(),
                    keyTop,
                    (keyLeft + keyWidth).toInt(),
                    keyTop + keyHeight
                )
                keyLeft += keyWidth
                keyLeft += hGap
            }
            keyTop += keyHeight
            keyTop += vGap
        }
    }

    fun setDecorationKey(decorationKey: DecorationKey) {
        rowList.forEach { row ->
            row.updateDecorationKey(decorationKey)
        }
    }

    private class EditModeAdapter(private val context: Context) : KeyViewAdapter {

        override fun createHodler(info: KeyInfo): KeyHolder {
            return EditModeKeyHolder(createView(info))
        }

        private fun createView(info: KeyInfo): View {
            return TextView(context).apply {
                text = info.key
                background = ColorDrawable(Color.GRAY)
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
            }
        }

    }

    private class EditModeKeyHolder(
        override val view: View
    ) : KeyHolder {
        override fun onDecorationKeyChanged(key: DecorationKey) {
        }
    }

    interface KeyViewAdapter {

        fun createHodler(info: KeyInfo): KeyHolder

    }

    interface KeyHolder {

        val view: View

        fun onDecorationKeyChanged(key: DecorationKey)

    }

    private class RowLayoutHolder(
        val row: RowInfo,
    ) {

        var weight: Float = 0F
            private set

        val size: Int
            get() {
                return keyLayoutHolders.size
            }

        val keyLayoutHolders = mutableListOf<KeyLayoutHolder>()

        fun updateDecorationKey(decorationKey: DecorationKey) {
            keyLayoutHolders.forEach {
                it.onDecorationKeyChanged(decorationKey)
            }
        }

        fun add(keyLayoutHolder: KeyLayoutHolder) {
            weight += keyLayoutHolder.info.weight
            keyLayoutHolders.add(keyLayoutHolder)
        }

    }

    private class KeyLayoutHolder(
        val info: KeyInfo,
        val view: View,
        val holder: KeyHolder
    ) {

        var decorationKey: DecorationKey = DecorationKey.Empty

        fun onDecorationKeyChanged(key: DecorationKey) {
            decorationKey = key
            holder.onDecorationKeyChanged(key)
        }

    }

    enum class DecorationKey {
        Shift,
        Command,
        Option,
        Empty
    }

}