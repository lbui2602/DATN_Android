package com.example.datn.view.main.fragment.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentSettingBinding
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.auth.AuthActivity
import com.example.datn.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment() {
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInfo("Bearer " + sharedPreferencesManager.getAuthToken())
        setTabBar()
    }

    override fun setView() {
        checkRole()
    }

    private fun checkRole() {
        if (sharedPreferencesManager.getUserRole().toString()
                .equals("giam_doc") || sharedPreferencesManager.getUserRole().toString()
                .equals("truong_phong")
        ) {
            binding.llManageStaff.visibility = View.VISIBLE
            binding.llManageAttendance.visibility = View.VISIBLE
            binding.llManageWorkingDay.visibility = View.VISIBLE
            if (sharedPreferencesManager.getUserRole().toString().equals("giam_doc")) {
                binding.llManageDepartment.visibility = View.VISIBLE
            } else {
                binding.llManageDepartment.visibility = View.GONE
            }
        } else {
            binding.llManageStaff.visibility = View.GONE
            binding.llManageWorkingDay.visibility = View.GONE
            binding.llManageAttendance.visibility = View.GONE
        }

    }

    override fun setAction() {
        binding.llLogout.setOnClickListener {
            Util.showDialog(requireContext(), "Bạn có muốn đăng xuất?", "OK", {
                Util.logout(sharedPreferencesManager)
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            }, "Hủy")
        }
        binding.llManageStaff.setOnClickListener {
            val bundle = Bundle()
            if (sharedPreferencesManager.getUserRole().toString().equals("truong_phong")) {
                bundle.putString("idDepartment", sharedPreferencesManager.getDepartment())
                findNavController().navigate(
                    R.id.action_settingFragment_to_listStaffFragment,
                    bundle
                )
            } else {
                findNavController().navigate(R.id.action_settingFragment_to_departmentFragment)
            }
        }
        binding.llProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment)
        }
        binding.llManageAttendance.setOnClickListener {
            val bundle = Bundle()
            if (!sharedPreferencesManager.getUserRole().toString().equals("giam_doc")) {
                bundle.putString("idDepartment", sharedPreferencesManager.getDepartment())
                findNavController().navigate(
                    R.id.action_settingFragment_to_manageAttendanceFragment,
                    bundle
                )
            } else {
                findNavController().navigate(
                    R.id.action_settingFragment_to_manageAttendanceFragment,
                    bundle
                )
            }
        }
        binding.llManageWorkingDay.setOnClickListener {
            val bundle = Bundle()
            if (!sharedPreferencesManager.getUserRole().toString().equals("giam_doc")) {
                bundle.putString("idDepartment", sharedPreferencesManager.getDepartment())
                findNavController().navigate(
                    R.id.action_settingFragment_to_manageWorkingDayFragment,
                    bundle
                )
            } else {
                findNavController().navigate(
                    R.id.action_settingFragment_to_manageWorkingDayFragment,
                    bundle
                )
            }
        }
        binding.llManageDepartment.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_manageDepartmentFragment)
        }
        binding.llUploadAvatar.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isFromMain",true)
            findNavController().navigate(R.id.action_settingFragment_to_uploadAvatarFragment2,bundle)
        }
    }

    override fun setObserves() {
        viewModel.userInfoResponse.observe(this, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    sharedPreferencesManager.saveUserRole(response.user.roleId)
                    sharedPreferencesManager.saveDepartment(response.user.idDepartment)
                    checkRole()
                    if(response.user.image.isNullOrEmpty()){
                        binding.llUploadAvatar.visibility = View.VISIBLE
                    }else{
                        binding.llUploadAvatar.visibility = View.GONE
                        sharedPreferencesManager.saveImage(response.user.image)
                    }
                } else {
                    Util.showDialog(requireContext(), response.message)
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}