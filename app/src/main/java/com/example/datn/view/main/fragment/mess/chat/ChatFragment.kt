package com.example.datn.view.main.fragment.mess.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentChatBinding
import com.example.datn.models.message.Message
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.ChatAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment() {
    private lateinit var binding: FragmentChatBinding
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var groupId : String
    private lateinit var groupName : String
    lateinit var senderId :String
    private val messages = mutableListOf<Message>()
    private lateinit var chatAdapter: ChatAdapter
    private val viewModel: ChatViewModel by viewModels()
    var isScroll = true
    var owner = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getString("groupId", "") ?: ""
        groupName = arguments?.getString("groupName", "") ?: ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setView() {
        lifecycleScope.launch {
            val groupResponse = viewModel.getGroupById(
                "Bearer "+sharedPreferencesManager.getAuthToken(),
                groupId
            )
            if(groupResponse != null){
                if(groupResponse.group.isPrivate != null ){
                    if(groupResponse.group.isPrivate){
                        binding.imgSetting.visibility = View.GONE
                    }
                    else{
                        binding.imgSetting.visibility = View.VISIBLE
                    }
                }
                else{
                    owner = groupResponse.group.owner
                    binding.imgSetting.visibility = View.VISIBLE
                }
            }
        }
        binding.tvTitle.setText(groupName)
        senderId = sharedPreferencesManager.getUserId().toString()
        viewModel.getMessages(groupId)
        viewModel.joinChatGroup(groupId)
        setupRecyclerView()
    }

    override fun setAction() {
        binding.sendButton.setOnClickListener {
            val messageText = binding.editText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(groupId, senderId, messageText)
                binding.editText.text.clear()
                isScroll = true
            }
        }
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgSetting.setOnClickListener {
            val bundle = Bundle().apply {
                putString("groupId",groupId)
                putString("owner",owner)
            }
            findNavController().navigate(R.id.action_chatFragment_to_settingChatFragment,bundle)
        }
    }

    override fun setObserves() {
        viewModel.messages.observe(viewLifecycleOwner) { response ->
            if(response !=null){
                if(response.code.toInt() == 1){
                    messages.clear()
                    messages.addAll(response.messages)
                    chatAdapter.updateMessages(ArrayList(messages))
                    Log.e("isScroll",isScroll.toString())
                    if(isScroll == true){
                        binding.recyclerView.scrollToPosition(messages.size - 1)
                        isScroll = false
                    }
                }
            }else{
                Snackbar.make(binding.root,"Lấy tin nhắn tất bại", Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(sharedPreferencesManager.getUserId().toString())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
    }
}
