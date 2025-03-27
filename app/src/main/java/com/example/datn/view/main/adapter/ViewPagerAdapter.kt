package com.example.datn.view.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.datn.view.main.fragment.history.attendance.AttendanceHistoryFragment
import com.example.datn.view.main.fragment.history.working_day.WorkingDayHistoryFragment
import com.example.datn.view.main.fragment.home.HomeFragment
import com.example.datn.view.main.fragment.profile.ProfileFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2 // Số lượng tab (fragment)
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AttendanceHistoryFragment()
            1 -> WorkingDayHistoryFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}