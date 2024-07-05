package com.kingz.coroutines.learn.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kingz.coroutines.data.local.entity.User
import com.zeke.example.coroutines.R

class UserAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<UserAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent,false)
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(users[position])

    fun addData(list: List<User>) {
        users.addAll(list)
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
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