package com.example.datn.view.main.fragment.history.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.base.BaseFragment
import com.example.datn.click.IClickAttendance
import com.example.datn.databinding.FragmentAttendanceHistoryBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.AttendanceAdapter
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
//        viewModel.getAttendanceByUserId(
//            "Bearer "+sharedPreferencesManager.getAuthToken().toString(),
//            sharedPreferencesManager.getUserId().toString())
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAttendanceByUserId(
            "Bearer "+sharedPreferencesManager.getAuthToken().toString(),
            sharedPreferencesManager.getUserId().toString())
    }

    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AttendanceAdapter(this)
        binding.rcv.adapter = adapter
    }


    override fun setView() {
        setRecyclerView()
    }

    override fun setAction() {
        binding.btnCalendar.setOnClickListener {
            Util.showDatePicker(requireContext(),{
                val request = GetAttendanceByUserIdRequest(
                    sharedPreferencesManager.getUserId().toString(), it
                )
                viewModel.getAttendanceByUserIdAndDate("Bearer "+sharedPreferencesManager.getAuthToken().toString(),request)
            })
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
        viewModel.attendanceByDateResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code.toInt() == 1) {
                if(response.attendances !=null){
                    adapter.submitList(response.attendances.toMutableList())
                }
            }else{
                adapter.submitList(mutableListOf())
                Util.showDialog(requireContext(),response?.message?: "")
            }
        })
        viewModel.attendanceResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if (response.code.toInt() == 1) {
                    if(response.attendances !=null){
                        adapter.submitList(response.attendances.toMutableList())
                    }
                }
                else{
                    Util.showDialog(requireContext(),response.message.toString())
                }
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }
}