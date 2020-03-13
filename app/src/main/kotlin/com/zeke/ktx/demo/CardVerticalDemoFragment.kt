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
    private lateinit var mRecycleView: RecyclerView
    var mRV: RVAdapter = RVAdapter()

    var pageId: String = ""

    override fun getLayoutId(): Int {
        return R.layout.single_recyclerview
    }

    override fun initView() {
        pageModel?.demoData?.forEach {
            mRV.addItem(it)
        }
        mRecycleView = rootView.findViewById(R.id.content_recycler)
        mRecycleView.setHasFixedSize(true)
        mRecycleView.layoutManager = LinearLayoutManager(activity!!,LinearLayoutManager.VERTICAL, false)
        mRecycleView.adapter = mRV
    }

    companion object {
        var pageModel: DemoContentModel? = null
        fun newInstance(pageData: DemoContentModel): CardVerticalDemoFragment {
            pageModel = pageData
            return CardVerticalDemoFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            pageId = args.getString("page_id", "")
        }
    }

    override fun initData() {
    }

    inner class RVAdapter : CommonRecyclerAdapter<CardItemData>() {

        override fun getItemLayout(type: Int): Int {
            return R.layout.demo_card_item
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val data = getItem(position)
            val cardItemData = data as CardItemData
            // CardView的Title
            holder.getView<TextView>(R.id.card_title_text).text = cardItemData.title

            // CardView的Content
            val cardContentLayout = holder.getView<LinearLayout>(R.id.card_content_layout)
            if (cardItemData.content != null) {
                cardContentLayout.addView(cardItemData.content)
                ZLog.d("cardContentLayout.addView +1")
            } else {
                cardContentLayout.setBackgroundColor(Color.GRAY)
            }
        }
    }

}