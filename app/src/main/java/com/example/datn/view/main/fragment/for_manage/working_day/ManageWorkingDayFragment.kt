package com.example.datn.view.main.fragment.for_manage.working_day

import android.os.Bundle
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
import com.example.datn.click.IClickWorkingDay
import com.example.datn.databinding.FragmentManageWorkingDayBinding
import com.example.datn.models.attendance.AttendanceRequest
import com.example.datn.models.department.Department
import com.example.datn.models.working_day.WorkingDay
import com.example.datn.models.working_day.WorkingDayXX
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.AttendanceForManageAdapter
import com.example.datn.view.main.adapter.WorkingDayForManageAdapter
import com.example.datn.view.main.fragment.for_manage.attendance.ManageAttendanceFragment
import com.example.datn.view.main.fragment.for_manage.attendance.ManageAttendanceViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ManageWorkingDayFragment : BaseFragment(), IClickWorkingDay {
    private val viewModel : ManageWorkingDayViewModel by viewModels()
    private lateinit var binding: FragmentManageWorkingDayBinding
    val request = AttendanceRequest("","","")
    private var departments = mutableListOf<Department>()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var adapter: WorkingDayForManageAdapter
    var selectedDate = ""
    var search = ""
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        departments.add(Department(_id = "", name = "Xem tất cả", createdAt = null, updatedAt = null, __v = null))
        viewModel.getDepartments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageWorkingDayBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString("idDepartment")?:""
        request.idDepartment = id
        viewModel.getAllWorkingDay("Bearer "+sharedPreferencesManager.getAuthToken(),request)
//        if(!id.isNullOrBlank()){
//            viewModel.getDepartmentById(
//                "Bearer "+sharedPreferencesManager.getAuthToken(),
//                id
//            )
//        }else{
//            binding.tvTitle.text = "Quản lý ngày công"
//        }
    }

    override fun setView() {
//        if(!sharedPreferencesManager.getUserRole().equals("giam_doc")) {
//            binding.spnDepartment.isEnabled = false
//        } else{
//            binding.spnDepartment.isEnabled = true
//        }
        setRecyclerView()
//        binding.edtDate.setText(Util.formatDate())
    }
    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkingDayForManageAdapter(this)
        binding.rcv.adapter = adapter
    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgCalendar.setOnClickListener {
            Util.showDatePicker(requireContext(),{
                selectedDate = it
                binding.edtDate.setText(selectedDate)
            })
        }
        binding.imgDelete.setOnClickListener {
            binding.edtDate.setText("")
            selectedDate = ""
        }
        binding.btnSearch.setOnClickListener {
            search = binding.edtSearch.text.toString().trim()
            request.idDepartment = id
            request.name = search
            request.date = selectedDate
            viewModel.getAllWorkingDay(
                "Bearer "+sharedPreferencesManager.getAuthToken(),
                request
            )
            Util.hideKeyboard(requireActivity())
        }
    }
    private fun setDepartmentSpinner(){
        val adapterDepartment = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            departments.map { it.name }
        )
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDepartment.adapter = adapterDepartment

        if(id.isNullOrBlank()){
            binding.spnDepartment.isEnabled = true
        }else{
            val index = departments.indexOfFirst { it._id == id }
            if (index != -1) {
                binding.spnDepartment.setSelection(index)
                binding.spnDepartment.isEnabled = true
            }
        }
    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.workingDayResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if (response.code.toInt() == 1) {
//                    adapter.submitList(response.attendances.toMutableList())
//                    binding.rcv.scrollToPosition(response.attendances.size - 1)
                    adapter.submitList(response.workingDays)
                }
            }
        })
        viewModel.departmentResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null ) {
                if(response.code.toInt()==1){
//                    binding.tvTitle.text = response.department?.name
                }else{
                    Util.showDialog(requireContext(),response.message.toString())
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.departmentsResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() == 1 && response.departments != null){
                    departments.addAll(response.departments.toMutableList())
                    setDepartmentSpinner()
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        binding.spnDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                this@ManageWorkingDayFragment.id = departments[position]._id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    override fun selectWorkingDay(workingDay: WorkingDay) {

    }

    override fun selectWorkingDay(workingDay: WorkingDayXX) {
        val bundle = Bundle()
        bundle.putString("workingDayId",workingDay._id)
        findNavController().navigate(R.id.action_manageWorkingDayFragment_to_detailWorkingDayFragment,bundle)
    }
}