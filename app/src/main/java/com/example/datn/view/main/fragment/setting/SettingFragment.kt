package com.example.datn.view.main.fragment.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentSettingBinding
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.auth.AuthActivity
import com.example.datn.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment() {
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        setTabBar()
    }

    override fun setView() {
        if(sharedPreferencesManager.getUserRole().toString().equals("giam_doc") || sharedPreferencesManager.getUserRole().toString().equals("truong_phong")){
            binding.llManageStaff.visibility = View.VISIBLE
        }else{
            binding.llManageStaff.visibility = View.GONE
        }
    }

    override fun setAction() {
        binding.llLogout.setOnClickListener {
            Util.showDialog(requireContext(),"Bạn có muốn đăng xuất?","OK", {
                sharedPreferencesManager.clearUserId()
                sharedPreferencesManager.clearAuthToken()
                sharedPreferencesManager.clearUserRole()
                sharedPreferencesManager.clearDepartment()
                startActivity(Intent(requireActivity(),AuthActivity::class.java))
                requireActivity().finish()
            })
        }
        binding.llManageStaff.setOnClickListener {
            val bundle = Bundle()
            if(sharedPreferencesManager.getUserRole().toString().equals("truong_phong")){
                bundle.putString("idDepartment",sharedPreferencesManager.getDepartment())
                findNavController().navigate(R.id.action_settingFragment_to_listStaffFragment,bundle)
            }else{
                findNavController().navigate(R.id.action_settingFragment_to_departmentFragment)
            }
        }
        binding.llProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment)
        }
    }

    override fun setObserves() {

    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}