package com.example.datn.view.main.fragment.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentHistoryBinding
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class HistoryFragment : BaseFragment() {

    lateinit var binding : FragmentHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    @SuppressLint("MissingInflatedId")
    private fun createTabView(position: Int): View {
        // Inflate custom view
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)
        val tabIcon = view.findViewById<ImageView>(R.id.tabIcon)
        val tabText = view.findViewById<TextView>(R.id.tabText)

        // Thiết lập icon và text cho từng tab
        when (position) {
            0 -> {
                tabIcon.setImageResource(R.drawable.daily_icon) // Đặt icon cho tab đầu tiên
                tabText.text = "Chấm công" // Đặt text cho tab đầu tiên
            }
            1 -> {
                tabIcon.setImageResource(R.drawable.working_icon) // Đặt icon cho tab thứ hai
                tabText.text = "Ngày công" // Đặt text cho tab thứ hai
            }
        }
        return view
    }

    override fun setView() {
        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.customView = createTabView(position)
        }.attach()
    }

    override fun setAction() {

    }

    override fun setObserves() {

    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }
}