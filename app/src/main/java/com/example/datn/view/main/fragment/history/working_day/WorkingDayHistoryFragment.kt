package com.example.datn.view.main.fragment.history.working_day

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWorkingDayByUserIdAndMonth(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            sharedPreferencesManager.getUserId().toString(),
            "4","2025"
        )
    }

    override fun onResume() {
        super.onResume()
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
    }

    override fun setAction() {

    }

    override fun setObserves() {
        viewModel.workingDayResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if (response.code.toInt() == 1) {
                    adapter.updateList(response.workingDays.toMutableList())
                }else{
                    Util.showDialog(requireContext(),"")
                }

            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }
}