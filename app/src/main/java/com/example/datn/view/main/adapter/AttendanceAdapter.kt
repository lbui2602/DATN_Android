package com.example.datn.view.main.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.click.IClickAttendance
import com.example.datn.databinding.LayoutAttendanceItemBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.util.Util

class AttendanceAdapter(
    private val iClick: IClickAttendance
) : ListAdapter<Attendance, AttendanceAdapter.AttendanceViewHolder>(AttendanceDiffCallback()) {

    class AttendanceViewHolder(val binding: LayoutAttendanceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAttendanceItemBinding.inflate(inflater, parent, false)
        return AttendanceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.binding.tvTime.text = data.time
            holder.binding.tvDate.text = data.date

            if (data.type == "check_in") {
                holder.binding.itemTimeLayout.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(holder.itemView.context, R.color.green)
                )
                holder.binding.imgType.setImageResource(R.drawable.check_in)
            } else {
                holder.binding.itemTimeLayout.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(holder.itemView.context, R.color.red)
                )
                holder.binding.imgType.setImageResource(R.drawable.check_out)
            }
            Glide.with(holder.itemView.context).load(Util.url+data.image).into(holder.binding.imgAttendance)
        }
    }

    class AttendanceDiffCallback : DiffUtil.ItemCallback<Attendance>() {
        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            // So sánh ID hoặc time+date unique
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            // So sánh toàn bộ nội dung
            return oldItem == newItem
        }
    }
}
