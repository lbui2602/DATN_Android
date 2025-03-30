package com.example.datn.view.main.fragment.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.databinding.FragmentChatBinding
import com.example.datn.models.Message
import com.example.datn.socket.SocketManager
import com.example.datn.util.Util
import com.example.datn.view.main.adapter.ChatAdapter
import com.example.datn.view.main.fragment.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    lateinit var binding : FragmentChatBinding
    private val groupId = "67e8b69bf5444ce17495a213"
    private val senderId = "67e8b63ef5444ce17495a209"
    private val messages = mutableListOf<Message>()
    private lateinit var chatAdapter: ChatAdapter
    private val viewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter(messages)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = chatAdapter

        viewModel.getMessages(groupId)

        setObserves()

        SocketManager.connect()
        SocketManager.joinGroup(groupId)

        SocketManager.socket.on("receive_message") { args ->
            val messageJson = args[0] as JSONObject
            val message = Message(
                messageJson.getString("_id"),
                messageJson.getString("groupId"),
                messageJson.getString("senderId"),
                messageJson.getString("message"),
                messageJson.getString("timestamp")
            )
            requireActivity().runOnUiThread {
                Log.e("Message",message.toString())
                messages.add(message)
                chatAdapter.updateMessages(messages)
            }
        }

        binding.sendButton.setOnClickListener {
            val message = binding.editText.text.toString()
            SocketManager.sendMessage(groupId, senderId, message)
            binding.editText.text.clear()
        }

    }

    private fun setObserves() {
        viewModel.messages.observe(viewLifecycleOwner, Observer { response ->
            if (response != null ) {
                messages.addAll(response)
                chatAdapter.updateMessages(messages)
            } else {
                Log.e("setObservers",response.toString())
                Snackbar.make(binding.root,"Điểm danh thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
