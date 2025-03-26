package com.example.datn.view.main.fragment.update_user_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentUpdateUserInfoBinding

class UpdateUserInfoFragment : BaseFragment() {
    private lateinit var binding : FragmentUpdateUserInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateUserInfoBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun setView() {

    }

    override fun setAction() {
        binding.imgBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    override fun setObserves() {

    }

    override fun setTabBar() {

    }

}