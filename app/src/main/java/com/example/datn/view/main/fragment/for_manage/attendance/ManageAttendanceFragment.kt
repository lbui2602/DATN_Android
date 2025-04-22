package com.example.datn.view.main.fragment.for_manage.attendance

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentManageAttendanceBinding
import com.example.datn.models.attendance.AttendanceRequest
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
    val request = AttendanceRequest("","","")
    private val viewModel : ManageAttendanceViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var adapter: AttendanceForManageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageAttendanceBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("idDepartment")
        request.idDepartment = id
        viewModel.getAllAttendance(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            request
        )
        if(!id.isNullOrBlank()){
            viewModel.getDepartmentById(
                "Bearer "+sharedPreferencesManager.getAuthToken(),
                id
            )
        }else{
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
    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.getAllAttendanceResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if (response.code.toInt() == 1) {
                    adapter.submitList(response.attendances.toMutableList())
                    binding.rcv.scrollToPosition(response.attendances.size - 1)
                }
            }
        })
        viewModel.departmentResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null ) {
                if(response.code.toInt()==1){
                    binding.tvTitle.text = response.department?.name
                }else{
                    adapter.submitList(mutableListOf())
                    Util.showDialog(requireContext(),response.message.toString())
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }
}