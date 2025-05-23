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
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentRegisterBinding
import com.example.datn.models.department.Department
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.role.Role
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    lateinit var binding: FragmentRegisterBinding
    private var roles = mutableListOf<Role>()
    private var departments = mutableListOf<Department>()
    private val viewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    var selectedRoleID = ""
    var selectedDepartmentId = ""
    val months = (1..12).map { it.toString() }

    // Dữ liệu năm từ 2000 đến 2025
    val years = (2025 downTo 1950).map { it.toString() }
    val days = (1..31).map { it.toString() }

    var selectedDay = ""
    var selectedMonth = ""
    var selectedYear = ""
    var gender = "Nam"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {
        setupSpinners()
        binding.radioGender.check(R.id.radioNam)
    }

    private fun setupSpinners() {

        val monthAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnMonth.adapter = monthAdapter

        val yearAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnYear.adapter = yearAdapter

        val dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, days)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDay.adapter = dayAdapter
    }

    override fun setAction() {
        binding.imgVisible.setOnClickListener {
            viewModel.changVisiblePassword()
        }
        binding.imgVisibleConfirm.setOnClickListener {
            viewModel.changVisiblePasswordConfirm()
        }
        binding.llBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnRegister.setOnClickListener {
            val fullname = binding.edtFullname.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val passwordConfirm = binding.edtPasswordConfirm.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()
            val birthday = selectedDay + "-" + selectedMonth + "-" + selectedYear
            validate(fullname, email, password, passwordConfirm, phone, address) {
                viewModel.register(
                    RegisterRequest(
                        fullname,
                        email,
                        password,
                        phone,
                        address,
                        birthday,
                        gender,
                        selectedDepartmentId,
                        selectedRoleID
                    )
                )
            }
//            findNavController().navigate(R.id.action_registerFragment_to_uploadAvatarFragment)
        }
    }

    override fun setObserves() {
        binding.radioGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioNam -> {
                    gender = "Nam"
                }

                R.id.radioNu -> {
                    gender = "Nữ"
                }
            }
        }
        binding.spnDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val day = days[position].toInt()
                selectedDay = if (day < 10) "0$day" else "$day"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spnMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val month = months[position].toInt()
                selectedMonth = if (month < 10) "0$month" else "$month"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spnYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedYear = years[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    sharedPreferencesManager.saveUserId(response.user._id)
                    findNavController().navigate(R.id.action_registerFragment_to_uploadAvatarFragment)
                } else {
                    Util.showDialog(requireContext(), response.message)
                }
            } else {
                Snackbar.make(binding.root, "Fail to register account", Snackbar.LENGTH_SHORT)
                    .show()
            }
        })

        binding.spnRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedRoleID = roles[position]._id

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.spnDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDepartmentId = departments[position]._id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        viewModel.rolesResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    roles = response.roles.toMutableList()
                    setRoleSpinner()
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.departmentsResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1 && response.departments != null) {
                    departments = response.departments.toMutableList()
                    setDepartmentSpinner()
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.isVisible.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible == true) {
                binding.imgVisible.setImageResource(R.drawable.ic_visible_off)
                binding.edtPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.imgVisible.setImageResource(R.drawable.ic_visible)
                binding.edtPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
        })

        viewModel.isVisibleConfirm.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible == true) {
                binding.imgVisibleConfirm.setImageResource(R.drawable.ic_visible_off)
                binding.edtPasswordConfirm.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.imgVisibleConfirm.setImageResource(R.drawable.ic_visible)
                binding.edtPasswordConfirm.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edtPasswordConfirm.setSelection(binding.edtPasswordConfirm.text!!.length)
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnRegister.isEnabled = false
                binding.llBack.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
                binding.llBack.isEnabled = true
            }
        })
    }

    override fun setTabBar() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        viewModel.getRoles()
        viewModel.getDepartments()
    }

    private fun setRoleSpinner() {
        val adapterRoles = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            roles.map { it.name }
        )
        adapterRoles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnRole.adapter = adapterRoles
    }

    private fun setDepartmentSpinner() {
        val adapterDepartment = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            departments.map { it.name }
        )
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDepartment.adapter = adapterDepartment
    }

    private fun validate(
        fullname: String,
        email: String,
        password: String,
        passwordConfirm: String,
        phone: String,
        address: String,
        action: () -> Unit
    ) {
        if (fullname.isNullOrBlank()) {
            Util.showDialog(requireContext(), getString(R.string.msg_register_fullname))
        } else if (email.isNullOrBlank()) {
            Util.showDialog(requireContext(), getString(R.string.msg_login_email))
        } else if (password.isNullOrBlank()) {
            Util.showDialog(requireContext(), getString(R.string.msg_login_password))
        } else if (password.length < 8) {
            Util.showDialog(requireContext(), "Vui lòng nhập mật khẩu từ 8 kí tự trở lên.")
        } else if (passwordConfirm.isNullOrBlank()) {
            Util.showDialog(requireContext(), getString(R.string.msg_login_password_confirm))
        } else if (!password.equals(passwordConfirm)) {
            Util.showDialog(requireContext(), getString(R.string.msg_password_no_match))
        } else if (phone.isNullOrBlank()) {
            Util.showDialog(requireContext(), getString(R.string.msg_register_phone))
        } else if (address.isNullOrBlank()) {
            Util.showDialog(requireContext(), getString(R.string.msg_register_address))
        } else {
            action()
        }
    }
}