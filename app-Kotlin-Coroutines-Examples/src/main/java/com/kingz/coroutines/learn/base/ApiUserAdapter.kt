package com.kingz.coroutines.learn.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kingz.coroutines.data.model.ApiUser
import com.zeke.example.coroutines.R
import kotlinx.android.synthetic.main.item_layout.view.*

class ApiUserAdapter(
    private val users: ArrayList<ApiUser>
) : RecyclerView.Adapter<ApiUserAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent,false)
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(users[position])

    fun addData(list: List<ApiUser>) {
        users.addAll(list)
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: ApiUser) {
            itemView.textViewUserName.text = user.name
            itemView.textViewUserEmail.text = user.email
            Glide.with(itemView.imageViewAvatar.context)
                .load(user.avatar)
                .error(R.drawable.resource_heart3)
                .into(itemView.imageViewAvatar)
        }
    }

}