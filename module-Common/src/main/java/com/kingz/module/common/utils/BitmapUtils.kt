package com.kingz.module.common.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * author：ZekeWang
 * date：2021/11/3
 * description：位图工具类
 */
object BitmapUtils {

  // <editor-fold defaultstate="collapsed" desc="图片储存API">
    /**
     * 保存图片到指定路径
     * @param context
     * @param bitmap   要保存的图片
     * @param prefixName 自定义图片名称
     */
    fun saveImageToGallery(context: Context, bitmap: Bitmap, prefixName: String) {
        var fileName = prefixName
        val format: DateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
        fileName = fileName + format.format(Date()).toString() + ".JPEG"
        // 保存图片至指定路径（API-30已经过时）
        val storePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            //通过io流的方式来压缩保存图片
            val isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            // 其次把文件插入到系统图库(API-30已经过时)
            try {
                MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, fileName, null)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            Log.d("BitmapUtils","发送广播通知系统图库刷新数据")
            val uri = Uri.fromFile(file)
            //ACTION_MEDIA_SCANNER_SCAN_FILE在API-30已经过时
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            if (isSuccess) {
                Toast.makeText(context, "图片已保存至$file", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
  // </editor-fold>
}