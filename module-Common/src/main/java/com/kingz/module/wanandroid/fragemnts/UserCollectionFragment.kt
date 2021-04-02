package com.kingz.module.wanandroid.fragemnts

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.animation.ScaleInAnimation
import com.kingz.base.factory.ViewModelFactory
import com.kingz.database.entity.CollectionArticle
import com.kingz.module.common.R
import com.kingz.module.common.utils.RvUtils
import com.kingz.module.wanandroid.adapter.ArticleAdapter
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.viewmodel.CollectArticalViewModel
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zeke.kangaroo.utils.ZLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * author：ZekeWang
 * date：2021/3/29
 * description：用户文章收藏页面的Fragment
 *
 * TODO 改为优先从ROOM数据库层中获取数据
 */
class UserCollectionFragment : CommonFragment<WanAndroidViewModelV2>() {

    private lateinit var mRecyclerView: RecyclerView
    private var articleAdapter: ArticleAdapter? = null
    private var swipeRefreshLayout: SmartRefreshLayout? = null
    private var collectArticles: CollectArticalViewModel? = null


    override val viewModel: WanAndroidViewModelV2 by viewModels {
        ViewModelFactory.build { WanAndroidViewModelV2() }
    }

    override fun getLayoutResID() = R.layout.fragment_common_page

    override fun initView() {
        super.initView()
        initRecyclerView()
        initFABInflate()
    }

    private fun initRecyclerView() {
        ZLog.d("init recyclerView.")
        mRecyclerView = rootView?.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView.apply {
            isVerticalScrollBarEnabled = true
            layoutManager = LinearLayoutManager(context)
            articleAdapter = ArticleAdapter(mType = ArticleAdapter.TYPE_COLLECTION)
            articleAdapter?.apply {
                adapterAnimation = ScaleInAnimation()
                setOnItemClickListener { adapter, view, position ->
                    if (articleAdapter!!.getDefItemCount() > position) {
                        openWeb(articleAdapter?.getItem(position))
                    }
                }

                //设置文章收藏监听器
                likeListener = object : ArticleAdapter.LikeListener {
                    override fun liked(item: Article, adapterPosition: Int) {
                        viewModel.changeArticleLike(item, adapterPosition, true)
                    }

                    override fun unLiked(item: Article, adapterPosition: Int) {
                        viewModel.changeArticleLike(item, adapterPosition, false)
                    }

                }
            }
            adapter = articleAdapter
        }
    }


    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout)!!
        swipeRefreshLayout?.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    ZLog.d("onRefresh")
                    fireVibrate()
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
//                finishLoadMore(2000/*,false*/)//传入false表示加载失败
                }
            })
        }
    }

    private fun initFABInflate() {
        val fabView = rootView?.findViewById<View>(R.id.app_fab_btn)
        fabView?.setOnClickListener {
            RvUtils.smoothScrollTop(mRecyclerView)
        }
    }

    override fun lazyInit() {
        initViewModel()
        initView()
        ZLog.d("articleAdapter?.itemCount = ${articleAdapter?.itemCount}")
        getMyCollectArticalData(0)
    }

    override fun initViewModel() {
        super.initViewModel()
        //Cannot create an instance of class com.kingz.module.wanandroid.viewmodel.CollectArticalViewModel
        collectArticles = ViewModelProvider(this).get(CollectArticalViewModel::class.java)
        collectArticles?.getCollectList(this,
            Observer { collectionList:List<CollectionArticle?>? ->
                if(collectionList == null) return@Observer
//                articleAdapter?.addData(collectionList)
            })

        viewModel.userCollectArticalListLiveData.observe(this, Observer {
            ZLog.d("userCollectArticalListLiveData onChanged: $it")
            if (it == null) {
                ZLog.d("User collection data is null.")
                swipeRefreshLayout?.apply {
                    finishRefresh()
                    visibility = View.GONE
                }
                showErrorStatus()
                return@Observer
            }
            dismissLoading()
            swipeRefreshLayout?.visibility = View.VISIBLE
            loadStatusView?.visibility = View.GONE

            launchIO {
                val datas = it.datas
                ZLog.d("collectionList Size = ${datas?.size};")
                //当前数据为空时
                if (articleAdapter?.getDefItemCount() == 0) {
                    withContext(Dispatchers.Main) {
                        articleAdapter?.addData(datas!!)
                    }
                    return@launchIO
                }

                // 数据非空时
                val currentFirstData = articleAdapter?.getItem(0)
                if (currentFirstData?.id != datas!![0].id) {
                    withContext(Dispatchers.Main) {
                        articleAdapter?.addData(datas)
                    }
                }

                //TODO ROOM数据库插入
                collectArticles?.inserArticalList(datas)
            }
        })
    }


    private fun getMyCollectArticalData(index: Int) {
        viewModel.getMineCollectArticalList(index)
    }
}