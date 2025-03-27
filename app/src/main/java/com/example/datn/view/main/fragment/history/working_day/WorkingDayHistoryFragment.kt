package com.example.datn.view.main.fragment.history.working_day

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentWorkingDayHistoryBinding
import com.example.datn.view.main.MainActivity

class WorkingDayHistoryFragment : BaseFragment() {
    private lateinit var binding : FragmentWorkingDayHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkingDayHistoryBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {

    }

    override fun setAction() {

    }

    override fun setObserves() {

    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }
}