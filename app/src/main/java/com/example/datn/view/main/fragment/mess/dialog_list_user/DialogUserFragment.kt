package com.example.datn.view.main.fragment.mess.dialog_list_user

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.R
import com.example.datn.click.IClickUserOnline
import com.example.datn.databinding.FragmentDialogUserBinding
import com.example.datn.models.register.User
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.adapter.OnlineUserAdapter
import com.example.datn.view.main.adapter.UserAdapter
import com.example.datn.view.main.fragment.mess.MessViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class DialogUserFragment : DialogFragment(), IClickUserOnline {
    private lateinit var binding : FragmentDialogUserBinding
    private val viewModel: DialogUserViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var adapter: OnlineUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllUser(
            "Bearer "+sharedPreferencesManager.getAuthToken(),
            sharedPreferencesManager.getUserId().toString()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogUserBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserves()
        setRecyclerView()
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setWindowAnimations(R.style.DialogSlideAnimation)

            val params = window.attributes
            params.width = (resources.displayMetrics.widthPixels * 0.8).toInt() // Chiếm 90% màn hình
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            params.gravity = Gravity.END

            window.attributes = params
            window.setBackgroundDrawableResource(android.R.color.transparent) // Xóa background trắng mặc định
        }
    }
    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = OnlineUserAdapter(this)
        binding.rcv.adapter = adapter
    }
    private fun setObserves() {
        viewModel.userResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                if(response.code.toInt() ==1){
                    adapter.submitList(response.users)
                }
            } else {
                Snackbar.make(binding.root,"Fail", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun clickUser(user: User) {
        lifecycleScope.launch {
            val response = viewModel.getPrivateGroup(
                "Bearer "+sharedPreferencesManager.getAuthToken().toString(),
                user._id
            )
            if(response!=null && response.code.toInt()==1){
                val bundle = Bundle().apply {
                    putString("groupId",response.group._id)
                    putString("groupName",response.group.name)
                }
                dismiss()
                findNavController().navigate(R.id.action_messFragment_to_chatFragment,bundle)
            }
        }
    }
}