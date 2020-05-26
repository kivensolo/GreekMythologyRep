package com.kingz.module.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * author: King.Z <br>
 * date:  2020/5/24 13:02 <br>
 * description: Adapter抽象代理 <br>
 */
interface IDelegateAdapter<T> {

    // 查找委托时调用的方法，返回自己能处理的类型即可。
    fun isForViewType(dataType:T):Boolean

    // 用于委托Adapter的onCreateViewHolder方法
    fun onCreateViewHolder(parent: ViewGroup, viewType:Int): RecyclerView.ViewHolder

    // 用于委托Adapter的onBindViewHolder方法
    fun onBindViewHolder(holder: RecyclerView.ViewHolder , position:Int ,dataType: T)

}