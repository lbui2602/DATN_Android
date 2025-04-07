package com.example.datn.view.main.fragment.history.attendance

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.AttendanceAdapter
import com.example.datn.view.main.fragment.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
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

    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val dayFormatted = String.format("%02d", dayOfMonth)
                val monthFormatted = String.format("%02d", month + 1)
                val formattedDate = "$dayFormatted-$monthFormatted-$year"
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun setView() {
        setRecyclerView()
    }

    override fun setAction() {
        binding.btnCalendar.setOnClickListener {
            showDatePicker(requireContext(),{
                val request = GetAttendanceByUserIdRequest(
                    sharedPreferencesManager.getUserId().toString(), it
                )
                viewModel.getAttendanceByUserIdAndDate("Bearer "+sharedPreferencesManager.getAuthToken().toString(),request)
            })
        }
    }

    override fun setObserves() {
        viewModel.attendanceResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code.toInt() == 1) {
                if(response.attendances !=null){
                    adapter.updateList(response.attendances.toMutableList())
                }
            }
        })
        viewModel.attendanceByDateResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code.toInt() == 1) {
                if(response.attendances !=null){
                    adapter.updateList(response.attendances.toMutableList())
                }
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }
}