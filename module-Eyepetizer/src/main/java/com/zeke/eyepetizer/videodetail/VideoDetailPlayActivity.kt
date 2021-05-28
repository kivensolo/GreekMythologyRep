package com.zeke.eyepetizer.videodetail

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.module.common.router.RPath
import com.kingz.module.common.utils.image.GlideLoader
import com.zeke.eyepetizer.bean.Data
import com.zeke.eyepetizer.bean.Item
import com.zeke.moudle_eyepetizer.R
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.android.synthetic.main.activity_detail_video.*

/**
 * author：ZekeWang
 * date：2021/5/28
 * description：开眼视频带播放器的详情页
 */
@Route(path = RPath.PAGE_EYE_DETAIL)
class VideoDetailPlayActivity : BaseVMActivity() {

    //static
    companion object {
        const val TAG = "VideoDetailPlayActivity"
    }

    //data from intent
    private lateinit var videoId: String                  //视频标志ID
    private lateinit var videoTitle: String               //视频标题
    private lateinit var videoPlayUrl: String             //视频播放地址Url
    private lateinit var videoFeedUrl: String             //视频封面地址Url
    private lateinit var blurredBackgroundUrl: String     //高斯模糊背景图片Url

    //data from net request
    private var mHeaderData: Data? = null
    private var mRelatedDataList: List<Item>? = null

    override val viewModel: BaseReactiveViewModel
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getContentLayout(): Int = R.layout.activity_detail_video

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        parseIntent()

        //背景图片加载
        GlideLoader.loadNetBitmap(this, blurredBackgroundUrl) {
            root.background = it
        }
        // 播放器初始化

        // 列表初始化
    }

    private fun parseIntent() {
        videoId = intent.getStringExtra("VIDEO_ID")
        videoTitle = intent.getStringExtra("VIDEO_TITLE")
        videoFeedUrl = intent.getStringExtra("VIDEO_FEED_URL")
        videoPlayUrl = intent.getStringExtra("VIDEO_PLAY_URL")
        blurredBackgroundUrl = intent.getStringExtra("VIDEO_BG")
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}