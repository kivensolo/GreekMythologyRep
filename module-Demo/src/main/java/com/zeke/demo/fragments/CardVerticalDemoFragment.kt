package com.zeke.demo.fragments

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.base.BaseFragment
import com.zeke.demo.R
import com.zeke.demo.model.CardItemModel
import com.zeke.demo.model.DemoContentModel
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter
import com.zeke.kangaroo.utils.ZLog

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
        mRecycleView.layoutManager = LinearLayoutManager(activity!!,
                LinearLayoutManager.VERTICAL, false)
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

    inner class RVAdapter : CommonRecyclerAdapter<CardItemModel>() {

        override fun getItemLayout(type: Int): Int {
            return R.layout.demo_card_item
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val data = getItem(position)
            val cardItemData = data as CardItemModel
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