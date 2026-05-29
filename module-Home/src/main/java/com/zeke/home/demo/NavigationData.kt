package com.zeke.home.demo

import com.kingz.module.common.bean.DemoGroup
import com.kingz.module.common.bean.DemoSample
import com.kingz.module.common.router.RouterConfig

/**
 * author：ZekeWang
 * date：2021/11/6
 * description：首页推荐页面Demo模块分组展示数据
 */
class NavigationData {
    var groupList:MutableList<DemoGroup> = mutableListOf(
        DemoGroup("基础控件&组件","原生基本控件",
            samples = arrayListOf(
                DemoSample("Four major components", "FourComponents"),
                DemoSample("BasicControls", "BasicControls"),
                DemoSample("ProgressBar", "ProgressBar"),
                DemoSample("ViewPager", "ViewPager"),
                DemoSample("WebView", "WebView"),
                DemoSample("ToolBar", RouterConfig.PAGE_TOOLBAR),
                DemoSample("Menu", "Menu"),
                DemoSample("SurfaceDraw", "SurfaceDraw"),
                DemoSample("Fragment Lifecycle", "FragmentLifecycle"),
                DemoSample("WebView", "WebView"),
                DemoSample("RecyclerView_Drag", "RecyclerDrag")
            )
        ),
        DemoGroup("Custom view & widgets","自定义View绘制练习& 控件",
            samples = arrayListOf(
                DemoSample("旧版自定义View集合页面", "CustomView"),
                DemoSample("新版自定义View展示页面", "CustomViewNew"),
                DemoSample("ShadowLayout展示", "ShadowLayout"),
                DemoSample("Canvas API展示", "Canvas"),
                // 可以合并到 PracticeDrawActivity(Canvas) 中去
                DemoSample("PracticeLayout(TODO)", "PracticeLayout"),
                DemoSample("光带扫描效果", RouterConfig.PAGE_FLASH_SCAN),
            )
        ),
        DemoGroup("文字","文字处理相关",
            samples = arrayListOf(
                DemoSample("TextViewOfLanguages", "TextLanguages"),
                DemoSample("LabelText", "LabelText"),
                DemoSample("SpanLable", "SpansDemo")
            )
        ),
        DemoGroup("Animation","动画",
            arrayListOf(
                DemoSample("Property Animation", "PropertyAnimation"),
                DemoSample("ViewFlipper Animation", "ViewFlipper"),
                DemoSample("Interpolator Animation", "Interpolator"),
            )
        ),

        DemoGroup("File","文件相关",
            samples = arrayListOf(
                DemoSample("File Test",     "FileTest"),
                DemoSample("DownloadFile",      RouterConfig.PAGE_DEMO_WORK_MANAGER),
                DemoSample("ExternalStorage", "ExternalStorage"),
            )
        ),
        DemoGroup("Images & Graphics","图像相关",
            samples = arrayListOf(
                DemoSample("Arcs",          RouterConfig.PAGE_GRAPHIC_ARCS),
                DemoSample("BitMapOverall", RouterConfig.PAGE_GRAPHIC_BITMAP_OVERALL),
                DemoSample("Palette",       RouterConfig.PAGE_PALETTE_DEMO),
                DemoSample("PixelCopy",     RouterConfig.PAGE_PIXEL_COPY),
                DemoSample("PathDemo",     "PathDemo"),
                DemoSample("Glide",    "GlideDemo"),
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
                DemoSample("MediaPlayer",   "MediaPlayer"),
                DemoSample("Detail-With-ExoPlayer（无效路径）", "com.zeke.music.activities.MusicDetailPageActivty"),
                DemoSample("IJK SampleList(Not ready)", "IJKSampleList"),
                DemoSample("ZPlayerView Test", "ZPlayerViewTest"),
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
                DemoSample("Paging3 Demo", "Paging3"),
                DemoSample("协程练习", "CoroutineTest"),
                DemoSample("ViewModel Demo", "ViewModel"),
                DemoSample("Shared ViewModel Demo", "Shared ViewModel"),
            )
        )
    )


}
