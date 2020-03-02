package com.zeke.ktx.fragments

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.ViewStub
import android.widget.LinearLayout
import com.kingz.customdemo.R
import com.zeke.ktx.activity.practice_draw.Practice1DrawColorView
import com.zeke.ktx.base.BaseFragment
import com.zeke.ktx.view.LoadStatusView

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
            R.string.title_1 -> contentLayout.addView(Practice1DrawColorView(context))
        }
        // 填充load_state View
        val loadStateStub = rootView.findViewById(R.id.load_state) as ViewStub
        loadStateStub.layoutResource = R.layout.load_status
        loadStateStub.inflatedId = R.id.load_state_view
        loadStatusView = loadStateStub.inflate() as LoadStatusView
    }

    override fun initView() {

    }

    override fun initData() {
    }

}