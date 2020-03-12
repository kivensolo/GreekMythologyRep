package com.zeke.ktx.activity.glide

import android.os.Bundle
import android.widget.LinearLayout
import com.kingz.customdemo.R
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.ktx.activity.practice_draw.Practice2DrawCircleView
import com.zeke.ktx.base.BaseFragment

/**
 * author：KingZ
 * date：2020/2/22
 * description：练习绘制页面的Fragment
 */
class GlideDemoFragment : BaseFragment() {

    var pageId: String = ""
    override fun getLayoutId(): Int {
        return R.layout.fragment_practice_draw
    }

    companion object{
        fun newInstance(page_id: String): GlideDemoFragment {
            val fragment = GlideDemoFragment()
            val args = Bundle()
            args.putString("page_id", page_id)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            pageId = args.getString("page_id","")
        }
    }

    override fun onCreateViewReady() {
        super.onCreateViewReady()
        val contentLayout = rootView.findViewById(R.id.content_practice) as LinearLayout
        when(pageId){
            getString(R.string.glide_circle) -> contentLayout.addView(GlideCircleView(context!!))
            getString(R.string.glide_round) -> contentLayout.addView(Practice2DrawCircleView(context!!))
            getString(R.string.glide_round_with_border) -> ToastUtils.show(context,"此效果还未实现")
            getString(R.string.glide_blur) -> ToastUtils.show(context,"此效果还未实现")
            getString(R.string.glide_load_fade) -> ToastUtils.show(context,"此效果还未实现")
        }
    }

    override fun initView() {

    }

    override fun initData() {
    }

}