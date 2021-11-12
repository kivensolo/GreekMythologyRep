package com.kingz.module.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
 * description：图像工具类
 */
object UIUtils {
    private val sCanvas: Canvas = Canvas()

    // <editor-fold defaultstate="collapsed" desc="Bitmap获取API">
    @RequiresApi(Build.VERSION_CODES.O)
    fun viewShotAfterOreo(
        view: AppCompatActivity,
        scale: Float = 1f,
        listener: PixelCopy.OnPixelCopyFinishedListener
    ): Bitmap {
        return viewShotAfterOreo(view.window.decorView, scale, listener)
    }

    /**
     * 通过PixelCopy API 从一个view创建Bitmap。
     * 注意点：绘制之前要清掉 View 的焦点，因为焦点可能会改变一个 View 的 UI 状态。
     *
     * 如果View是Activity的根布局，则生成的Bitmap包含状态栏，但是又没有状态栏的图标，这个比较奇怪。
     *
     * @param view  target view
     * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun viewShotAfterOreo(
        view: View,
        @FloatRange(from = 0.0, to = 1.0) scale: Float = 1.0F,
        listener: PixelCopy.OnPixelCopyFinishedListener
    ): Bitmap {
        if(view.context !is Activity){
            throw RuntimeException("View must be in activity.")
        }
        //检查是否是ImageView
        if (view is ImageView) {
            val drawable: Drawable = view.drawable
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
        }
        val bitmap: Bitmap = Bitmap.createBitmap(
            (view.width * scale).toInt(),
            (view.height * scale).toInt(),
            Bitmap.Config.ARGB_8888
        )
        view.clearFocus()

        synchronized(sCanvas) {
            //>= 8.0
            val srcRect = Rect()
            srcRect.set(0, 0, bitmap.width, bitmap.height)
            if(view is SurfaceView){
                PixelCopy.request(view, srcRect,bitmap, listener,Handler(Looper.getMainLooper()))
            } else{
                PixelCopy.request((view.context as Activity).window,
                  srcRect,bitmap, listener,Handler(Looper.getMainLooper()))
            }

        }
        return bitmap
    }

    /**
     * 主要通过软件渲染的方式获取视图副本bitmap，
     * 以下方式使用view.draw(Canvas)是安卓官方推荐的,
     * 但是这些软件渲染的用法是不被鼓励的，并且与硬件渲染特性(如Config)存在兼容性问题。
     * 所以此方式效果并不是完全的好.
     */
    fun viewShotBeforOreoV2(view: View, @FloatRange(from = 0.0, to = 1.0) scale: Float = 1.0F): Bitmap {
        //检查是否是ImageView
        if (view is ImageView) {
            val drawable: Drawable = view.drawable
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
        }
        view.clearFocus()
        val bitmap: Bitmap = Bitmap.createBitmap(
            (view.width * scale).toInt(),
            (view.height * scale).toInt(),
            Bitmap.Config.ARGB_8888
        )
        synchronized(sCanvas) {
            //
            val canvas: Canvas = sCanvas
            canvas.setBitmap(bitmap)
            canvas.save()
            // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
            canvas.drawColor(Color.WHITE)
            canvas.scale(scale, scale)
            view.draw(canvas) //手动将view(及其所有子视图)呈现到给定的Canvas
            canvas.restore()
            canvas.setBitmap(null)
        }
        return bitmap
    }

    /**
     * 通过视图绘制缓存获取Bitmap;
     * 但是, API 11中硬件加速渲染的引入，视图绘制缓存在很大程度上已经过时了.
     * 在使用硬件加速时，中间缓存层基本上是不必要的，并且由于创建和更新该层的成本，很容易导致性能的净损失。
     * 在极少数的情况下，缓存层是有用的，比如alpha动画。
     *
     */
    fun viewShotWithDrawCache(view: View, @FloatRange(from = 0.0, to = 1.0) scale: Float = 1.0F): Bitmap {
        //检查是否是ImageView
        if (view is ImageView) {
            val drawable: Drawable = view.drawable
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        return view.drawingCache
    }

    // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="图片储存API">
        /**
     * 保存图片到指定路径
     * @param context
     * @param bitmap   要保存的图片
     * @param name 自定义图片名称
     * @param successTips 成功提示文本
     * @param isStandardDir 是否是常规设备标准的相册目录
     *        即: /DCIM/Camera/   Meizu 、Oppo有的是/DCIM/
     */
    fun saveImageToGallery(context: Context, bitmap: Bitmap, name: String,successTips:String = "",
                           isStandardDir:Boolean = true) {
        var fileName = name
        val format: DateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        fileName = fileName + format.format(Date()).toString() + ".JPEG"
        val storePath = if(isStandardDir){
            Environment.getExternalStorageDirectory().path +"/DCIM/Camera/"
        }else{
            Environment.getExternalStorageDirectory().path +"/DCIM/"
        }
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val file = File(appDir, fileName)
        try {
            if(file.exists()){
                file.delete()
            }
            val fos = FileOutputStream(file)
            //通过io流的方式来压缩保存图片
            //格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            val isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()
            // 插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, fileName, null)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            Log.d("UIUtils","发送广播通知系统图库刷新数据")
            val uri = Uri.fromFile(file)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            if (isSuccess) {
                Toast.makeText(context, "图片已保存至$file", Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, successTips, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
  // </editor-fold>
}