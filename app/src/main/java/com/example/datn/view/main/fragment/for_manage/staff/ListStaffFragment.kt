package com.example.datn.view.main.fragment.for_manage.staff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.click.IClickUser
import com.example.datn.databinding.FragmentListStaffBinding
import com.example.datn.models.department.Department
import com.example.datn.models.staff.AcceptUserRequest
import com.example.datn.models.staff.User
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.auth.fragment.login.LoginViewModel
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.AttendanceAdapter
import com.example.datn.view.main.adapter.UserAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ListStaffFragment : BaseFragment(), IClickUser {
    private lateinit var binding : FragmentListStaffBinding
    private val viewModel: ListStaffViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var idDepartment: String
    lateinit var adapter: UserAdapter
    var list = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idDepartment = arguments?.getString("idDepartment", "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListStaffBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDepartmentById(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            idDepartment
        )
    }
    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserAdapter(this)
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
        viewModel.staffsResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null ) {
                if(response.code.toInt()==1){
                    adapter.submitList(response.users)
                }else{
                    adapter.submitList(mutableListOf())
                    Util.showDialog(requireContext(),response.message.toString())
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
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
        viewModel.acceptUserResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null ) {
                if(response.code.toInt()==1){
                    viewModel.getListUserByDepartmentID(
                        "Bearer "+sharedPreferencesManager.getAuthToken(),
                        idDepartment,""
                    )
                }else{
                    Util.showDialog(requireContext(),response.message.toString())
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }
            else{
                binding.progressBar.visibility = View.GONE
            }
        })
        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                val name = binding.edtSearch.text.toString()
                viewModel.getListUserByDepartmentID(
                    "Bearer "+sharedPreferencesManager.getAuthToken(),
                    idDepartment,name
                )
                Util.hideKeyboard(requireActivity())
                true // Đã xử lý sự kiện
            } else {
                false
            }
        }
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    override fun clickUser(user: User) {
        val gson = Gson()
        val userString = gson.toJson(user)
        val bundle = Bundle()
        bundle.putString("user",userString)
        if(sharedPreferencesManager.getUserId().toString().equals(user._id)){
            bundle.putBoolean("isOwner",true)
        }
        findNavController().navigate(R.id.action_listStaffFragment_to_updateUserInfoFragment,bundle)
    }

    override fun confirmUser(user: User) {
        val mess = if (!user.status) {
            "Bạn có chắc chắn muốn xác nhận tài khoản này?"
        } else {
            "Bạn có chắc chắn muốn khóa tài khoản này?"
        }
        Util.showDialog(requireContext(),mess,"OK",{
            viewModel.acceptUser(
                "Bearer "+sharedPreferencesManager.getAuthToken(),
                AcceptUserRequest(user._id)
            )
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListUserByDepartmentID(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            idDepartment,""
        )
        if(sharedPreferencesManager.getUserRole().toString().equals("nhan_vien")){
            findNavController().popBackStack()
        }
        if(!sharedPreferencesManager.getDepartment().toString().equals(idDepartment)){
            if(!sharedPreferencesManager.getDepartment().toString().equals("ban_giam_doc")){
                findNavController().popBackStack()
            }
        }
    }
}