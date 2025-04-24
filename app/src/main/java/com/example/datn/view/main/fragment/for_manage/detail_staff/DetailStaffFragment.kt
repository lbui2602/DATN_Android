package com.example.datn.view.main.fragment.for_manage.detail_staff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentDetailStaffBinding

class DetailStaffFragment : BaseFragment() {
    private lateinit var binding : FragmentDetailStaffBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailStaffBinding.inflate(layoutInflater,container,false)
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

    }
}