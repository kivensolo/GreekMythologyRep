package com.kingz.coroutines.learn.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kingz.coroutines.data.model.ApiUser
import com.zeke.example.coroutines.R

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
            itemView.findViewById<TextView>(R.id.textViewUserName).text = user.name
            itemView.findViewById<TextView>(R.id.textViewUserEmail).text = user.email

            val avatarView = itemView.findViewById<ImageView>(R.id.imageViewAvatar)
            Glide.with(avatarView.context)
                .load(user.avatar)
                .error(R.drawable.resource_heart3)
                .into(avatarView)
        }
    }

}