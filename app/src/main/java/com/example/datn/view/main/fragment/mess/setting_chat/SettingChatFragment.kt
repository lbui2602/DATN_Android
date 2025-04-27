package com.example.datn.view.main.fragment.mess.setting_chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentSettingChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingChatFragment : BaseFragment() {
    private lateinit var binding: FragmentSettingChatBinding
    var groupId = ""
    private val viewModel : SettingChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getString("groupId") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingChatBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {

    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnLeft.setOnClickListener {
            
        }
    }

    override fun setObserves() {

    }

    override fun setTabBar() {

    }
}