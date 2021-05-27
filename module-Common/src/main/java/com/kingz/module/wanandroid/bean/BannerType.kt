package com.kingz.module.wanandroid.bean

import androidx.annotation.IntDef

/**
 * author: King.Z <br>
 * date:  2021/1/30 11:29 <br>
 * description: Banner的视图类型注解类 <br>
 */
const val STYLE_PIC = 0
const val STYLE_VID= 1
const val STYLE_EYE_BANNER_CARD= 2

@IntDef(
    STYLE_VID,
    STYLE_PIC
)
annotation class BannerType
