package com.example.datn.view.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.click.IClickUserOnline
import com.example.datn.databinding.LayoutOnlineUserItemBinding
import com.example.datn.databinding.LayoutUserSelectItemBinding
import com.example.datn.models.register.User
import com.example.datn.util.Util

class SelectUserAdapter(var iClickUser: IClickUserOnline) : ListAdapter<User, SelectUserAdapter.SelectUserViewHolder>(UserDiffCallback()) {

    inner class SelectUserViewHolder(private val binding: LayoutUserSelectItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(user: User, context: Context) {
            binding.tvName.text = user.fullName
            Glide.with(context).load(Util.url+user.image).into(binding.imgAvatar)
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                iClickUser.selectUser(user,isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectUserViewHolder {
        val binding = LayoutUserSelectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectUserViewHolder, position: Int) {
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