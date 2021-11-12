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
                DemoSample("ColorMatrix",   RouterConfig.PAGE_COLOR_MATRIX),
                DemoSample("Palette",       RouterConfig.PAGE_PALETTE_DEMO),
                DemoSample("PixelCopy",     RouterConfig.PAGE_PIXEL_COPY)
            )
        ),
        DemoGroup("NewFeatures","新特性",
            samples = arrayListOf(
                DemoSample("NewFeature of UI", RouterConfig.PAGE_PALETTE_DEMO)
            )
        )
    )


}