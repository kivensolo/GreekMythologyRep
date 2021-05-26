package com.zeke.eyepetizer.fragemnts

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.animation.ScaleInAnimation
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.base.IRvScroller
import com.kingz.module.common.utils.RvUtils
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zeke.eyepetizer.adapter.EyepetizerPagerAdapter
import com.zeke.eyepetizer.bean.EyepetizerTabPageData
import com.zeke.eyepetizer.bean.Item
import com.zeke.eyepetizer.viewmodel.EyepetizerViewModel
import com.zeke.kangaroo.utils.ZLog
import com.zeke.moudle_eyepetizer.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Url

/**
 * author：KingZ
 * date：2019/12/29
 * description：开眼视频
 * Tab详情内容的Fragment
 */
class EyepetizerPagerFragment(
    var apiUrl: String
) : CommonFragment<EyepetizerViewModel>(), IRvScroller {


   // <editor-fold defaultstate="collapsed" desc="页面控件 Widgets">
    private lateinit var mRecyclerView: RecyclerView
    private var eyepetizerAdapter: EyepetizerPagerAdapter? = null
    private var swipeRefreshLayout: SmartRefreshLayout? = null
    // setNoMoreData(true)  在没有更多数据，即最后一页时调用，并将参数传为 true,
    // 最重要的是要记得在不是最后一页时，记得要把状态恢复过来，即将参数传为 false.
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="业务属性">
    private var nextPageUrl: String? = null
    private var firstPageUrl: String? = null
    private var mItemList: List<Item>? = null
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="VM属性 ViewModel property">
    override val viewModel: EyepetizerViewModel by viewModels {
        ViewModelFactory.build { EyepetizerViewModel() }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.tabPageDetailLiveData.observe(this, Observer {
            if (it?.itemList == null) {
                ZLog.d("Tab page detail LiveData request error. result is null.")
                swipeRefreshLayout?.finishRefresh()
                showErrorView(true)
                return@Observer
            }
            dismissLoading()
            showErrorView(false)
            launchIO {

                if (!TextUtils.isEmpty(it.nextPageUrl)) {
                    nextPageUrl = it.nextPageUrl
                }else{
                    ZLog.e("Next page url is empty.")
                }
                addNewData(it)
                syncRefreshState()
            }

        })

        initData()
    }

    /**
     * 同步刷新状态
     */
    private fun syncRefreshState() {
        with(swipeRefreshLayout!!) {
            ZLog.d("state=$state")
            if (state == RefreshState.Loading) {
                finishLoadMore()
            } else {
                finishRefresh()
            }
        }
    }
    // </editor-fold>


    override fun getLayoutResID(): Int = R.layout.fragment_common_page

    // <editor-fold defaultstate="collapsed" desc="视图初始化 UI init">
    override fun initView() {
        super.initView()
        initRecyclerView()
        initSwipeRefreshLayout()
        initFABInflate()
    }

    private fun initFABInflate() {
        val fabView = rootView?.findViewById<View>(R.id.app_fab_btn)
        fabView?.setOnClickListener { scrollToTop() }
    }

    private fun initRecyclerView() {
        mRecyclerView = rootView?.findViewById(R.id.recycler_view) as RecyclerView
        with(mRecyclerView) {
            isVerticalScrollBarEnabled = true
            layoutManager = LinearLayoutManager(context)
            eyepetizerAdapter = EyepetizerPagerAdapter()
            eyepetizerAdapter?.apply {
                adapterAnimation = ScaleInAnimation()

            }
            adapter = eyepetizerAdapter
        }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout)!!
        swipeRefreshLayout?.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    ZLog.d("onRefresh")
                    fireVibrate()
//                    refreshTabPageDetail()
                    requestTabPageDetail(apiUrl)
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    ZLog.d("onLoadMore  state=$state")
                    if(!TextUtils.isEmpty(nextPageUrl)){
//                      apiUrl = nextPageUrl!!
                        requestTabPageDetail(nextPageUrl!!)
                    }else{
                        finishLoadMore(500/*,false*/)//传入false表示加载失败
                    }
                }
            })
        }
    }
    // </editor-fold>


 // <editor-fold defaultstate="collapsed" desc="数据处理 Data deal">

    private fun initData() {
        requestTabPageDetail(apiUrl)
    }
    private fun refreshTabPageDetail(){
        viewModel.getTabPageDetail(apiUrl)
    }
      /**
     * 请求对应分类下的详情数据
     */
    private fun requestTabPageDetail(@Url url:String){
        ZLog.d("request Tab page detail: $url")
        viewModel.getTabPageDetail(url)
    }

    /**
     * 添加新数据至RecyclerView中
     */
    private suspend fun addNewData(data: EyepetizerTabPageData) {
        withContext(Dispatchers.Main) {
            with(eyepetizerAdapter!!) {
                if (getDefItemCount() == 0) {
                    addData(data.itemList)
                } else {
                    if (swipeRefreshLayout!!.state == RefreshState.Refreshing) {
                        addData(data.itemList)
                    } else {
                        val defItemCount = getDefItemCount()
                        addData(defItemCount, data.itemList)
                    }
                }
            }
        }
    }
 // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="视图操作 UI operator">
    private fun showErrorView(show:Boolean) {
        if(show){
            swipeRefreshLayout?.visibility = View.GONE
            showErrorStatus()
        }else{
            swipeRefreshLayout?.visibility = View.VISIBLE
            loadStatusView?.visibility = View.GONE
        }
    }


    // <editor-fold defaultstate="collapsed" desc="滚动操作  UI scroll">
    override fun scrollToTop() {
        RvUtils.smoothScrollTop(mRecyclerView)
    }

    override fun scrollToTopRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // </editor-fold>
    // </editor-fold>

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
