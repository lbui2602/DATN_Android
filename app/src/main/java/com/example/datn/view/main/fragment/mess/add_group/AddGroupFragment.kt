package com.example.datn.view.main.fragment.mess.add_group

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
import com.example.datn.databinding.FragmentAddGroupBinding
import com.example.datn.models.group.AddRequest
import com.example.datn.models.register.User
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.SelectUserAdapter
import com.example.datn.view.main.fragment.mess.create_group.CreateGroupViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue
@AndroidEntryPoint
class AddGroupFragment : BaseFragment(), IClickUserOnline {
    private lateinit var binding : FragmentAddGroupBinding
    private lateinit var adapter : SelectUserAdapter
    private val viewModel : AddGroupViewModel by viewModels()
    var selectedUser = mutableListOf<String>()
    var groupId = ""
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getString("groupId") ?: ""
        viewModel.getAllUser(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            sharedPreferencesManager.getUserId().toString()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddGroupBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {
        setRecyclerView()
    }
    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SelectUserAdapter(this)
        binding.rcv.adapter = adapter
    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnAdd.setOnClickListener {
            Util.showDialog(requireContext(),"Bạn có muốn thêm những người vào vào nhóm ?","OK",{
                viewModel.joinGroup(
                    "Bearer " + sharedPreferencesManager.getAuthToken(),
                    AddRequest(groupId,selectedUser)
                )
            },"Hủy")
        }
    }

    override fun setObserves() {
        viewModel.userResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() ==1){
                    adapter.submitList(response.users)
                }else{
                    Util.showDialog(requireContext(),response.message)
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.joinGroupResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() ==1){
                    Util.showDialog(requireContext(),response.message,"OK",{
                        findNavController().popBackStack()
                    })
                }else{
                    Util.showDialog(requireContext(),response.message)
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
        if(isCheck){
            selectedUser.add(user._id)
        }else{
            selectedUser.remove(user._id)
        }
        check()
    }
    private fun check(){
        if(selectedUser.size > 0){
            binding.btnAdd.visibility = View.VISIBLE
        }else{
            binding.btnAdd.visibility = View.GONE
        }
    }

}