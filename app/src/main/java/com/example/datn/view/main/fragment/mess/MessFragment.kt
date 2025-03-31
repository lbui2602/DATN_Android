package com.example.datn.view.main.fragment.mess

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentMessBinding
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.fragment.change_password.ChangePasswordViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue
@AndroidEntryPoint
class MessFragment : BaseFragment() {
    private lateinit var binding : FragmentMessBinding
    private val viewModel: MessViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = Bundle().apply {
            putString("groupId","67e8b69bf5444ce17495a213")
        }
        findNavController().navigate(R.id.action_messFragment_to_chatFragment,bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGroupsByUserId(sharedPreferencesManager.getUserId().toString())
    }

    override fun setView() {

    }

    override fun setAction() {

    }

    override fun setObserves() {
        viewModel.groupsResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() == 1){
                    Log.e("MessFragment",response.toString())
                }else{
                    Util.showDialog(requireContext(),response.message)
                }
            } else {
                Snackbar.make(binding.root,"Fail", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun setTabBar() {

    }

}