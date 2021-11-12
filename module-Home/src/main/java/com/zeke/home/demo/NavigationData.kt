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
        DemoGroup("File",
            samples = arrayListOf(
                DemoSample("DownloadFile", RouterConfig.PAGE_DEMO_WORK_MANAGER),
                DemoSample("DownloadFile2", RouterConfig.PAGE_DEMO_WORK_MANAGER)
            )
        ),
        DemoGroup("Images & Graphics",
            samples = arrayListOf(
                DemoSample("Palette", RouterConfig.PAGE_PALETTE_DEMO),
                DemoSample("ColorMatrix", RouterConfig.PAGE_COLOR_MATRIX),
                DemoSample("PixelCopy", RouterConfig.PAGE_PIXEL_COPY)
            )
        )
    )


}