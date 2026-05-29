package com.kingz.pages

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.apiDemo.content.ExternalStorage
import com.kingz.file.FileAndPicTestACT
import com.kingz.graphics.PathDemoActivity
import com.kingz.module.common.router.DemoPageRegistry
import com.kingz.recyclerview.MusicPosterPages
import com.kingz.text.LabelTextViewPage
import com.kingz.text.langs.TextViewOfLanguages
import com.kingz.view.animation.InterpolatorAnimation
import com.kingz.view.animation.PropertyAnimationsActivity
import com.kingz.view.animation.ViewFlipperAnimation
import com.kingz.view.surface.DrawRectWithSurface
import com.kingz.view.webview.WebViewActivity
import com.kingz.widgets.android_src.BasicControlsActivity
import com.kingz.widgets.android_src.NativeProgressBar
import com.kingz.widgets.android_src.OriginViewPager
import com.kingz.widgets.android_src.SpansDemo
import com.mplayer.ApolloMediaPlayer
import com.zeke.demo.ShadowLayoutDemoActivity
import com.zeke.demo.customview.CustomViewsDemoMultiCardActivity
import com.zeke.demo.draw.PracticeDrawActivity
import com.zeke.demo.draw.PracticeLayoutActivity
import com.zeke.demo.fragments.FragmentLifeCycleTestPage
import com.zeke.demo.glide.GlideCardDemoActivity
import com.zeke.demo.jetpack.paging.PagingDemoActivity
import com.zeke.demo.menu.app.MenuMainActivity
import com.zeke.ktx.modules.aac.CoroutineTestActivity
import com.zeke.ktx.modules.aac.ViewModelDemoActivity
import com.zeke.ktx.modules.aac.sharedvm.SharedFirstVMActivity
import com.zeke.play.activities.IJKSampleMediaActivity
import com.zeke.play.activities.ZPlayerViewTestPage

/**
 * Demo页面路径自注册配置
 *
 * 通过 ContentProvider 在应用启动时自动初始化，
 * 将各模块的 Demo Activity 类路径注册到 DemoPageRegistry。
 *
 * 后续需要慢慢用Arouter做迁移
 */
object DemoPageConfig {

    fun registerAll() {
        // ===== app 模块 =====

        // 基础控件
        DemoPageRegistry.register("FourComponents",    FourComponentPage::class.java)
        DemoPageRegistry.register("BasicControls",      BasicControlsActivity::class.java)
        DemoPageRegistry.register("ProgressBar",      NativeProgressBar::class.java)
        DemoPageRegistry.register("ViewPager",         OriginViewPager::class.java)
        DemoPageRegistry.register("Menu",              MenuMainActivity::class.java)
        DemoPageRegistry.register("SurfaceDraw",       DrawRectWithSurface::class.java)
        DemoPageRegistry.register("FragmentLifecycle",       FragmentLifeCycleTestPage::class.java)
        DemoPageRegistry.register("WebView",           WebViewActivity::class.java)
        DemoPageRegistry.register("RecyclerDrag",      MusicPosterPages::class.java)

        // 自定义View
        DemoPageRegistry.register("CustomView",         CustomViewsPage::class.java)
        DemoPageRegistry.register("CustomViewNew",      CustomViewsDemoMultiCardActivity::class.java)
        DemoPageRegistry.register("ShadowLayout",       ShadowLayoutDemoActivity::class.java)
        DemoPageRegistry.register("Canvas",             PracticeDrawActivity::class.java)
        DemoPageRegistry.register("PracticeLayout",     PracticeLayoutActivity::class.java)
        DemoPageRegistry.register("BookReader",         PracticeLayoutActivity::class.java)

        // 文字
        DemoPageRegistry.register("TextLanguages",     TextViewOfLanguages::class.java)
        DemoPageRegistry.register("LabelText",         LabelTextViewPage::class.java)
        DemoPageRegistry.register("SpansDemo",         SpansDemo::class.java)

        // 动画
        DemoPageRegistry.register("PropertyAnimation", PropertyAnimationsActivity::class.java)
        DemoPageRegistry.register("ViewFlipper",       ViewFlipperAnimation::class.java)
        DemoPageRegistry.register("Interpolator",      InterpolatorAnimation::class.java)

        // 文件
        DemoPageRegistry.register("FileTest",          FileAndPicTestACT::class.java)
        DemoPageRegistry.register("ExternalStorage",   ExternalStorage::class.java)

        // 图像
        DemoPageRegistry.register("PathDemo",           PathDemoActivity::class.java)
        DemoPageRegistry.register("GlideDemo",          GlideCardDemoActivity::class.java)

        // 播放器
        DemoPageRegistry.register("MediaPlayer",        ApolloMediaPlayer::class.java)
        DemoPageRegistry.register("IJKSampleList",      IJKSampleMediaActivity::class.java)
        DemoPageRegistry.register("ZPlayerViewTest",    ZPlayerViewTestPage::class.java)

        // JetPack
        DemoPageRegistry.register("Paging3",            PagingDemoActivity::class.java)
        DemoPageRegistry.register("CoroutineTest",      CoroutineTestActivity::class.java)
        DemoPageRegistry.register("ViewModel",          ViewModelDemoActivity::class.java)
        DemoPageRegistry.register("Shared ViewModel",   SharedFirstVMActivity::class.java)
    }
}

/**
 * 用于自动初始化 DemoPageConfig 的 ContentProvider
 */
class DemoPageInitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        DemoPageConfig.registerAll()
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
