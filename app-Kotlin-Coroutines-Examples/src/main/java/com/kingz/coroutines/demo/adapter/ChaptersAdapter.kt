package com.kingz.coroutines.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kingz.coroutines.demo.entity.Data
import com.zeke.example.coroutines.R
import kotlinx.android.synthetic.main.item_layout.view.*

/**
 * @author zeke.wang
 * @date 2020/6/22
 * @maintainer zeke.wang
 * @desc: ChaptersAdapter
 */
class ChaptersAdapter(private val dataList: MutableList<Data>)
    : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_layout, parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(dataList[position])
    }

    fun addData(list: List<Data>) {
        dataList.addAll(list)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(data: Data) {
            itemView.textViewUserName.text = data.name
            itemView.textViewUserEmail.text = data.id.toString()
        }
    }

}