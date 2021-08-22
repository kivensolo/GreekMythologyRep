package com.zeke.demo.meterial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.zeke.demo.R
import kotlinx.android.synthetic.main.activity_appbarlayout_demo.*

/**
 * author：ZekeWang
 * date：2021/2/28
 * description：
 *  沉浸式Demo测试
 *
 *  - View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
 *      Activity全屏显示，但导航栏不会被隐藏覆盖，导航栏依然可见，Activity底部布局部分会被导航栏遮住(布局入侵)
 *  - View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
 *      Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
 *  - View.SYSTEM_UI_FLAG_LAYOUT_STABLE
 *      防止系统栏隐藏时内容区域大小发生变化
 */
class ImmersionBarDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appbarlayout_demo)

        transparent_status?.setOnClickListener {
            // 手动模式
            // <= 4.4
//            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            // >= 5.0
//            val flag = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//            window?.decorView?.systemUiVisibility = flag
//            window?.statusBarColor = Color.TRANSPARENT
//            window?.navigationBarColor = Color.TRANSPARENT

            // 引用第三方库
            ImmersionBar.with(this)
                .transparentBar() // 透明状态栏和导航栏
                .init()
        }

        hide_bars?.setOnClickListener {
            ImmersionBar.with(this)
                .fullScreen(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init()
        }

        change_bars_color?.setOnClickListener {
            // >= 5.0 设置状态栏颜色和Actionbar一样
//            window?.statusBarColor = Color.RED
//            window?.navigationBarColor = Color.RED

            ImmersionBar.with(this)
                .barColor(R.color.google_green)
                .init()
        }

        hide_navigation_only?.setOnClickListener {
            ImmersionBar.with(this)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init()
        }

        change_navigation_color.setOnClickListener {
            ImmersionBar.with(this)
                .fullScreen(false)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .navigationBarColor(R.color.google_yellow)
                .init()
        }

        reset_bars?.setOnClickListener {
            ImmersionBar.with(this)
                .fullScreen(false)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .barColor(R.color.black)
                .init()
        }

        // Light模式 ---- 在 Android 6.0 的以上，状态栏支持字体变灰色，Android 8.0 以上，导航栏支持导航按钮变灰色
        change_state_bar_text_color?.setOnClickListener {
            //手动调用
//            val flag = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                    or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
//            window?.decorView?.systemUiVisibility = flag
//            window?.statusBarColor = Color.TRANSPARENT
//            window?.navigationBarColor = Color.TRANSPARENT

            // 引用第三方库
            ImmersionBar.with(this)
                .fullScreen(true)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .barColor(R.color.transparent)
                .statusBarDarkFont(true) //目的是为了增加：SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                .navigationBarDarkIcon(true) //目的是为了填加：SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                .init()

        }
    }
}