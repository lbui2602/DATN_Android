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
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var binding : FragmentProfileBinding
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private val viewModel: ProfileViewModel by viewModels()
    var userString = ""
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
            val bundle = Bundle()
            bundle.putString("user",userString)
            bundle.putString("from","profile")
            bundle.putBoolean("isOwner",true)
            findNavController().navigate(R.id.action_profileFragment_to_updateUserInfoFragment,bundle)
        }
        binding.btnChangPass.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
    }
    override fun setTabBar(){
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.profileResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if ( response.code.toInt() ==1 ) {
                    Glide.with(requireActivity()).load(Util.url+response.user.image).into(binding.imgAvatar)
                    binding.tvEmail.text = response.user.email
                    binding.tvFullname.text = response.user.fullName
                    binding.tvDepartment.text = response.user.department
                    binding.tvRole.text = response.user.role
                    binding.tvAddress.text = response.user.address
                    binding.tvGender.text = response.user.gender
                    binding.tvBirthday.text = response.user.birthday
                    binding.tvPhone.text = response.user.phone
                                                    val gson = Gson()
                                                    userString = gson.toJson(response.user)
                } else {
                    Util.showDialog(requireContext(),response.message)
                }
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        viewModel.getProfile("Bearer "+sharedPreferencesManager.getAuthToken().toString())
    }
}