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
        DemoGroup("基础控件","原生基本控件",
            samples = arrayListOf(
                DemoSample("BasicControls", "com.kingz.widgets.android_src.BasicControlsActivity"),
                DemoSample("ProgressBar", "com.kingz.widgets.android_src.NativeProgressBar"),
                DemoSample("SrcLayout", "com.kingz.widgets.android_src.LayoutPage"),
                DemoSample("ViewPager", "com.kingz.widgets.android_src.OriginViewPager"),
                DemoSample("WebView", "com.kingz.view.webview.WebViewActivity"),
                DemoSample("ToolBar", RouterConfig.PAGE_TOOLBAR)
            )
        ),
        DemoGroup("RecyclerView","RecyclerView",
            samples = arrayListOf(
                DemoSample("DragRecyclerView", "com.kingz.recyclerview.MusicPosterPages"),
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
                DemoSample("PixelCopy",     RouterConfig.PAGE_PIXEL_COPY)
            )
        ),
        DemoGroup("About Color","颜色",
            samples = arrayListOf(
                DemoSample("Color World",   RouterConfig.PAGE_COLOR_DEMO),
                DemoSample("NewFeature of UI", RouterConfig.PAGE_NEW_FEATURES)
            )
        ),
        DemoGroup("Meterial Design","MD设计",
            samples = arrayListOf(
                DemoSample("AppBarLayout效果", RouterConfig.PAGE_APPBAR_DEMO),
                DemoSample("沉浸式效果测试", RouterConfig.PAGE_PALETTE_DEMO)
            )
        ),
        DemoGroup("NewFeatures","新特性",
            samples = arrayListOf(
                DemoSample("NewFeature of UI", RouterConfig.PAGE_NEW_FEATURES),
                DemoSample("Palette", RouterConfig.PAGE_PALETTE_DEMO)
            )
        )
    )


}