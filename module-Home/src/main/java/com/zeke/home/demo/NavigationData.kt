package com.zeke.home.demo

import com.kingz.module.common.router.RouterConfig
import com.zeke.home.entity.DemoGroup
import com.zeke.home.entity.DemoSample

/**
 * author：ZekeWang
 * date：2021/11/6
 * description：
 */
class NavigationData {
    var groupList:MutableList<DemoGroup> = mutableListOf()
    init {
        groupList.add(
            DemoGroup("File", samples = arrayListOf(
                    DemoSample("DownloadFile", RouterConfig.PAGE_DEMO_WORK_MANAGER),
                    DemoSample("DownloadFile2", RouterConfig.PAGE_DEMO_WORK_MANAGER)
                )
            )
        )
    }


}