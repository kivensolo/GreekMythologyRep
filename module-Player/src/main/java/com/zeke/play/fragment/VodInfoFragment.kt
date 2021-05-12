package com.zeke.play.fragment

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.base.BaseActivity
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.base.IPresenter
import com.zeke.kangaroo.utils.ZLog
import com.zeke.module_player.R
import com.zeke.play.VideoInfo
import com.zeke.play.activities.DetailPageActivty
import com.zeke.play.presenter.VodInfoPresenter
import com.zeke.play.view.IPlayerView

/**
 * author：KingZ
 * date：2019/7/30
 * description：影片简单样式详情的Fragment
 */
class VodInfoFragment : BaseFragment(), IPlayerView {
    private var vodInfoPresenter: VodInfoPresenter? = null
    private var mScrollView: ScrollView? = null
    //影片名字
    private var nameTv: TextView? = null
    //评分
    private var scoreTv: TextView? = null
    private var descTv: TextView? = null
    ////收藏按钮
    private var collectChk: CheckBox? = null
    private var videoInfo: VideoInfo? = null
    // 剧集
//    private EpisodeAdapter episodeAdapter;
    private var episodeRecyclerView: RecyclerView? = null

    override val isShown: Boolean
        get() = activity != null && (activity as BaseActivity).isActivityShow && isVisible
    override fun getLayoutId(): Int = R.layout.detailpage_vodinfo_layout

    override fun onCreateViewReady() {
        super.onCreateViewReady()
        initViews(rootView)
        onPresenterCreateView()
    }

    override fun onViewCreated() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vodInfoPresenter = VodInfoPresenter(this)
        vodInfoPresenter?.onCreate()
    }

    private fun initViews(rootView: View?) {
        if(rootView == null){
            return
        }
        mScrollView = rootView.findViewById(R.id.vod_detail_scrollview)
        nameTv = rootView.findViewById(R.id.tv_name_video)
        scoreTv = rootView.findViewById(R.id.tv_score_video)
        descTv = rootView.findViewById(R.id.tv_detail_video)
        rootView.findViewById<View>(R.id.iv_detail_arrow_more)
            .setOnClickListener(this)
        collectChk = rootView.findViewById(R.id.chk_collect)
        collectChk?.setOnClickListener(this)
        episodeRecyclerView = rootView.findViewById(R.id.episode_recycler)
        episodeRecyclerView?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        //TODo 进行剧集数据适配器数据绑定
//        episodeAdapter = new EpisodeAdapter(mediaParams);
//        episodeRecyclerView.setAdapter(episodeAdapter);
    }

    private fun onPresenterCreateView() {
        if (vodInfoPresenter != null) {
            vodInfoPresenter?.onCreateView()
        }
        showVideoInfo()
    }

    /**
     * 显示影片信息 (假数据)
     */
    private fun showVideoInfo() {
        videoInfo = VideoInfo()
        mScrollView?.visibility = View.VISIBLE
        nameTv?.text = videoInfo?.videoName
        scoreTv?.text = videoInfo?.userScore
        descTv?.text = videoInfo?.summary
        syncCollectState()
    }

    private fun syncCollectState() {
        collectChk?.isChecked = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (vodInfoPresenter != null) {
            vodInfoPresenter?.onDestroyView()
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.iv_detail_arrow_more) {
            if (activity != null) { //通过Activity来与Fragment交互
                (activity as DetailPageActivty?)?.showOrDismissVideoDetail(true, videoInfo)
            }
        } else if (id == R.id.episode_more) {
            ZLog.d(TAG, "onClick --- 更多剧集UI暂时未实现")
            //打开剧集Activity  REQUEST_EPISODE
//                Intent episodeIntent = new Intent(getContext(), EpisodeActivity.class);
//                episodeIntent.putExtra("MediaParams", mediaParams);
//                startActivityForResult(episodeIntent, REQUEST_EPISODE);
        } else if (id == R.id.chk_collect) {
            collect()
        }
    }

    private fun collect() {
        if (collectChk?.isChecked == true) {
            ZLog.d(TAG, "onClick ---取消收藏")
        } else {
            ZLog.d(TAG, "onClick ---添加收藏")
        }
    }

    override fun setPresenter(presenter: IPresenter) {}
    override fun showLoading() { //do showLoadding
    }

    override fun hideLoading() {}
    override fun showError(listener: View.OnClickListener?) {}
    override fun showEmpty(listener: View.OnClickListener?) {}

    override fun showMessage(tips: String?) {}
    override fun getPlayView(): View? {
        return null
    }

    override fun showPlayLoadingTips(tips: String) {}
    override fun showPlayingView() {}
    override fun showPlayBufferTips() {}
    override fun dismissPlayBufferTips() {}
    override fun showPlayCompleteTips(tips: String) {}
    override fun showPlayErrorTips(tips: String) {}
    override fun updateTitleView() {}
    override fun showPlayStateView() {}
    override fun showPauseStateView() {}
    override fun showFlowTipsView() {}
    override fun switchVisibleState() {}
    override fun updatePlayProgressView(isDrag: Boolean, postion: Int) {}
    override fun repostControllersDismissTask(enable: Boolean) {}
    override fun dismissPop() {}
}