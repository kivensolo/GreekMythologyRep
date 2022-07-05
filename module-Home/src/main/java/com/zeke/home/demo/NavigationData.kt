package com.zeke.home.demo

import com.kingz.module.common.router.RouterConfig
import com.zeke.home.entity.DemoGroup
import com.zeke.home.entity.DemoSample

/**
 * author：ZekeWang
 * date：2021/11/6
 * description：首页推荐页面Demo模块分组展示数据
 */
class NavigationData {
    var groupList:MutableList<DemoGroup> = mutableListOf(
        DemoGroup("基础控件&组件","原生基本控件",
            samples = arrayListOf(
                DemoSample("Four major components", "com.kingz.widgets.android_src.FourComponentPage"),
                DemoSample("BasicControls", "com.kingz.widgets.android_src.BasicControlsActivity"),
                DemoSample("ProgressBar", "com.kingz.widgets.android_src.NativeProgressBar"),
                DemoSample("SrcLayout", "com.kingz.widgets.android_src.LayoutPage"),
                DemoSample("ViewPager", "com.kingz.widgets.android_src.OriginViewPager"),
                DemoSample("WebView", "com.kingz.view.webview.WebViewActivity"),
                DemoSample("ToolBar", RouterConfig.PAGE_TOOLBAR),
                DemoSample("Menu", "com.zeke.demo.menu.app.MenuMainActivity"),
                DemoSample("SurfaceDraw", "com.kingz.widgets.android_src.DrawRectWithSurface"),
                DemoSample("Fragment Lifecycle", "com.zeke.demo.fragments.FragementLifeCycleTestPage"),
                DemoSample("WebView", "com.kingz.view.webview.WebViewActivity"),
                DemoSample("RecyclerView_Drag", "com.kingz.recyclerview.MusicPosterPages")
            )
        ),
        DemoGroup("Custom view & widgets","自定义View绘制练习& 控件",
            samples = arrayListOf(
                DemoSample("旧版自定义View集合页面", "com.kingz.pages.CustomViewsPage"),
                DemoSample("新版自定义View展示页面", "com.zeke.demo.customview.CustomViewsDemoActivity"),
                DemoSample("ShadowLayout展示", "com.zeke.demo.shadowlayout.ShadowLayoutDemoActivity"),
                //被CustomViewsDemoActivity取代
                DemoSample("Davinci", "com.zeke.demo.draw.PracticeDrawActivity"),
                // 可以合并到 PracticeDrawActivity 中去
                DemoSample("PracticeLayout(TODO)", "com.zeke.demo.draw.PracticeLayoutActivity"),
            )
        ),
        DemoGroup("文字","文字处理相关",
            samples = arrayListOf(
                DemoSample("TextViewOfLanguages", "com.kingz.text.langs.TextViewOfLanguages"),
                DemoSample("LabelText", "com.kingz.text.LabelTextViewPage"),
                DemoSample("SpanLable", "com.kingz.text.metrics.SpansDemo")
            )
        ),
        DemoGroup("Animation","动画",
            arrayListOf(
                DemoSample("Property Animation", "com.kingz.view.animation.PropertyAnimationsActivity"),
                DemoSample("ViewFlipper Animation", "com.kingz.view.animation.ViewFlipperAnimation"),
                DemoSample("Interpolator Animation", "com.kingz.view.animation.InterpolatorAnimation"),
                DemoSample("Crossfading Two Views", "com.kingz.view.animation.CrossfadeActivity"),
            )
        ),

        DemoGroup("File","文件相关",
            samples = arrayListOf(
                DemoSample("File Test",     "com.kingz.file.FileAndPicTestACT"),
                DemoSample("DownloadFile",      RouterConfig.PAGE_DEMO_WORK_MANAGER),
                DemoSample("ExternalStorage", "com.apiDemo.content.ExternalStorage"),
            )
        ),
        DemoGroup("Images & Graphics","图像相关",
            samples = arrayListOf(
                DemoSample("Arcs",          RouterConfig.PAGE_GRAPHIC_ARCS),
                DemoSample("BitMapOverall", RouterConfig.PAGE_GRAPHIC_BITMAP_OVERALL),
                DemoSample("Palette",       RouterConfig.PAGE_PALETTE_DEMO),
                DemoSample("PixelCopy",     RouterConfig.PAGE_PIXEL_COPY),
                DemoSample("PathDemo",     "com.kingz.graphics.PathDemoActivity"),
                DemoSample("MemoryCheck(无效)",     "com.kingz.pages.photo.memory.MemoryCheck"),
                DemoSample("Glide",    "com.zeke.demo.glide.GlideCardDemoActivity"),
                DemoSample("Blur",     RouterConfig.PAGE_BLUR_LIST),
            )
        ),
        DemoGroup("About Color","颜色",
            samples = arrayListOf(
                DemoSample("Color World",   RouterConfig.PAGE_COLOR_DEMO),
                DemoSample("NewFeature of UI", RouterConfig.PAGE_NEW_FEATURES)
            )
        ),
        DemoGroup("Player","颜色",
            samples = arrayListOf(
                DemoSample("MediaPlayer",   "com.mplayer.ApolloMediaPlayer"),
                DemoSample("Detail-With-ExoPlayer", "com.zeke.music.activities.MusicDetailPageActivty"),
                DemoSample("IJK SampleList(Not ready)", "com.zeke.play.activities.IJKSampleMediaActivity"),
                DemoSample("ZPlayerView Test", "com.zeke.play.activities.ZPlayerViewTest"),
            )
        ),
        DemoGroup("Meterial Design","MD设计",
            samples = arrayListOf(
                DemoSample("AppBarLayout效果(TODO)", RouterConfig.PAGE_APPBAR_DEMO),
                DemoSample("沉浸式效果测试", RouterConfig.PAGE_IMMERSION)
            )
        ),
        DemoGroup("NewFeatures","新特性",
            samples = arrayListOf(
                DemoSample("NewFeature of UI", RouterConfig.PAGE_NEW_FEATURES),
                DemoSample("Palette", RouterConfig.PAGE_PALETTE_DEMO)
            )
        ),
        DemoGroup("Architecture Components","JetPack",
            samples = arrayListOf(
                DemoSample("Paging3 Demo", "com.zeke.demo.jetpack.paging.PagingDemoActivity"),
                DemoSample("协程练习", "com.zeke.ktx.modules.aac.CoroutineTestActivity"),
                DemoSample("ViewModel Demo", "com.zeke.ktx.modules.aac.ViewModelDemoActivity"),
                DemoSample("Shared ViewModel Demo", "com.zeke.ktx.modules.aac.sharedvm.SharedFirstVMActivity"),
            )
        )
    )


}