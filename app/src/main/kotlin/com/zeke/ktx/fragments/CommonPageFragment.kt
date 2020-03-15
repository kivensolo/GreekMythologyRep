package com.zeke.ktx.fragments

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.ViewStub
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseFragment
import com.zeke.ktx.view.LoadStatusView

/**
 * author：KingZ
 * date：2020/2/22
 * description：公共页面的Fragment
 */
class CommonPageFragment: BaseFragment(){
    private var fabButton: FloatingActionButton? = null

    companion object{
        const val mContentLayoutResTag:String = "contentLayoutRes"
//        fun newInstance(content: View): CommonPageFragment {
//            val fragment = CommonPageFragment()
//            val args = Bundle()
//            args.putInt(mContentLayoutResTag, contentLayoutRes)
//            fragment.arguments = args
//            contentView = WeakReference(content)
//            return fragment
//        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_common_page
    }

    override fun onCreateViewReady() {
        super.onCreateViewReady()
        val recyclerView = rootView.findViewById(R.id.recycler_view) as RecyclerView

        // 填充load_state View
        val loadStateStub = rootView.findViewById(R.id.load_state) as ViewStub
        loadStateStub.layoutResource = R.layout.load_status
        loadStateStub.inflatedId = R.id.load_state_view
        loadStatusView = loadStateStub.inflate() as LoadStatusView

        // 填充FAB View
        val fabStub = rootView.findViewById(R.id.fbtn_go_top) as ViewStub
        fabStub.layoutResource = R.layout.app_floating_action_button
        loadStateStub.inflatedId = R.id.app_fab_btn
        fabButton = fabStub.inflate() as FloatingActionButton
    }

    override fun onViewCreated() {
        loadStatusView?.showEmpty()
    }

}