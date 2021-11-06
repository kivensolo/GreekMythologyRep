package com.zeke.eyepetizer.videodetail

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.bean.MediaParams
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.common.utils.image.GlideLoader
import com.zeke.eyepetizer.adapter.VideoPlayAdapter
import com.zeke.eyepetizer.bean.Item
import com.zeke.eyepetizer.bean.VideoDetailMergeData
import com.zeke.eyepetizer.bean.cards.item.VideoSmallCard
import com.zeke.eyepetizer.viewmodel.EyepetizerViewModel
import com.zeke.moudle_eyepetizer.R
import com.zeke.moudle_eyepetizer.databinding.ActivityDetailVideoBinding
import kotlinx.android.synthetic.main.activity_detail_video.*

/**
 * author：ZekeWang
 * date：2021/5/28
 * description：开眼视频带播放器的详情页
 *
 * 【增加画中画模式】
 * https://developer.android.google.cn/guide/topics/ui/picture-in-picture?hl=zh_cn#kotlin
 * New Feat:
 * 在应用使用画中画之前，请务必通过调用
 * hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
 * 进行检查以确保可以使用画中画。
 *
 * NOTES:
 * 当 Activity 进入画中画模式后，默认不会获得输入焦点。
 * 要在画中画模式下接收输入事件，请使用 MediaSession.setCallback()
 */


/** Intent action for stopwatch controls from Picture-in-Picture mode.  */
private const val ACTION_STOPWATCH_CONTROL = "stopwatch_control"
/** Intent extra for stopwatch controls from Picture-in-Picture mode.  */
private const val EXTRA_CONTROL_TYPE = "control_type"

private const val CONTROL_TYPE_CLEAR = 1
private const val CONTROL_TYPE_START_OR_PAUSE = 2

private const val REQUEST_CLEAR = 3
private const val REQUEST_START_OR_PAUSE = 4


@Route(path = RouterConfig.PAGE_EYE_DETAIL)
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

    //是否在画中画模式
    private var isInPIPMode = false
    private lateinit var binding: ActivityDetailVideoBinding

    /**
     * A [BroadcastReceiver] for handling action items on the picture-in-picture mode.
     */
    private val broadcastReceiver = object : BroadcastReceiver() {

        // Called when an item is clicked.
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null || intent.action != ACTION_STOPWATCH_CONTROL) {
                return
            }
            when (intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)) {
//                CONTROL_TYPE_START_OR_PAUSE -> viewModel.startOrPause()
//                CONTROL_TYPE_CLEAR -> viewModel.clear()
            }
        }
    }

    override fun getContentView(): View? {
        binding = ActivityDetailVideoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mAdapter = VideoPlayAdapter()
        // 列表初始化
        with(binding.videoRecycler){
            layoutManager = LinearLayoutManager(context)
            visibility = View.GONE
            adapter = mAdapter.apply {
                onRelatedItemClick = { reloadMediaInfo(it)}
            }
        }
        // 处理来自图中图模式的动作图标的事件
        registerReceiver(broadcastReceiver, IntentFilter(ACTION_STOPWATCH_CONTROL))
    }

    override fun initViewModel() {
        super.initViewModel()

        viewModel.detailPageLiveData.observe(this, Observer { result ->
//            ZLog.d("Video detail page recom data is ok :$it")
            mPageData = result
            if (result != null) {
                binding.videoRecycler.visibility = View.VISIBLE
                mAdapter.setData(result.detailData, result.relatedData.itemList)
            }
        })
    }

    private fun parseIntent() {
        mediaParams = intent.getParcelableExtra(MediaParams.PARAMS_KEY)
    }

    override fun initData(savedInstanceState: Bundle?) {
        parseIntent()
        with(binding.videoPlayer) {
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

        with(binding.videoPlayer) {
            stop()
            setDataSource(mediaParams!!)
            open()
            //TODO 切换播放后，画面最后一帧残留
        }

        //刷新列表前重置状态
        binding.videoRecycler.visibility = View.GONE
        binding.videoRecycler.scrollToPosition(0)

        //获取数据
        viewModel.getVideoDetailAndReleatedData(videoData.id)
    }


    /**
     * 当 Activity 由于用户选择而即将进入后台时，作为 Activity 生命周期的一部分调用。
     * 例如，当用户按下 Home 键时，onUserLeaveHint()会被调用，
     * 但是当有来电导致通话中的Activity被自动带到前台时，
     * onUserLeaveHint()在被中断的 Activity 上不会被调用。
     * 在调用它的情况下，此方法在活动的onPause()回调之前被调用。
     */
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            isInPIPMode = true

//            setPictureInPictureParams(pipParams)
//            enterPictureInPictureMode(pipParams)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePictureInPictureParams(started: Boolean): PictureInPictureParams {
        //PictureInPictureParams指定自定义操作
        val visibleRect = Rect()
        binding.videoPlayer.getGlobalVisibleRect(visibleRect)
        val pipParams = PictureInPictureParams.Builder()
            // Set action items for the picture-in-picture mode. These are the only custom controls
            // available during the picture-in-picture mode.
            .setActions(
                listOf(
                    // "Clear" action.
                    if (started) {
                        // "Pause" action when the stopwatch is already started.
                        createRemoteAction(
                            R.drawable.exo_icon_pause,
                            R.string.collect_failed,
                            REQUEST_START_OR_PAUSE,
                            CONTROL_TYPE_START_OR_PAUSE
                        )
                    } else {
                        // "Start" action when the stopwatch is not started.
                        createRemoteAction(
                            R.drawable.exo_icon_play,
                            R.string.collect_failed,
                            REQUEST_START_OR_PAUSE,
                            CONTROL_TYPE_START_OR_PAUSE
                        )
                    }
                )
            )
            .setAspectRatio(Rational(16, 9))
            // Specify the portion of the screen that turns into the picture-in-picture mode.
            // This makes the transition animation smoother.
            .setSourceRectHint(visibleRect)
            // Turn the screen into the picture-in-picture mode if it's hidden by the "Home" button.
//            .setAutoEnterEnabled(true)
            // Disables the seamless resize. The seamless resize works great for videos where the
            // content can be arbitrarily scaled, but you can disable this for non-video content so
            // that the picture-in-picture mode is resized with a cross fade animation.
//            .setSeamlessResizeEnabled(false) //API30
            .build()
        return pipParams
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        binding.videoRecycler.visibility = if (isInPictureInPictureMode) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        isInPIPMode = false
    }

    /**
     * 在onStop中停止播放器，如果 非要在onPause中处理，
     * 则需要进行isInPictureInPictureMode判断
     */
    override fun onStop() {
        super.onStop()
        binding.videoPlayer.apply {
           pause()
           stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoPlayer.release()
    }


    /**
     * Creates a [RemoteAction]. It is used as an action icon
     * on the overlay of the picture-in-picture mode.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRemoteAction(
          @DrawableRes iconResId: Int,
          @StringRes titleResId: Int,
          requestCode: Int,
          controlType: Int
    ): RemoteAction {
        return RemoteAction(Icon.createWithResource(this, iconResId),
            getString(titleResId), getString(titleResId),
            PendingIntent.getBroadcast(
                this,requestCode,
                Intent(ACTION_STOPWATCH_CONTROL).putExtra(EXTRA_CONTROL_TYPE, controlType),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}