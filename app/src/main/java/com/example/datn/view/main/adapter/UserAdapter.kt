package com.example.datn.view.main.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.click.IClickUser
import com.example.datn.databinding.LayoutUserItemBinding
import com.example.datn.models.staff.User
import com.example.datn.util.Util

class UserAdapter(var iClickUser: IClickUser) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    inner class UserViewHolder(private val binding: LayoutUserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User,context: Context) {
            binding.tvFullName.text = user.fullName
            binding.tvEmail.text = user.email
            binding.tvAddress.text = user.address
            binding.tvDate.text = "Tham gia từ "+user.createdAt
            Glide.with(context).load(Util.url+user.image).into(binding.imgAvatar)
            if(user.status){
                binding.btnConfirm.setText("Đã xác nhận")
                binding.btnConfirm.isEnabled = false
                binding.btnConfirm.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.green)
                )
            }else{
                binding.btnConfirm.setText("Xác nhận")
                binding.btnConfirm.isEnabled = true
                binding.btnConfirm.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.red)
                )
            }
            binding.btnConfirm.setOnClickListener {
                iClickUser.confirmUser(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = LayoutUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
