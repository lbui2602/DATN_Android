package com.example.datn.view.auth.fragment.send_otp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentSendOtpBinding
import com.example.datn.models.mail.SendOTPRequest
import com.example.datn.util.Util
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendOtpFragment : BaseFragment() {
    private lateinit var binding : FragmentSendOtpBinding
    private val viewModel : SendOtpViewModel by viewModels()
    var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendOtpBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {

    }

    override fun setAction() {
        binding.btnSend.setOnClickListener {
            email = binding.edtEmail.text.toString().trim()
            if(email.isNullOrBlank()){
                Util.showDialog(requireContext(),"Vui lòng nhập vào email.")
            }else{
                viewModel.sendOtp(SendOTPRequest(email))
            }
        }
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnSend.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnSend.isEnabled = true
            }
        })
        viewModel.sendOtpResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    val bundle = Bundle()
                    bundle.putString("email",email)
                    findNavController().navigate(R.id.action_sendOtpFragment_to_verifyOtpFragment,bundle)
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