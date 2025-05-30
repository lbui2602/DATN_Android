package com.example.datn.view.main.fragment.change_password

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentChangePasswordBinding
import com.example.datn.models.password.ChangePasswordRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.fragment.attendance.AttendanceViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment() {
    private lateinit var binding : FragmentChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun setView() {

    }

    override fun setAction() {
        binding.imgBack.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.btnChange.setOnClickListener {
            val oldPass = binding.edtOldPassword.text.toString().trim()
            val newPass = binding.edtNewPassword.text.toString().trim()
            val newPassConfirm = binding.edtNewPasswordConfirm.text.toString().trim()
            validate(oldPass,newPass,newPassConfirm,{
                viewModel.changePassword("Bearer ${sharedPreferencesManager.getAuthToken().toString()}",
                    ChangePasswordRequest(oldPass,newPass))
            })
        }
        binding.imgNewVisible.setOnClickListener {
            viewModel.changVisiblePasswordNew()
        }
        binding.imgOldVisible.setOnClickListener {
            viewModel.changVisiblePassword()
        }
        binding.imgNewVisibleConfirm.setOnClickListener {
            viewModel.changVisiblePasswordConfirm()
        }
    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
                binding.btnChange.isEnabled = false
            }else{
                binding.progressBar.visibility = View.GONE
                binding.btnChange.isEnabled = true
            }
        })
        viewModel.changePasswordResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if ( response.code.toInt() == 1) {
                    Util.showDialog(requireContext(),response.message,"OK",{
                        findNavController().popBackStack()
                    })
                } else {
                    Util.showDialog(requireContext(),response.message)
                }
            }
        })
        viewModel.isVisible.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible == true) {
                binding.imgOldVisible.setImageResource(R.drawable.ic_visible_off)
                binding.edtOldPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.imgOldVisible.setImageResource(R.drawable.ic_visible)
                binding.edtOldPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edtOldPassword.setSelection(binding.edtOldPassword.text!!.length)
        })

        viewModel.isVisibleConfirm.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible == true) {
                binding.imgNewVisibleConfirm.setImageResource(R.drawable.ic_visible_off)
                binding.edtNewPasswordConfirm.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.imgNewVisibleConfirm.setImageResource(R.drawable.ic_visible)
                binding.edtNewPasswordConfirm.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edtNewPasswordConfirm.setSelection(binding.edtNewPasswordConfirm.text!!.length)
        })

        viewModel.isVisibleNew.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible == true) {
                binding.imgNewVisible.setImageResource(R.drawable.ic_visible_off)
                binding.edtNewPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.imgNewVisible.setImageResource(R.drawable.ic_visible)
                binding.edtNewPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edtNewPassword.setSelection(binding.edtNewPassword.text!!.length)
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }
    private fun validate(oldPass:String,newPass:String,newPassConfirm : String,action : () -> Unit){
        if(oldPass.isNullOrBlank()){
            Util.showDialog(requireContext(),"Vui lòng nhập mật khẩu cũ.")
        }else if(newPass.isNullOrBlank()){
            Util.showDialog(requireContext(),"Vui lòng nhập mật khẩu mới.")
        }else if(newPassConfirm.isNullOrBlank()){
            Util.showDialog(requireContext(),"Vui lòng xác nhận lại mật khẩu.")
        }else if(!newPass.equals(newPassConfirm)){
            Util.showDialog(requireContext(),"Mật khẩu mới không khớp vui lòng nhập lại.")
        }else{
            action()
        }
    }
}