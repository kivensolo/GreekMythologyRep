package com.zeke.music.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.zeke.moudle_music.R
import com.zeke.music.bean.VideoInfo
import com.zeke.music.viewmodel.MusicViewModel

/**
 * author：KingZE
 * date：2019/10/9
 * description：视频简介Fragment
 */
class VodDetailFragment : CommonFragment<MusicViewModel>() {
    //影片名字 评分
    private var nameScoreTextview: TextView? = null
    //最新，总集数
    private var newEpisodeTv: TextView? = null
    //演员
    private var actorTv: TextView? = null
    //导演
    private var directorTv: TextView? = null
    //影片简介
    private var detailTv: TextView? = null

    //视频id
    private var mVideoInfo: VideoInfo? = null

    companion object {
        const val DETAIL_ARG_KEY = "VideoInfo"
        @JvmStatic
        fun newInstance(videoInfo: VideoInfo?): VodDetailFragment {
            val vodDetailFragment = VodDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable(DETAIL_ARG_KEY, videoInfo)
            vodDetailFragment.arguments = bundle
            return vodDetailFragment
        }
    }

    override fun getLayoutResID(): Int = R.layout.fragment_vod_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        mVideoInfo = arguments?.get(DETAIL_ARG_KEY) as VideoInfo?
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        super.initView()
        nameScoreTextview = rootView?.findViewById(R.id.tv_name_video)
        newEpisodeTv = rootView?.findViewById(R.id.tv_episode_desc)
        actorTv = rootView?.findViewById(R.id.tv_actor)
        directorTv = rootView?.findViewById(R.id.tv_director)
        detailTv = rootView?.findViewById(R.id.video_detail)
        loadUI()
    }

    private fun loadUI() {
        if(mVideoInfo == null){
            return
        }
        val videoInfo = mVideoInfo
        val spannableString = SpannableString(videoInfo?.videoName + "   " + 7.8)
        val foregroundColorSpan =
            ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.accent_A200))
        spannableString.setSpan(
            foregroundColorSpan,
            (videoInfo?.videoName + "   ").length,
            spannableString.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        nameScoreTextview?.text = spannableString
        //剧集显示
//            newEpisodeTv!!.visibility = View.VISIBLE
//            newEpisodeTv!!.text = String.format(
//                context!!.getString(R.string.txt_tv_new),
//                (videoInfo!!.newEpisodeIndex + 1).toString(),
//                videoInfo!!.episodeCount.toString()
//            )
        //主演
        actorTv?.text = String.format(context!!.getString(R.string.txt_actor), videoInfo?.artistName)
        directorTv?.text =
            String.format(context!!.getString(R.string.txt_director), videoInfo?.artistName)
        detailTv?.text =
            String.format(context!!.getString(R.string.txt_detail), videoInfo?.videoDesc)
    }

    override val viewModel: MusicViewModel
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}