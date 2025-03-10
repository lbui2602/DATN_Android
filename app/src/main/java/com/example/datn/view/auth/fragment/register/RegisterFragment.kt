package com.example.datn.view.auth.fragment.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.datn.R
import com.example.datn.databinding.FragmentRegisterBinding
import com.example.datn.models.department.Department
import com.example.datn.models.role.Role
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding : FragmentRegisterBinding
    private var roles = mutableListOf<Role>()
    private var departments = mutableListOf<Department>()
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
        binding.spnRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = roles[position] // Lấy object MyItem từ danh sách

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.spnDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = departments[position] // Lấy object MyItem từ danh sách

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


    }
    private fun setSpinner(){
        val adapterRoles = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            roles.map { it.name } // Chỉ hiển thị tên
        )
        adapterRoles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnRole.adapter = adapterRoles

        val adapterDepartment = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            departments.map { it.name } // Chỉ hiển thị tên
        )
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnRole.adapter = adapterDepartment
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