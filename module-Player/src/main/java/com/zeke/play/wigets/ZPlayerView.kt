package com.zeke.play.wigets

import android.content.Context
import android.util.AttributeSet
import com.zeke.module_player.R

/**
 * author：ZekeWang
 * date：2021/6/5
 * description：
 * 对外提供的具备播放的播放器UI组件
 * 页面
 */
class ZPlayerView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0    // 注意这个attr的默认值,有的控件不一定是0
) :BasePlayView(mContext,attrs,defStyleAttr) {

    override fun getLayoutId(): Int = R.layout.player_view_controller_basic_new
}