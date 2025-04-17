package com.example.datn.view.main.fragment.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentChatBinding
import com.example.datn.models.message.Message
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.ChatAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
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
    }

    override fun setObserves() {
        viewModel.messages.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.e("Mesage Réponse",response.toString())
                if(response.code.toInt() ==1){
                    messages.clear()
                    messages.addAll(response.messages)
                    chatAdapter.updateMessages(messages)
                    Log.e("isScroll",isScroll.toString())
                    if(isScroll == true){
                        binding.recyclerView.scrollToPosition(messages.size - 1)
                        isScroll = false
                    }
                }
            } ?: Snackbar.make(binding.root, "Lấy tin nhắn thất bại", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages,sharedPreferencesManager.getUserId().toString())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
    }

}
