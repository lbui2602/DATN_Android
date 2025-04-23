package com.example.datn.view.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.click.IClickWorkingDay
import com.example.datn.databinding.LayoutWorkingDayItemBinding
import com.example.datn.models.working_day.WorkingDay
import com.example.datn.models.working_day.WorkingDayXX
import com.example.datn.util.Util

class WorkingDayForManageAdapter(
    var list : MutableList<WorkingDayXX>,
    var iClickWorkingDay: IClickWorkingDay
) : RecyclerView.Adapter<WorkingDayForManageAdapter.WorkingDayViewHolder>() {
    class WorkingDayViewHolder(val binding: LayoutWorkingDayItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkingDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutWorkingDayItemBinding.inflate(inflater, parent, false)
        return WorkingDayViewHolder(binding)
    }
    fun updateList(newList : MutableList<WorkingDayXX>){
        list = newList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("ResourceAsColor", "MissingInflatedId")
    override fun onBindViewHolder(holder: WorkingDayViewHolder, position: Int) {
        val data = list.get(position)
        if (data != null) {
            holder.binding.cardView.visibility = View.VISIBLE
            holder.binding.tvName.visibility = View.VISIBLE
            holder.binding.tvDate.text = data.date
            holder.binding.tvName.text = data.userId.fullName
            Glide.with(holder.itemView.context).load(Util.url+data.userId.image).into(holder.binding.imgAttendance)
            holder.binding.tvDate.text = data.date
            holder.binding.tvTotalHour.text = "Tổng số giờ công: ${data.totalHours}"
            val pairedTimes = mutableListOf<Pair<String, String>>()
            var currentCheckIn: String? = null
            data.attendances.forEach { attendance ->
                when (attendance.type) {
                    "check_in" -> {
                        currentCheckIn = attendance.time
                    }
                    "check_out" -> {
                        currentCheckIn?.let { checkInTime ->
                            pairedTimes.add(Pair(checkInTime, attendance.time))
                            currentCheckIn = null
                        }
                    }
                }
            }
            val inflater = LayoutInflater.from(holder.itemView.context)
            holder.binding.flexboxLayout.removeAllViews()
            pairedTimes.forEach { (checkIn, checkOut) ->
                val itemView = inflater.inflate(R.layout.time_item, holder.binding.flexboxLayout, false)
                val tvTime = itemView.findViewById<TextView>(R.id.tvTimeInOut)
                tvTime.text = "$checkIn - $checkOut"
                holder.binding.flexboxLayout.addView(itemView)
            }
            holder.binding.llItem.setOnClickListener {
                iClickWorkingDay.selectWorkingDay(data)
            }
        }
    }
}