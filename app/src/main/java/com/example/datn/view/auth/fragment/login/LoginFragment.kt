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
import com.example.datn.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding : FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
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
        Log.e("apiKey",BuildConfig.face_api_key)
        Log.e("apiKey",BuildConfig.face_api_secret)
        Log.e("apiKey",BuildConfig.faceset_token)
        setAction()
        setObserver()
    }

    private fun setObserver() {
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                print(response.toString())
                Glide.with(requireContext()).load("http://192.168.52.52:3000"+response.image).into(binding.imgLogo)
                startActivity(Intent(requireContext(),MainActivity::class.java))
            } else {
                Snackbar.make(binding.root,"Đăng nhập thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAction(){
        binding.btnLogin.setOnClickListener {
            val loginRequest = LoginRequest("anv@gmail.com","11111111")
            viewModel.login(loginRequest)
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}