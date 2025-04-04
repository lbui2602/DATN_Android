package com.example.datn.view.main.fragment.history.attendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.click.IClickAttendance
import com.example.datn.click.IClickMess
import com.example.datn.databinding.FragmentAttendanceBinding
import com.example.datn.databinding.FragmentAttendanceHistoryBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.view.main.adapter.AttendanceAdapter
import com.example.datn.view.main.fragment.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class AttendanceHistoryFragment : BaseFragment(), IClickAttendance {
    private val viewModel: AttendanceHistoryViewModel by viewModels()
    private lateinit var binding : FragmentAttendanceHistoryBinding
    lateinit var adapter: AttendanceAdapter
    private var list = mutableListOf<Attendance>()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendanceHistoryBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAttendanceByUserId(
            "Bearer "+sharedPreferencesManager.getAuthToken().toString(),
            sharedPreferencesManager.getUserId().toString())
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAttendanceByUserId(
            "Bearer "+sharedPreferencesManager.getAuthToken().toString(),
            sharedPreferencesManager.getUserId().toString())
    }
    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AttendanceAdapter(list,this)
        binding.rcv.adapter = adapter
    }

    override fun setView() {
        setRecyclerView()
    }

    override fun setAction() {

    }

    override fun setObserves() {
        viewModel.attendanceResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code.toInt() == 1) {
                if(response.attendances !=null){
                    adapter.updateList(response.attendances.toMutableList())
                }
            }
        })
    }

    override fun setTabBar() {

    }
}