package com.example.datn.view.auth.fragment.login

import android.content.Intent
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
import com.example.datn.BuildConfig
import com.example.datn.R
import com.example.datn.databinding.FragmentLoginBinding
import com.example.datn.models.login.LoginRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
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
        setAction()
        setObserver()
    }

    private fun setObserver() {
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                print(response.toString())
                Glide.with(requireContext()).load("http://192.168.52.52:3000"+response.image).into(binding.imgLogo)
                sharedPreferencesManager.saveAuthToken(response.token)
                sharedPreferencesManager.saveUserId(response._id)
                startActivity(Intent(requireContext(),MainActivity::class.java))
            } else {
                Snackbar.make(binding.root,"Đăng nhập thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAction(){
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