package com.example.datn.view.main.fragment.working_day

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
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.click.IClickAttendance
import com.example.datn.databinding.FragmentDetailWorkingDayBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.models.working_day.DetailWorkingDayResponse
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.AttendanceAdapter
import com.example.datn.view.main.fragment.update_user_info.UpdateUserInfoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class DetailWorkingDayFragment : BaseFragment(), IClickAttendance {
    private val viewModel: DetailWorkingDayViewModel by viewModels()
    private lateinit var binding : FragmentDetailWorkingDayBinding
    var workingDayId = ""
    lateinit var adapter: AttendanceAdapter
    private var list = mutableListOf<Attendance>()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workingDayId = arguments?.getString("workingDayId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailWorkingDayBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDetailWorkingDay(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            workingDayId
        )
    }

    override fun setView() {
        setRecyclerView()
    }
    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AttendanceAdapter(this)
        binding.rcv.adapter = adapter
    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setObserves() {
        viewModel.detailWorkingDayResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() == 1){
                    setData(response)
                }
                else{
                    Util.showDialog(requireContext(),response.message.toString())
                }
            } else {
                Snackbar.make(binding.root,"Fail", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })
    }
    private fun setData(response : DetailWorkingDayResponse ){
        binding.tvFullName.text = response.workingDay.userId.fullName
        binding.tvDepartment.text = response.workingDay.userId.idDepartment.name
        binding.tvRole.text = response.workingDay.userId.roleId.name
        binding.tvDate.text = response.workingDay.date
        binding.tvHour.text = response.workingDay.totalHours.toString()
        Glide.with(requireContext())
            .load(Util.url+response.workingDay.userId.image)
            .into(binding.imgAvatar)
        adapter.submitList(response.workingDay.attendances.toMutableList())
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }
}