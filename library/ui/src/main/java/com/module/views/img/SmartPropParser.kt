package com.module.views.img

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.Log
import androidx.annotation.StyleableRes
import com.module.views.R

//SmartImageView各参数解析器

class SmartPropParser(private val context: Context,
private val typedAttr: TypedArray) {
    private val TAG: String = SmartPropParser::class.java.simpleName
    private var _typedArrayParserMap = HashMap<@StyleableRes Int, ParseInvoker>()

    init {
        //初始化注册自定义属性
        _typedArrayParserMap[R.styleable.SmartImageView_border] = object : ParseInvoker() {
            override fun doParse(context:Context, typeArr: TypedArray): Any {
                return ParsedStyle_Border.doParse(context, typeArr)
            }
        }
    }

    /**
     * 获取解析到的值
     */
    fun <T> getParsedValue(@StyleableRes attrId: Int): T {
        val parseInvoker = _typedArrayParserMap[attrId]
        if (parseInvoker == null) {
            Log.e(TAG, "unsupported Styleable: $attrId")
            throw IllegalArgumentException("unsupported Styleable: $attrId, Please init first !!!")
        }
        val parsed = parseInvoker.doParse(context, typedAttr)
        @Suppress("UNCHECKED_CAST")
        return  parsed as T
    }

    abstract class ParseInvoker {
        abstract fun doParse(context:Context, typeArr: TypedArray): Any
    }
}

/**
 * 元素边框。
 * 宽度，颜色，ltx-radius，lty-radius，rtx-radius，rty-radius，brx-radius，bry-radius，blx-radius，bly-radius，位置
 * 宽度，颜色，圆角x-radius，圆角y-radius，位置
 * 宽度，颜色，圆角x-radius，圆角y-radius
 * 宽度，颜色，位置
 * 如：8,E01595EF,12,12,0.35
 */
class ParsedStyle_Border {
    var size = 0f
    var pos = 0.5f
    var xRadius = 0f
    var yRadius = 0f
    var radiiArrayMode = false
    //Array of 8 values, 4 pairs of [X,Y] radii  左上，右上，右下，左下
    var radii: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    var color = -0x1000000

    companion object {
        fun doParse(context:Context, prop: TypedArray): ParsedStyle_Border {
            val borderObj = ParsedStyle_Border()
            val density = context.resources.displayMetrics.density
            val borderVal = prop.getString(R.styleable.SmartImageView_border)
            if (!TextUtils.isEmpty(borderVal)) {
                val borderParams: List<String> = borderVal.split(",")
                when (borderParams.size) {
                    11 -> {
                        borderObj.radiiArrayMode = true
                        borderObj.color = ParseUtils.tryParseHex(borderParams[1]).toInt()
                        borderObj.size = ParseUtils.tryParseFloat(borderParams[0]) * density
                        borderObj.radii[0] = ParseUtils.tryParseFloat(borderParams[2]) * density
                        borderObj.radii[1] = ParseUtils.tryParseFloat(borderParams[3]) * density
                        borderObj.radii[2] = ParseUtils.tryParseFloat(borderParams[4]) * density
                        borderObj.radii[3] = ParseUtils.tryParseFloat(borderParams[5]) * density
                        borderObj.radii[4] = ParseUtils.tryParseFloat(borderParams[6]) * density
                        borderObj.radii[5] = ParseUtils.tryParseFloat(borderParams[7]) * density
                        borderObj.radii[6] = ParseUtils.tryParseFloat(borderParams[8]) * density
                        borderObj.radii[7] = ParseUtils.tryParseFloat(borderParams[9]) * density
                        borderObj.pos = ParseUtils.tryParseFloat(borderParams[10])
                    }
                    5 -> {
                        borderObj.size = ParseUtils.tryParseFloat(borderParams[0]) * density
                        borderObj.color = ParseUtils.tryParseHex(borderParams[1]).toInt()
                        borderObj.xRadius = ParseUtils.tryParseFloat(borderParams[2]) * density
                        borderObj.yRadius = ParseUtils.tryParseFloat(borderParams[3]) * density
                        borderObj.radii[0] = borderObj.xRadius
                        borderObj.radii[2] = borderObj.xRadius
                        borderObj.radii[4] = borderObj.xRadius
                        borderObj.radii[6] = borderObj.xRadius

                        borderObj.radii[1] = borderObj.yRadius
                        borderObj.radii[3] = borderObj.yRadius
                        borderObj.radii[5] = borderObj.yRadius
                        borderObj.radii[7] = borderObj.yRadius
                        borderObj.pos = ParseUtils.tryParseFloat(borderParams[4])
                    }
                    4 -> {
                        borderObj.size = ParseUtils.tryParseFloat(borderParams[0]) * density
                        borderObj.color = ParseUtils.tryParseHex(borderParams[1]).toInt()
                        borderObj.xRadius = ParseUtils.tryParseFloat(borderParams[2]) * density
                        borderObj.yRadius = ParseUtils.tryParseFloat(borderParams[3]) * density
                        borderObj.radii[0] = borderObj.xRadius
                        borderObj.radii[2] = borderObj.xRadius
                        borderObj.radii[4] = borderObj.xRadius
                        borderObj.radii[6] = borderObj.xRadius

                        borderObj.radii[1] = borderObj.yRadius
                        borderObj.radii[3] = borderObj.yRadius
                        borderObj.radii[5] = borderObj.yRadius
                        borderObj.radii[7] = borderObj.yRadius
                    }
                    3 -> {
                        borderObj.size = ParseUtils.tryParseFloat(borderParams[0]) * density
                        borderObj.color = ParseUtils.tryParseHex(borderParams[1]).toInt()
                        borderObj.pos = ParseUtils.tryParseFloat(borderParams[2])
                    }
                    2 -> {
                        borderObj.size = ParseUtils.tryParseFloat(borderParams[0]) * density
                        borderObj.color = ParseUtils.tryParseHex(borderParams[1]).toInt()
                    }
                    1 -> {
                        borderObj.size = ParseUtils.tryParseFloat(borderParams[0]) * density
                    }
                }
            }
            return borderObj
        }
    }
}


object ParseUtils {
    // <editor-fold defaultstate="collapsed" desc="基础数据类型之间的转换">
    fun tryParseFloat(borderObj: String, defVal: Float = 0f): Float {
        if (TextUtils.isEmpty(borderObj)) {
            return defVal
        }
        try {
            return borderObj.toFloat()
        } catch (e: Exception) {
        }
        return defVal
    }

    fun tryParseDouble(borderObj: String, defVal: Double = 0.0): Double {
        if (TextUtils.isEmpty(borderObj)) {
            return defVal
        }
        try {
            return borderObj.toDouble()
        } catch (e: java.lang.Exception) {
        }
        return defVal
    }

    fun tryParseHex(borderObj: String, defVal: Long = 0L): Long {
        if (TextUtils.isEmpty(borderObj)) {
            return defVal
        }
        try {
            return borderObj.toLong(16)
        } catch (e: java.lang.Exception) {
        }
        return defVal
    }

    fun tryParseLong(borderObj: String, defVal: Long = 0L): Long {
        if (TextUtils.isEmpty(borderObj)) {
            return defVal
        }
        try {
            return borderObj.toLong()
        } catch (e: java.lang.Exception) {
        }
        return defVal
    }

    fun tryParseInt(borderObj: String, defVal: Int = 0): Int {
        if (TextUtils.isEmpty(borderObj)) {
            return defVal
        }
        try {
            return borderObj.toInt()
        } catch (e: java.lang.Exception) {
        }
        return defVal
    }

    fun tryParseInt(borderObj: String): Int {
        return tryParseInt(borderObj, 0)
    }

    fun tryParseLong(borderObj: String): Long {
        return tryParseLong(borderObj, 0)
    }

    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="精度提升转换">

    fun roundToInt(borderObj: Double): Int {
        return if (borderObj >= 0) {
            (borderObj + 0.5).toInt()
        } else {
            (borderObj - 0.5).toInt()
        }
    }

    fun roundToInt(borderObj: Float): Int {
        return if (borderObj >= 0) {
            (borderObj + 0.5f).toInt()
        } else {
            (borderObj - 0.5f).toInt()
        }
    }
// </editor-fold>
}