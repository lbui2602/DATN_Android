package com.example.datn.view.main.fragment.mess.setting_chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.click.IClickUserOnline
import com.example.datn.databinding.FragmentSettingChatBinding
import com.example.datn.models.group.LeaveRequest
import com.example.datn.models.register.User
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.OnlineUserAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingChatFragment : BaseFragment(), IClickUserOnline {
    private lateinit var binding: FragmentSettingChatBinding
    var groupId = ""
    private val viewModel : SettingChatViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var adapter: OnlineUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getString("groupId") ?: ""
        viewModel.getUserInGroup(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            groupId
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInGroup(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            groupId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingChatBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {
        setRecyclerView()
    }
    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = OnlineUserAdapter(this)
        binding.rcv.adapter = adapter
    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.llAdd.setOnClickListener {
            val bundle = Bundle().apply {
                putString("groupId",groupId)
            }
            findNavController().navigate(R.id.action_settingChatFragment_to_addGroupFragment,bundle)
        }
        binding.llLeft.setOnClickListener {
            Util.showDialog(requireContext(),"Bạn có muốn rời nhóm này?","OK",{
                 viewModel.leaveGroup(
                     "Bearer "+sharedPreferencesManager.getAuthToken(),
                     LeaveRequest(sharedPreferencesManager.getUserId().toString(),groupId)
                 )
            },"Hủy")
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
        viewModel.userResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() ==1){
                    adapter.submitList(response.users)
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })

        viewModel.leaveGroupResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() ==1){
                    findNavController().popBackStack(R.id.messFragment,false)
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    override fun clickUser(user: User) {

    }

    override fun selectUser(user: User, isCheck: Boolean) {

    }
}