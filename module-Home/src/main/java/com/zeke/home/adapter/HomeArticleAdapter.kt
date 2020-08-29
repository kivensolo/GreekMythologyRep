package com.zeke.home.adapter

import android.view.View
import android.view.ViewGroup
import com.kingz.module.common.adapter.IDelegateAdapter
import com.kingz.module.common.bean.ArticleData
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter


/**
 * author: King.Z <br>
 * date:  2020/5/24 13:05 <br>
 * description: 支持多种viewType的首页推荐Adapter <br>
 *
 */
class HomeArticleAdapter : CommonRecyclerAdapter<ArticleData.DataBean.ArticleItem>() {

    /**
     * ViewType类型判断的委托者
     */
    var delegateAdapters: MutableList<IDelegateAdapter<ArticleData.DataBean.ArticleItem>> =
        ArrayList()

    fun addDelegate(delegateAdapter: IDelegateAdapter<ArticleData.DataBean.ArticleItem>) {
        delegateAdapters.add(delegateAdapter)
    }

    override fun getItemLayout(type: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 找到对应的委托Adapter 把onCreateViewHolder交给委托Adapter去处理
        return delegateAdapters[viewType].onCreateViewHolder(parent, viewType) as ViewHolder
    }

    override fun getItemViewType(position: Int): Int {
        val recomData = getItem(position)
        // 遍历所有的代理，查询谁能处理
        delegateAdapters.forEach {
            if (it.isForViewType(recomData)) {
                // 谁能处理就返回他的index
                return delegateAdapters.indexOf(it)
            }
        }
        return 0
        // throw RuntimeException("没有找到可以处理的委托Adapter")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 找到当前ViewHolder的ViewType，也就是委托Adapter在集合中的index
        val viewType = holder.itemViewType
        // 找到对应的委托Adapter
        val delegateAdapter = delegateAdapters[viewType]
        // 把onBindViewHolder交给委托Adapter去处理
        delegateAdapter.onBindViewHolder(holder, position, getItem(position))

        // 监听器设置
        if (mOnItemClickListener != null) {
            holder.setOnClickListener { itemView ->
                mOnItemClickListener?.onItemClick(itemView, position)
            }
        }
    }

    public var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }
}