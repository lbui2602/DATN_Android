package com.example.datn.view.main.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.click.IClickAttendance
import com.example.datn.click.IClickMess
import com.example.datn.databinding.LayoutAttendanceItemBinding
import com.example.datn.databinding.LayoutMessItemBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.models.group.Group
import com.example.datn.util.Util

class MessAdapter(
    var list : MutableList<Group>,
    val iClick: IClickMess
) : RecyclerView.Adapter<MessAdapter.MessViewHolder>() {
    class MessViewHolder(val binding: LayoutMessItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutMessItemBinding.inflate(inflater, parent, false)
        return MessViewHolder(binding)
    }
    fun updateList(newList : MutableList<Group>){
        list = newList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MessViewHolder, position: Int) {
        val data = list.get(position)
        if (data != null) {
            holder.binding.tvNameGr.text = data.name.toString()
            holder.binding.llItem.setOnClickListener {
                iClick.clickGr(data)
            }
            if(data.image !=null){
                Glide.with(holder.itemView.context).load(Util.url+data.image).into(holder.binding.imgAvatar)
            }
        }
    }

}