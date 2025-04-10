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
import com.example.datn.databinding.LayoutAttendanceItemBinding
import com.example.datn.databinding.LayoutWorkingDayItemBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.models.working_day.WorkingDay
import com.example.datn.models.working_day.WorkingDayByMonthResponse
import com.example.datn.util.Util

class WorkingDayAdapter(
    var list : MutableList<WorkingDay>,
    val iClick: IClickAttendance
) : RecyclerView.Adapter<WorkingDayAdapter.WorkingDayViewHolder>() {
    class WorkingDayViewHolder(val binding: LayoutWorkingDayItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkingDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutWorkingDayItemBinding.inflate(inflater, parent, false)
        return WorkingDayViewHolder(binding)
    }
    fun updateList(newList : MutableList<WorkingDay>){
        list = newList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: WorkingDayViewHolder, position: Int) {
        val data = list.get(position)
        if (data != null) {

        }
    }

}