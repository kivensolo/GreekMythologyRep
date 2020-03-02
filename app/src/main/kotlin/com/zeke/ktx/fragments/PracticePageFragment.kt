package com.zeke.ktx.fragments

import android.os.Bundle
import android.support.annotation.StringRes
import android.widget.LinearLayout
import com.kingz.customdemo.R
import com.zeke.ktx.activity.practice_draw.Practice1DrawColorView
import com.zeke.ktx.activity.practice_draw.Practice2DrawCircleView
import com.zeke.ktx.activity.practice_draw.Practice3DrawRectView
import com.zeke.ktx.activity.practice_draw.Practice4DrawPointView
import com.zeke.ktx.base.BaseFragment

/**
 * author：KingZ
 * date：2020/2/22
 * description：联系绘制页面的Fragment
 */
class PracticePageFragment : BaseFragment() {

    @StringRes
    var pageId: Int = 0
    override fun getLayoutId(): Int {
        return R.layout.fragment_practice_draw
    }

    companion object{
        fun newInstance(@StringRes page_id: Int): PracticePageFragment {
            val fragment = PracticePageFragment()
            val args = Bundle()
            args.putInt("page_id", page_id)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            pageId = args.getInt("page_id")
        }
    }

    override fun onCreateViewReady() {
        super.onCreateViewReady()
        val contentLayout = rootView.findViewById(R.id.content_practice) as LinearLayout
        when(pageId){
            R.string.title_1 -> contentLayout.addView(Practice1DrawColorView(context!!))
            R.string.title_2 -> contentLayout.addView(Practice2DrawCircleView(context!!))
            R.string.title_3 -> contentLayout.addView(Practice3DrawRectView(context!!))
            R.string.title_4 -> contentLayout.addView(Practice4DrawPointView(context!!))
        }
    }

    override fun initView() {

    }

    override fun initData() {
    }

}