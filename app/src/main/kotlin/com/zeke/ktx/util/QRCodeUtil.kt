package com.zeke.ktx.util

import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

object QRCodeUtil {
    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content       字符串内容
     * @param width         位图宽度,要求>=0(单位:px)
     * @param height        位图高度,要求>=0(单位:px)
     * @param charSet       字符集/字符转码格式 (支持格式:[CharacterSetECI])。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param errorCorrection 容错级别 (支持级别:[ErrorCorrectionLevel])。传null时,zxing源码默认使用 "L"
     * @param margin        空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param color_black   黑色色块的自定义颜色值
     * @param color_white   白色色块的自定义颜色值
     * @return 位图
     */
    @JvmOverloads
    fun createQRCodeBitmap(
        content: String?, width: Int, height: Int,
        charSet: CharacterSetECI? = CharacterSetECI.UTF8,
        errorCorrection: ErrorCorrectionLevel? = ErrorCorrectionLevel.H,
        margin: String? = "2",
        color_black: Int = Color.BLACK,
        color_white: Int = Color.WHITE
    ): Bitmap? {
        /** 1.参数合法性判断  */
        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null
        }
        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null
        }
        try {
            // 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象
            val hints = Hashtable<EncodeHintType, String>()
            // 字符转码格式设置
            if(charSet != null){
                hints[EncodeHintType.CHARACTER_SET] = charSet.toString()
            }
            // 容错级别设置
            if(errorCorrection != null){
                hints[EncodeHintType.ERROR_CORRECTION] = errorCorrection.bits.toString()
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints[EncodeHintType.MARGIN] = margin
            }
            val bitMatrix =   QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            // 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = color_black // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white // 白色色块像素设置
                    }
                }
            }

            // 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}