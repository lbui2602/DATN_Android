package com.example.datn.view.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.click.IClickUser
import com.example.datn.click.IClickUserOnline
import com.example.datn.databinding.LayoutOnlineUserItemBinding
import com.example.datn.databinding.LayoutUserItemBinding
import com.example.datn.models.register.User
import com.example.datn.util.Util

class OnlineUserAdapter(var iClickUser: IClickUserOnline) : ListAdapter<User, OnlineUserAdapter.UserViewHolder>(UserDiffCallback()) {

    inner class UserViewHolder(private val binding: LayoutOnlineUserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(user: User, context: Context) {
            binding.tvName.text = user.fullName
            Glide.with(context).load(Util.url+user.image).into(binding.imgAvatar)
            if(user.isOnline!!){
                binding.llOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.green))

            }else{
                binding.llOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))

            }
            binding.llItem.setOnClickListener {
                iClickUser.clickUser(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = LayoutOnlineUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position),holder.itemView.context)
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}