package com.example.datn.view.main.fragment.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentProfileBinding
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.fragment.attendance.AttendanceViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var binding : FragmentProfileBinding
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {

    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnUpdate.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateUserInfoFragment)
        }
        binding.btnChangPass.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
    }
    override fun setTabBar(){
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    override fun setObserves() {
        viewModel.profileResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code.toInt() ==1 ) {
                Glide.with(requireActivity()).load(Util.url+response.user.image).into(binding.imgAvatar)
                binding.tvEmail.text = response.user.email
                binding.tvFullname.text = response.user.fullName
                binding.tvDepartment.text = response.user.department
                binding.tvRole.text = response.user.role
                binding.tvAddress.text = response.user.address
                binding.tvPhone.text = response.user.phone
            } else {
                Log.e("setObservers",response.toString())
                Snackbar.make(binding.root,"Điểm danh thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfile("Bearer "+sharedPreferencesManager.getAuthToken().toString())
    }
}