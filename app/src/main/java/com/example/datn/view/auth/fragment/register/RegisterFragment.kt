package com.example.datn.view.auth.fragment.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datn.R
import com.example.datn.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding : FragmentRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun setActions(){

    }
    private fun setObservers(){

    }
    private fun setSpinner(){

    }
    private fun validate(){
        val fullname = binding.edtFullname.text.toString().trim()
        val email = binding.edtFullname.text.toString().trim()
        val password = binding.edtFullname.text.toString().trim()
        val phone = binding.edtFullname.text.toString().trim()
        val address = binding.edtFullname.text.toString().trim()
        val departmentId = binding.edtFullname.text.toString().trim()
        val roleId = binding.spnRole.
    }
}