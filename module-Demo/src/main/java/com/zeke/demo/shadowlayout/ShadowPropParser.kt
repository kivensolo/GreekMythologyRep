@file:Suppress("ClassName", "ObjectPropertyName")

package com.zeke.demo.shadowlayout

import android.content.res.TypedArray
import android.text.TextUtils
import android.util.Log
import androidx.annotation.StyleableRes
import com.kingz.module.common.utils.ParseUtils
import com.zeke.demo.R

/**
 * author：ZekeWang
 * date：2021/6/2
 * description：自定义属性解析器
 */
class ShadowPropParser(private val typedAttr: TypedArray) {
    private val TAG: String = ShadowPropParser::class.java.simpleName
    private var _typedArrayParserMap = HashMap<@StyleableRes Int, ParseInvoker>()

    init {
        //初始化注册自定义属性
        _typedArrayParserMap[R.styleable.ShadowLayout_border] = object : ParseInvoker() {
            override fun doParse(typeArr: TypedArray): Any {
                return ParsedTyped_Border.doParse(typeArr)
            }
        }
    }

    /**
     * 获取解析到的值
     */
    fun <T> getParsedValue(@StyleableRes index: Int): T? {
        val parseInvoker = _typedArrayParserMap[index]
        if (parseInvoker == null) {
            Log.w(TAG, "unsupported Styleable: $index")
            return null
        }
        val parsed = parseInvoker.doParse(typedAttr)
        return if(parsed == null) {
            null
        } else {
            @Suppress("UNCHECKED_CAST")
            parsed as T
        }
    }

    abstract class ParseInvoker {
        abstract fun doParse(typeArr: TypedArray): Any?
    }


    /**
     * [嵌套类] 自定义Border解析器
     */
    class ParsedTyped_Border {
        var size = 0f
        var pos = 0.5f
        var xRadius = 0f
        var yRadius = 0f
        var color = -0x1000000

        companion object {
            fun doParse(typeArr: TypedArray): Any {
                val borderObj = ParsedTyped_Border()
                val borderVal = typeArr.getString(R.styleable.ShadowLayout_border)
                if(!TextUtils.isEmpty(borderVal)){
                    val borderParams = borderVal!!.split(",")
                    when(borderParams.size){
                        5 -> {
                            borderObj.size = ParseUtils.tryParseFloat(borderParams[0], 0f)
                            borderObj.color = ParseUtils.tryParseHex(borderParams[1], 0L).toInt()
                            borderObj.xRadius = ParseUtils.tryParseFloat(borderParams[2], 0f)
                            borderObj.yRadius = ParseUtils.tryParseFloat(borderParams[3], 0f)
                            borderObj.pos = ParseUtils.tryParseFloat(borderParams[4], 0f)
                        }
                        4 -> {
                            borderObj.size = ParseUtils.tryParseFloat(borderParams[0], 0f)
                            borderObj.color = ParseUtils.tryParseHex(borderParams[1], 0L).toInt()
                            borderObj.xRadius = ParseUtils.tryParseFloat(borderParams[2], 0f)
                            borderObj.yRadius = ParseUtils.tryParseFloat(borderParams[3], 0f)
                        }
                        3 -> {
                            borderObj.size = ParseUtils.tryParseFloat(borderParams[0], 0f)
                            borderObj.color = ParseUtils.tryParseHex(borderParams[1], 0L).toInt()
                            borderObj.xRadius = ParseUtils.tryParseFloat(borderParams[2], 0f)
                        }
                        2 -> {
                            borderObj.size = ParseUtils.tryParseFloat(borderParams[0], 0f)
                            borderObj.color = ParseUtils.tryParseHex(borderParams[1], 0L).toInt()
                        }
                        1 -> {
                            borderObj.size = ParseUtils.tryParseFloat(borderParams[0], 0f)
                        }
                    }
                }
                return borderObj
            }


        }

        override fun toString(): String {
            return "ParsedTyped_Border(size=$size, pos=$pos, xRadius=$xRadius, yRadius=$yRadius, color=$color)"
        }
    }

}