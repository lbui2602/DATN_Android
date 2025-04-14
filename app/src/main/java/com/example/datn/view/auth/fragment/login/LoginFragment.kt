package com.example.datn.view.auth.fragment.login

import android.content.Intent
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
import com.bumptech.glide.Glide
import com.example.datn.BuildConfig
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentLoginBinding
import com.example.datn.models.login.LoginRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    lateinit var binding : FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setView() {

    }

    override fun setAction(){
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            validate(email,password) {
                viewModel.login(LoginRequest(email, password))
            }
//            startActivity(Intent(requireContext(),MainActivity::class.java))
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.imgVisible.setOnClickListener {
            viewModel.changVisiblePassword()
        }
    }

    override fun setObserves() {
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null ) {
                if(response.code.toInt()==1){
                    sharedPreferencesManager.saveAuthToken(response.token)
                    sharedPreferencesManager.saveUserId(response._id)
                    sharedPreferencesManager.saveFaceToken(response.face_token)
                    sharedPreferencesManager.saveUserRole(response.roleId)
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                    requireActivity().finish()
                }else{
                    Util.showDialog(requireContext(),response.message)
                }
            } else {
                Snackbar.make(binding.root,"Đăng nhập thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }
            else{
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.isVisible.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible == true) {
                binding.imgVisible.setImageResource(R.drawable.ic_visible_off)
                binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.imgVisible.setImageResource(R.drawable.ic_visible)
                binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
        })
    }

    override fun setTabBar() {

    }

    private fun validate(email:String,password:String,action : () -> Unit){
        if(email.isNullOrBlank()){
            Util.showDialog(requireContext(),getString(R.string.msg_login_email))
        }else if(password.isNullOrBlank()){
            Util.showDialog(requireContext(),getString(R.string.msg_login_password))
        }else{
            action()
        }
    }
}