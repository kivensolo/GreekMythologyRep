package com.kingz.module.common.router

/**
 * ARouter配置
 */
object RouterConfig {
    //App模块
    const val PAGE_DEMO_WORK_MANAGER = "/app/demo/work_manager"

// <editor-fold defaultstate="collapsed" desc="Home模块">
    const val PAGE_MAIN = "/home/main"
// </editor-fold>
    const val PAGE_LOGIN = "/module_Login/login"
    const val PAGE_WEB = "/module_Web/web"
    const val PAGE_DETAIL = "/module_MPlayer/detailPage"
    const val PAGE_PLAYER = "/module_MPlayer/playerPage"
    const val PAGE_IJK_DMEO = "/module_MPlayer/jikDemoPage"

    const val PAGE_MUSIC_DETAIL = "/module_Music/detailPage"
    const val PAGE_EYE_DETAIL = "/module_Eyepetizer/detailVideoPage"
    // 公共类型，带有appBar的页面
    const val PAGE_COMMON_APPBAR = "/module_Common/CommonAppBarPage"



    /*------------路由参数 Start---------------------*/
    const val PARAM_WEB_ARTICAL_INFO = "PARAM_WEB_ARTICAL"



}