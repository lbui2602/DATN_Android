package com.example.datn.view.main.fragment.history.working_day

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentWorkingDayHistoryBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.models.working_day.WorkingDay
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.AttendanceAdapter
import com.example.datn.view.main.adapter.WorkingDayAdapter
import com.example.datn.view.main.fragment.history.attendance.AttendanceHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.YearMonth
import javax.inject.Inject
import kotlin.getValue
@AndroidEntryPoint
class WorkingDayHistoryFragment : BaseFragment() {
    private lateinit var binding : FragmentWorkingDayHistoryBinding
    private val viewModel: WorkingDayHistoryViewModel by viewModels()
    lateinit var adapter: WorkingDayAdapter
    private var list = mutableListOf<WorkingDay >()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    // Dữ liệu tháng từ 1 đến 12
    val months = (1..12).map { it.toString() }

    // Dữ liệu năm từ 2000 đến 2025
    val years = (2025 downTo 2000).map { it.toString() }
    var selectedMonth = ""
    var selectedYear = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getWorkingDayByUserIdAndMonth(
//            "Bearer "+sharedPreferencesManager.getAuthToken(),
//            sharedPreferencesManager.getUserId().toString(),
//            Util.getCurrentMonth(),Util.getCurrentYear()
//        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.getWorkingDayByUserIdAndMonth(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            sharedPreferencesManager.getUserId().toString(),
            Util.getCurrentMonth(),Util.getCurrentYear()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkingDayHistoryBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }
    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkingDayAdapter(list)
        binding.rcv.adapter = adapter
    }

    override fun setView() {
        setRecyclerView()
        setupSpinners()
    }

    override fun setAction() {
        binding.btnSearch.setOnClickListener {
            viewModel.getWorkingDayByUserIdAndMonth(
                "Bearer "+sharedPreferencesManager.getAuthToken(),
                sharedPreferencesManager.getUserId().toString(),
                selectedMonth,selectedYear
            )
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
                    Log.e("res",response.toString())
                    adapter.updateList(response.workingDays.toMutableList())
                }else{
                    Util.showDialog(requireContext(),"")
                }
            }
        })
        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedMonth = months[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedYear = years[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
    private fun setupSpinners() {

        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonth.adapter = monthAdapter
        binding.spinnerMonth.setSelection(Util.getCurrentMonth().toInt()-1)

        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerYear.adapter = yearAdapter
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }
}