package com.zeke.ktx.demo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import com.kingz.customdemo.R
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter
import com.zeke.kangaroo.utils.ZLog
import com.zeke.ktx.base.BaseFragment
import com.zeke.ktx.demo.modle.CardItemModle
import com.zeke.ktx.demo.modle.DemoContentModel

/**
 * author：KingZ
 * date：2020/2/22
 * description：
 *  垂直Card样式的 Fragment
 * <RecyclerView>
 *     <cardView></cardView>
 *     <cardView></cardView>
 *     <cardView></cardView>
 *     ...
 * </RecyclerView>
 *
 */
class CardVerticalDemoFragment : BaseFragment() {

    private var pageModel: DemoContentModel? = null
    private lateinit var mRecycleView: RecyclerView
    var mRV: RVAdapter = RVAdapter()

    override fun getLayoutId(): Int {
        return R.layout.single_recyclerview
    }

    fun initData(pageData: DemoContentModel?) {
        pageModel = pageData
    }

    override fun onCreateViewReady() {
        super.onCreateViewReady()
        mRecycleView = rootView.findViewById(R.id.content_recycler)
        mRecycleView.setHasFixedSize(true)
        mRecycleView.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        mRecycleView.adapter = mRV
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(mRV.itemCount == 0){ // 防止多次初始化数据
            ZLog.d(TAG,"数据为空,进行数据添加")
            pageModel?.demoData?.forEach {
                mRV.addItem(it)
            }
        }
    }

    override fun onViewCreated() {
    }

    /**
     * 被PagerAdapter销毁时，会被调用
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ZLog.d(TAG,"onSaveInstanceState")
    }

    inner class RVAdapter : CommonRecyclerAdapter<CardItemModle>() {

        override fun getItemLayout(type: Int): Int {
            return R.layout.demo_card_item
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val data = getItem(position)
            val cardItemData = data as CardItemModle
            // CardView的Title
            holder.getView<TextView>(R.id.card_title_text).text = cardItemData.title

            // CardView的Content
            val cardContentLayout = holder.getView<LinearLayout>(R.id.card_content_layout)
            if (cardItemData.content != null) {
                try {
                    if (cardContentLayout.childCount == 0) {
                        ZLog.d("cardContentLayout.addView +1")
                        cardContentLayout.addView(cardItemData.content)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                cardContentLayout.setBackgroundColor(Color.GRAY)
            }
        }
    }

}