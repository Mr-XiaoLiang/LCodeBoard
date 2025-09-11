package com.lollipop.codeboard.keyboard

import android.content.Context
import android.util.TypedValue
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.Reader

class KeyboardInfo(
    val horizontalGapWeight: Float,
    val verticalGapSize: Int,
    val keyHeightSize: Int,
    val keyWidthWeight: Float
) {

    val rows = mutableListOf<RowInfo>()

}

class RowInfo(
    val keyHeightSize: Int,
) {
    val keys = mutableListOf<KeyInfo>()
}

class KeyInfo(
    val key: String,
    val shiftCase: String,
    val commandCase: String,
    val optionCase: String,
    val weight: Float
)

object KeyboardInfoFactory {

    private class KeyboardInfoBuilder {
        var horizontalGapWeight: Float = 0F
        var verticalGapSize: Int = 0
        var keyHeightSize: Int = 0
        var keyWidthWeight: Float = 0F

        fun build(): KeyboardInfo {
            return KeyboardInfo(
                horizontalGapWeight = horizontalGapWeight,
                verticalGapSize = verticalGapSize,
                keyHeightSize = keyHeightSize,
                keyWidthWeight = keyWidthWeight
            )
        }
    }

    private class KeyInfoBuilder {
        var key: String = ""
        var shiftCase: String = ""
        var commandCase: String = ""
        var optionCase: String = ""
        var weight: Float = 0F

        fun build(): KeyInfo {
            return KeyInfo(
                key = key,
                shiftCase = shiftCase,
                commandCase = commandCase,
                optionCase = optionCase,
                weight = weight
            )
        }
    }

    private class RowInfoBuilder {
        var keyHeightSize: Int = 0

        fun build(): RowInfo {
            return RowInfo(
                keyHeightSize = keyHeightSize
            )
        }
    }

    fun parse(context: Context, xmlResId: Int): KeyboardInfo {
        val xmlReader = context.resources.getXml(xmlResId)
        return parse(context, xmlReader)
    }

    fun parse(context: Context, reader: Reader): KeyboardInfo {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(reader)
        return parse(context, parser)
    }

    /**
    <Keyboard
    horizontalGap="0.09"
    keyHeight="52dp"
    keyWidth="0.09"
    verticalGap="0px">
    <Row>
    <Key
    command="Quit"
    option=""
    weight="0.01"
    shift="Q"
    key="q"/>
    </Row>
    </Keyboard>
     */
    fun parse(context: Context, parser: XmlPullParser): KeyboardInfo {
        var keyboardInfo: KeyboardInfo? = null

        var rowInfo: RowInfo? = null
        var defaultKeyWidth = 0F

        do {
            when (parser.eventType) {
                XmlPullParser.END_DOCUMENT -> {
                    break
                }

                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "Keyboard" -> {
                            val builder = KeyboardInfoBuilder()
                            forEachAttribute(parser) { name, value ->
                                when (name) {
                                    "horizontalGap" -> {
                                        builder.horizontalGapWeight =
                                            value.getTypedValue(context)
                                    }

                                    "keyHeight" -> {
                                        builder.keyHeightSize =
                                            value.getTypedValue(context).toInt()
                                    }

                                    "keyWidth" -> {
                                        val typedValue = value.getTypedValue(context)
                                        builder.keyWidthWeight = typedValue
                                        defaultKeyWidth = typedValue
                                    }

                                    "verticalGap" -> {
                                        builder.verticalGapSize =
                                            value.getTypedValue(context).toInt()
                                    }
                                }
                            }
                            keyboardInfo = builder.build()
                        }

                        "Row" -> {
                            val builder = RowInfoBuilder()
                            builder.keyHeightSize = keyboardInfo?.keyHeightSize ?: 0
                            forEachAttribute(parser) { name, value ->
                                when (name) {
                                    "keyHeight" -> {
                                        builder.keyHeightSize =
                                            value.getTypedValue(context).toInt()
                                    }
                                }
                            }
                            rowInfo = builder.build()
                            keyboardInfo?.rows?.add(rowInfo)
                        }

                        "Key" -> {
                            //    command="Quit"
                            //    option=""
                            //    weight="0.01"
                            //    shift="Q"
                            //    key="q"
                            val builder = KeyInfoBuilder()
                            builder.weight = defaultKeyWidth
                            forEachAttribute(parser) { name, value ->
                                when (name) {
                                    "key" -> {
                                        builder.key = value
                                    }

                                    "shift" -> {
                                        builder.shiftCase = value
                                    }

                                    "option" -> {
                                        builder.optionCase = value
                                    }

                                    "command" -> {
                                        builder.commandCase = value
                                    }

                                    "weight" -> {
                                        builder.weight = value.getTypedValue(context)
                                    }
                                }
                            }
                            rowInfo?.keys?.add(builder.build())
                        }
                    }
                }
            }
            parser.next()
        } while (true)
        return keyboardInfo!!
    }

    private fun String.getTypedValue(context: Context): Float {
        val value = this
        return when {
            value.endsWith("dp") -> {
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    value.substring(0, value.length - 2).toFloat(),
                    context.resources.displayMetrics
                )
            }

            value.endsWith("px") -> {
                value.substring(0, value.length - 2).toFloat()
            }

            value.endsWith("sp") -> {
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    value.substring(0, value.length - 2).toFloat(),
                    context.resources.displayMetrics
                )
            }

            value.endsWith("pt") -> {
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PT,
                    value.substring(0, value.length - 2).toFloat(),
                    context.resources.displayMetrics
                )
            }

            value.endsWith("%") -> {
                return value.substring(0, value.length - 1).toFloat() * 0.01F
            }

            else -> {
                value.toFloat()
            }
        }
    }

    private fun forEachAttribute(
        parser: XmlPullParser,
        action: (name: String, value: String) -> Unit
    ) {
        val attributeCount = parser.attributeCount
        for (i in 0 until attributeCount) {
            val name = parser.getAttributeName(i)
            val value = parser.getAttributeValue(i)
            action(name, value)
        }
    }

}
