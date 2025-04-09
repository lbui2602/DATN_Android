package com.example.datn.view.main.fragment.update_user_info

import android.os.Bundle
import android.util.Log
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
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentUpdateUserInfoBinding
import com.example.datn.models.department.Department
import com.example.datn.models.password.CheckPasswordRequest
import com.example.datn.models.profile.User
import com.example.datn.models.role.Role
import com.example.datn.models.update_user.UpdateUserRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.auth.fragment.register.RegisterViewModel
import com.example.datn.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue
@AndroidEntryPoint
class UpdateUserInfoFragment : BaseFragment() {
    lateinit var user : User
    private lateinit var binding : FragmentUpdateUserInfoBinding
    private val viewModel: UpdateUserInfoViewModel by viewModels()
    private var roles = mutableListOf<Role>()
    private var departments = mutableListOf<Department>()
    var selectedRoleID = ""
    var selectedDepartmentId = ""
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userString = arguments?.getString("user")
        val gson = Gson()
        user = gson.fromJson(userString, User::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateUserInfoBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }
    private fun initData(){
        viewModel.getRoles()
        viewModel.getDepartments()
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
    override fun setView() {
        if(user!=null){
            binding.tvEmail.text = user.email
            binding.edtFullname.setText(user.fullName)
            binding.edtPhone.setText(user.phone)
            binding.edtAddress.setText(user.address)
            Glide.with(requireContext()).load(Util.url+user.image).into(binding.imgAvatar)
        }
    }

    override fun setAction() {
        binding.imgBack.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.btnChange.setOnClickListener {
            val pass = binding.edtPassword.text.toString().trim()
            viewModel.checkPassword("Bearer "+sharedPreferencesManager.getAuthToken(),
                CheckPasswordRequest(pass))
        }
    }

    override fun setObserves() {
        viewModel.checkPasswordResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                Log.e("check",response.toString())
                if(response.message.equals("false")){
                    Util.showDialog(requireContext(),"Mật khẩu xác thực không đúng!")
                }else{
                    val fullName = binding.edtFullname.text.toString().trim().ifEmpty { null }
                    val phone = binding.edtPhone.text.toString().trim().ifEmpty { null }
                    val address = binding.edtAddress.text.toString().trim().ifEmpty { null }
                    val request = UpdateUserRequest(fullName,phone,address,selectedRoleID,selectedDepartmentId)
                    viewModel.updateUser(
                        "Bearer "+sharedPreferencesManager.getAuthToken(),
                        request
                    )
                }
            }
        })
        viewModel.updateUserResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() == 1){
                    Util.showDialog(requireContext(),response.message,"OK",{
                        findNavController().popBackStack()
                    })
                }
            } else {
                Snackbar.make(binding.root,"Fail", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.rolesResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() == 1){
                    roles = response.roles.toMutableList()
                    setRoleSpinner()
                    val position = roles.indexOfFirst { it.name.equals(user.role) }
                    if (position != -1) {
                        binding.spnRole.setSelection(position)
                    }
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
                    val position = departments.indexOfFirst { it.name.equals(user.department) }
                    if (position != -1) {
                        binding.spnDepartment.setSelection(position)
                    }
                }
            } else {
                Snackbar.make(binding.root,"Fail", Snackbar.LENGTH_SHORT).show()
            }
        })
        binding.spnRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedRoleID = roles[position]._id

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.spnDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedDepartmentId = departments[position]._id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }
}