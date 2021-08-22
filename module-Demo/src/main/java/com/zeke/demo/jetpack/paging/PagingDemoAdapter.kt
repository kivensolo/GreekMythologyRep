package com.zeke.demo.jetpack.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zeke.demo.R

/**
 * author: King.Z <br>
 * date:  2021/8/22 21:42 <br>
 * description:  <br>
 */
class PagingDemoAdapter : PagingDataAdapter<Data, PagingDemoAdapter.ViewHolder>(DataDifferntiator) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvUser).text = "${getItem(position)?.first_name} ${getItem(position)?.last_name}"
        holder.itemView.findViewById<TextView>(R.id.tvScore).text = getItem(position)?.email
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
             return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_rank, parent, false)
        )
    }

    /**
     * Paging接收一个DiffUtil的Callback处理Item的diff，这里定一个DataDifferntiator
     */
    object DataDifferntiator : DiffUtil.ItemCallback<Data>() {

        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

}

/**
 * 创建HeaderFooterAdapter继承自LoadStateAdapter，onBindViewHolder中可以返回LoadState
 * 通过withLoadStateHeaderAndFooter将其添加到MainListAdapter
 */
//class HeaderFooterAdapter() : LoadStateAdapter<HeaderFooterAdapter.ViewHolder>() {
//
//    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
//
//        if (loadState == LoadState.Loading) {
//            //show progress viewe
//        } else //hide the view
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
//        return LoadStateViewHolder(
//           //layout file
//        )
//    }
//
//    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view)
//}
