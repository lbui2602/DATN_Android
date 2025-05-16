package com.example.datn.view.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.datn.click.IClickDepartment
import com.example.datn.databinding.LayoutMessItemBinding
import com.example.datn.models.department.Department

class ManageDepartmentAdapter(
    private val iClick: IClickDepartment
) : ListAdapter<Department, ManageDepartmentAdapter.DepartmentViewHolder>(DepartmentDiffCallback()) {

    class DepartmentViewHolder(val binding: LayoutMessItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutMessItemBinding.inflate(inflater, parent, false)
        return DepartmentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.binding.tvNameGr.text = data.name.toString()
            holder.binding.llItem.setOnClickListener {
                iClick.clickDepartment(data)
            }
        }
    }

    class DepartmentDiffCallback : DiffUtil.ItemCallback<Department>() {
        override fun areItemsTheSame(oldItem: Department, newItem: Department): Boolean {
            // So sánh ID hoặc time+date unique
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Department, newItem: Department): Boolean {
            // So sánh toàn bộ nội dung
            return oldItem == newItem
        }
    }
}