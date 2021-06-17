package com.zeke.eyepetizer.videodetail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.bean.MediaParams
import com.kingz.module.common.router.RPath
import com.kingz.module.common.utils.image.GlideLoader
import com.zeke.eyepetizer.adapter.VideoPlayAdapter
import com.zeke.eyepetizer.bean.Item
import com.zeke.eyepetizer.bean.VideoDetailMergeData
import com.zeke.eyepetizer.bean.cards.item.VideoSmallCard
import com.zeke.eyepetizer.viewmodel.EyepetizerViewModel
import com.zeke.moudle_eyepetizer.R
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

    //视频参数
    private var mediaParams: MediaParams? = null

    //data from net request
    private var mPageData: VideoDetailMergeData? = null
    private lateinit var mAdapter: VideoPlayAdapter

    override val viewModel: EyepetizerViewModel by viewModels {
        ViewModelFactory.build { EyepetizerViewModel() }
    }

    override fun getContentLayout(): Int = R.layout.activity_detail_video

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mAdapter = VideoPlayAdapter()
        // 列表初始化
        with(videoRecycler){
            layoutManager = LinearLayoutManager(context)
            visibility = View.GONE
            adapter = mAdapter.apply {
                onRelatedItemClick = { reloadMediaInfo(it)}
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()

        viewModel.detailPageLiveData.observe(this, Observer { result ->
//            ZLog.d("Video detail page recom data is ok :$it")
            mPageData = result
            if(result != null){
                videoRecycler.visibility = View.VISIBLE
                mAdapter.setData(result.detailData, result.relatedData.itemList)
            }
        })
    }

    private fun parseIntent() {
        mediaParams = intent.getParcelableExtra(MediaParams.PARAMS_KEY)
    }

    override fun initData(savedInstanceState: Bundle?) {
        parseIntent()
        with(videoPlayer) {
            setDataSource(mediaParams!!)
            open()
        }

        viewModel.getVideoDetailAndReleatedData(mediaParams?.videoId!!)

        //背景图片加载
        GlideLoader.loadNetBitmap(this, mediaParams?.videoBkg ?: "") {
            root.background = it
        }
    }

    /**
     *
     */
    private fun reloadMediaInfo(item: Item) {

        //init data
        val jsonObject = item.data
        val videoData = Gson().fromJson(jsonObject, VideoSmallCard::class.java)

        GlideLoader.loadNetBitmap(this, videoData.cover.blurred) { root.background = it }      //整个页面背景图片
        //播放器缩略图
//        GlideLoader.loadNetImage(this, videoPlayer.thumbImageView, videoData.cover.detail)   //视频封面

        //改变播放视频Url
        mediaParams?.apply {
            this.videoName = videoData.title
            this.videoUrl = videoData.playUrl
            this.videoId = videoData.id
            this.videoType = videoData.type
        }

        with(videoPlayer) {
            stop()
            setDataSource(mediaParams!!)
            open()
            //TODO 切换播放后，画面最后一帧残留
        }

        //刷新列表前重置状态
        videoRecycler.visibility = View.GONE
        videoRecycler.scrollToPosition(0)

        //获取数据
        viewModel.getVideoDetailAndReleatedData(videoData.id)
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.pause()
        videoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.release()
    }
}