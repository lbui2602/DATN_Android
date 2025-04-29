package com.example.datn.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.click.IClickMess
import com.example.datn.databinding.LayoutMessItemBinding
import com.example.datn.models.group.Group
import com.example.datn.util.Util

class MessAdapter(
    private val iClick: IClickMess
) : ListAdapter<Group, MessAdapter.MessViewHolder>(GroupDiffCallback()) {

    class MessViewHolder(val binding: LayoutMessItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutMessItemBinding.inflate(inflater, parent, false)
        return MessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.tvNameGr.text = data.name.toString()
        holder.binding.llItem.setOnClickListener {
            iClick.clickGr(data)
        }
        if (data.image != null) {
            Glide.with(holder.itemView.context)
                .load(Util.url + data.image)
                .placeholder(R.drawable.bg_circle) // placeholder nếu cần
                .into(holder.binding.imgAvatar)
        } else {
            holder.binding.imgAvatar.setImageResource(R.drawable.group_icon) // ảnh mặc định
        }
    }

    class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            // So sánh ID nhóm
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            // So sánh toàn bộ nội dung
            return oldItem == newItem
        }
    }
}
