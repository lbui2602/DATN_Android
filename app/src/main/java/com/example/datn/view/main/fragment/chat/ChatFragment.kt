package com.example.datn.view.main.fragment.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentChatBinding
import com.example.datn.models.Message
import com.example.datn.socket.SocketManager
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.view.main.adapter.ChatAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment() {
    private lateinit var binding: FragmentChatBinding
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var groupId : String
    private val senderId = sharedPreferencesManager.getUserId().toString()
    private val messages = mutableListOf<Message>()
    private lateinit var chatAdapter: ChatAdapter
    private val viewModel: ChatViewModel by viewModels()

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments!!.getString("groupId","")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setView() {
        viewModel.getMessages(groupId)
        setupRecyclerView()
        setupSocket()
    }

    override fun setAction() {
        binding.sendButton.setOnClickListener {
            val message = binding.editText.text.toString().trim()
            if (message.isNotEmpty()) {
                SocketManager.sendMessage(groupId, senderId, message)
                binding.editText.text.clear()
            }
        }
    }

    override fun setObserves() {
        viewModel.messages.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                messages.clear()
                messages.addAll(response)
                chatAdapter.updateMessages(messages)
            } else {
                Snackbar.make(binding.root, "Điểm danh thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun setTabBar() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = chatAdapter
    }


    private fun setupSocket() {
        SocketManager.connect()
        SocketManager.joinGroup(groupId)

        SocketManager.socket.on("receive_message") { args ->
            val messageJson = args[0] as JSONObject
            val message = Message(
                messageJson.getString("_id"),
                messageJson.getString("groupId"),
                messageJson.getString("senderId"),
                messageJson.getString("message"),
                messageJson.getString("createdAt"),
                messageJson.getString("updatedAt")
            )

            // Đảm bảo Fragment vẫn còn tồn tại trước khi cập nhật UI
            if (isAdded && view != null) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    Log.e("Message", message.toString())
                    messages.add(message)
                    chatAdapter.updateMessages(messages)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hủy lắng nghe socket khi Fragment bị hủy
        SocketManager.socket.off("receive_message")
    }
}
