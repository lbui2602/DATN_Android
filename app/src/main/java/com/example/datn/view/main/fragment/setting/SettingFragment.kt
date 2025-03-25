package com.example.datn.view.main.fragment.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.datn.R
import com.example.datn.databinding.FragmentSettingBinding
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.llLogout.setOnClickListener {
            Util.showDialog(requireContext(),"Bạn có muốn đăng xuất?","OK", {
                    sharedPreferencesManager.clearUserId()
                    sharedPreferencesManager.clearAuthToken()
                    sharedPreferencesManager.clearFaceToken()
                    startActivity(Intent(requireActivity(),AuthActivity::class.java))
                    requireActivity().finish()
                })
        }
    }

}