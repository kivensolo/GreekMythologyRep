package com.kingz.base.viewbinding

import androidx.viewbinding.ViewBinding
import com.kingz.base.BaseSimpleActivity

/**
 * author：ZekeWang
 * date：2023/7/20
 * description：binding相关的便捷工具
 */
inline fun <reified VB:ViewBinding> BaseSimpleActivity.binding(setContentView:Boolean = true) =
    lazy {
//        inflate
    }
