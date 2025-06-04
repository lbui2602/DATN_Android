package com.example.datn.view.auth.fragment.update_password

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentUpdatePasswordBinding
import com.example.datn.models.password.ChangePasswordRequest
import com.example.datn.models.password.UpdatePasswordRequest
import com.example.datn.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePasswordFragment : BaseFragment() {
    private lateinit var binding : FragmentUpdatePasswordBinding
    private val viewModel : UpdatePasswordViewModel by viewModels()
    var email =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email") ?: ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdatePasswordBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {

    }

    override fun setAction() {
        binding.imgBack.setOnClickListener{
            findNavController().popBackStack(R.id.loginFragment,true)
        }
        binding.btnChange.setOnClickListener {
            val newPass = binding.edtNewPassword.text.toString().trim()
            val newPassConfirm = binding.edtNewPasswordConfirm.text.toString().trim()
            validate(newPass,newPassConfirm,{
                viewModel.updatePassword(UpdatePasswordRequest(email,newPass))
            })
        }
        binding.imgNewVisible.setOnClickListener {
            viewModel.changVisiblePasswordNew()
        }
        binding.imgNewVisibleConfirm.setOnClickListener {
            viewModel.changVisiblePasswordConfirm()
        }
    }
    private fun validate(newPass:String,newPassConfirm : String,action : () -> Unit){
        if(newPass.isNullOrBlank()){
            Util.showDialog(requireContext(),"Vui lòng nhập mật khẩu mới.")
        }else if(newPassConfirm.isNullOrBlank()){
            Util.showDialog(requireContext(),"Vui lòng xác nhận lại mật khẩu.")
        }else if(!newPass.equals(newPassConfirm)){
            Util.showDialog(requireContext(),"Mật khẩu mới không khớp vui lòng nhập lại.")
        }else{
            action()
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
        viewModel.updatePasswordResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if ( response.code.toInt() == 1) {
                    Util.showDialog(requireContext(),response.message,"OK",{
                        findNavController().popBackStack(R.id.loginFragment,true)
                    })
                } else {
                    Util.showDialog(requireContext(),response.message)
                }
            }
        })
    }

    override fun setTabBar() {

    }
}