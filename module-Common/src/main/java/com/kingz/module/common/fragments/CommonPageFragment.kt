package com.kingz.module.common.fragments

import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.R
import com.kingz.module.common.base.BaseFragment

/**
 * author：KingZ
 * date：2020/2/22
 * description：公共页面的Fragment
 */
open class CommonPageFragment: BaseFragment(){
    // private var fabButton: FloatingActionButton? = null

    lateinit var mRecyclerView:RecyclerView

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
        initRecyclerView()
    }

    open fun initRecyclerView(){
        mRecyclerView = rootView!!.findViewById(R.id.recycler_view) as RecyclerView
    }

    override fun onViewCreated() {
        showLoadingView()
    }

    open fun showLoadingView(){
        loadStatusView?.showEmpty()
    }

}