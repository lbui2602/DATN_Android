package com.example.datn.view.auth.fragment.register

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.databinding.FragmentRegisterBinding
import com.example.datn.models.department.Department
import com.example.datn.models.role.Role
import com.example.datn.view.auth.fragment.login.LoginViewModel
import com.example.datn.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding : FragmentRegisterBinding
    private var roles = mutableListOf<Role>()
    private var departments = mutableListOf<Department>()
    private val viewModel: RegisterViewModel by viewModels()
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
        initData()
        setActions()
        setObservers()
    }
    private fun initData(){
        viewModel.getRoles()
        viewModel.getDepartments()
    }
    private fun setActions(){
        binding.imgVisible.setOnClickListener {
            viewModel.changVisiblePassword()
        }
        binding.imgVisibleConfirm.setOnClickListener {
            viewModel.changVisiblePasswordConfirm()
        }
        binding.llBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun setObservers(){
        binding.spnRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = roles[position]

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.spnDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = departments[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        viewModel.rolesResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() == 1){
                    roles = response.roles.toMutableList()
                    setRoleSpinner()
                }
            } else {
                Snackbar.make(binding.root,"Fail", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.departmentsResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() == 1){
                    departments = response.departments.toMutableList()
                    setDepartmentSpinner()
                }
            } else {
                Snackbar.make(binding.root,"Fail", Snackbar.LENGTH_SHORT).show()
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

        viewModel.isVisibleConfirm.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible == true) {
                binding.imgVisibleConfirm.setImageResource(R.drawable.ic_visible_off)
                binding.edtPasswordConfirm.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.imgVisibleConfirm.setImageResource(R.drawable.ic_visible)
                binding.edtPasswordConfirm.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edtPasswordConfirm.setSelection(binding.edtPasswordConfirm.text!!.length)
        })
    }
    private fun setRoleSpinner(){
        val adapterRoles = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            roles.map { it.name }
        )
        adapterRoles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnRole.adapter = adapterRoles
    }
    private fun setDepartmentSpinner(){
        val adapterDepartment = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            departments.map { it.name }
        )
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDepartment.adapter = adapterDepartment
    }
    private fun validate(){
        val fullname = binding.edtFullname.text.toString().trim()
        val email = binding.edtFullname.text.toString().trim()
        val password = binding.edtFullname.text.toString().trim()
        val phone = binding.edtFullname.text.toString().trim()
        val address = binding.edtFullname.text.toString().trim()
        val departmentId = binding.edtFullname.text.toString().trim()
    }
}