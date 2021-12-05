package com.zeke.demo.color

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.wanandroid.activity.AppBarActivity

/**
 * author: King.Z <br></br>
 * date:  2017/5/26 22:01 <br></br>
 *
 * ARGB 色值采用16进制，取值在 0 － 255 之间 ，0（0x00） 即 完全没有 ，255(0xff) 代表满值;
 *
 * setColorFilter(ColorFilter filter):设置颜色过滤器,可以通过颜色过滤器过滤掉对应的色值，
 * 比如去掉照片颜色，生成老照片效果；ColorFilter有以下几个子类可用:
 *  ColorMatrixColorFilter
 *  LightingColorFilter
 *  PorterDuffColorFilter
 *  修改图片 RGBA 的值需要ColorMatrix类的支持，它定义了一个 4*5 的float[]类型的矩阵
 *  颜色矩阵M是以一维数组的方式进行存储的
 *  m=[a,b,c,d,e,  ----- 表示三原色中的红色
 *    f,g,h,i,j,  ----- 表示三原色中的绿色
 *    k,l,m,n,o,  ----- 表示三原色中的蓝色
 *
 *    p,q,r,s,t]  ----- 表示颜色的透明度
 *  第五列用于表示颜色的偏移量
 *
 *  参考文章:http://www.tuicool.com/articles/yYvEn2q
 */
@Route(path = RouterConfig.PAGE_COLOR_MATRIX)
class ColorMatrixActivity : AppBarActivity() {

    override fun initData(savedInstanceState: Bundle?) {
        defaultFragmentName = ColorMatrixFragment::class.java.name
        defaultFragmentTitle = "ColorMatrix"
        super.initData(savedInstanceState)
    }

}