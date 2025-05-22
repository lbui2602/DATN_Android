package com.example.datn.view.main.fragment.for_manage.attendance

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentManageAttendanceBinding
import com.example.datn.models.attendance.AttendanceRequest
import com.example.datn.models.department.Department
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.AttendanceAdapter
import com.example.datn.view.main.adapter.AttendanceForManageAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageAttendanceFragment : BaseFragment() {
    private lateinit var binding: FragmentManageAttendanceBinding
    val request = AttendanceRequest("", "", "")
    private var departments = mutableListOf<Department>()
    private val viewModel: ManageAttendanceViewModel by viewModels()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var adapter: AttendanceForManageAdapter
    var selectedDate = ""
    var search = ""
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedPreferencesManager.getUserRole().equals("giam_doc")) {
            departments.add(
                Department(
                    _id = "",
                    name = "Xem tất cả",
                    createdAt = null,
                    updatedAt = null,
                    __v = null
                )
            )
        }
        viewModel.getDepartments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageAttendanceBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString("idDepartment") ?: ""
        request.idDepartment = id
        viewModel.getAllAttendance(
            "Bearer " + sharedPreferencesManager.getAuthToken(),
            request
        )
        if (!id.isNullOrBlank()) {
            viewModel.getDepartmentById(
                "Bearer " + sharedPreferencesManager.getAuthToken(),
                id
            )
        } else {
            binding.tvTitle.text = "Quản lý chấm công"
        }
    }

    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AttendanceForManageAdapter()
        binding.rcv.adapter = adapter
    }

    override fun setView() {
        setRecyclerView()
    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgCalendar.setOnClickListener {
            Util.showDatePicker(requireContext(), {
                selectedDate = it
                binding.edtDate.setText(selectedDate)
            })
        }
        binding.btnSearch.setOnClickListener {
            search = binding.edtSearch.text.toString().trim()
            request.idDepartment = id
            request.name = search
            request.date = selectedDate
            println("luong " + request.toString())
            viewModel.getAllAttendance(
                "Bearer " + sharedPreferencesManager.getAuthToken(),
                request
            )
            Util.hideKeyboard(requireActivity())
        }
        binding.imgDelete.setOnClickListener {
            binding.edtDate.setText("")
            selectedDate = ""
        }
    }

    private fun setDepartmentSpinner() {
        val adapterDepartment = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            departments.map { it.name }
        )
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDepartment.adapter = adapterDepartment

        if (id.isNullOrBlank()) {
            binding.spnDepartment.isEnabled = true
        } else {
            val index = departments.indexOfFirst { it._id == id }
            if (index != -1) {
                binding.spnDepartment.setSelection(index)
                binding.spnDepartment.isEnabled = false
            }
        }

    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnSearch.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnSearch.isEnabled = true
            }
        })
        viewModel.getAllAttendanceResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    adapter.submitList(response.attendances.toMutableList())
//                    binding.rcv.scrollToPosition(response.attendances.size - 1)
                }
            }
        })
        viewModel.departmentResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
//                    binding.tvTitle.text = response.department?.name
                } else {
                    adapter.submitList(mutableListOf())
                    Util.showDialog(requireContext(), response.message.toString())
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.departmentsResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1 && response.departments != null) {
                    departments.addAll(response.departments.toMutableList())
                    setDepartmentSpinner()
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        binding.spnDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                this@ManageAttendanceFragment.id = departments[position]._id
                Log.e("id", this@ManageAttendanceFragment.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }
}