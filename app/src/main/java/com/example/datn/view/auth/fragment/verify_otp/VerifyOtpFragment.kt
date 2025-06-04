package com.example.datn.view.auth.fragment.verify_otp

import android.os.Bundle
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
import com.example.datn.databinding.FragmentVerifyOtpBinding
import com.example.datn.models.mail.VerifyOTPRequest
import com.example.datn.util.Util
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpFragment : BaseFragment() {
    private lateinit var binding : FragmentVerifyOtpBinding
    var email =""
    private val viewModel : VerifyOtpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyOtpBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {

    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment,true)
        }
        binding.btnSend.setOnClickListener {
            val otp = binding.edtOTP.text.toString().trim()
            if(otp.length == 6){
                viewModel.verifyOtp(VerifyOTPRequest(email,otp))
            }
        }
    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.verifyOtpResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    val bundle = Bundle()
                    bundle.putString("email",email)
                    findNavController().navigate(R.id.action_verifyOtpFragment_to_updatePasswordFragment,bundle)
                }else{
                    Util.showDialog(requireContext(),response.message)
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun setTabBar() {

    }
}