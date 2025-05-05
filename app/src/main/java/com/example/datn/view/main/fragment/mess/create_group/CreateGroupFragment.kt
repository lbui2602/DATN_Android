package com.example.datn.view.main.fragment.mess.create_group

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.base.BaseFragment
import com.example.datn.click.IClickUserOnline
import com.example.datn.databinding.FragmentCreateGroupBinding
import com.example.datn.models.group.CreateRequest
import com.example.datn.models.register.User
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.SelectUserAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateGroupFragment : BaseFragment(), IClickUserOnline {
    private lateinit var adapter: SelectUserAdapter
    private lateinit var binding: FragmentCreateGroupBinding
    var name = ""
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private val viewModel: CreateGroupViewModel by viewModels()
    var selectedUser = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedUser.add(sharedPreferencesManager.getUserId().toString())
        viewModel.getAllUser(
            "Bearer " + sharedPreferencesManager.getAuthToken(),
            sharedPreferencesManager.getUserId().toString()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateGroupBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {
        setRecyclerView()
    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCreate.setOnClickListener {
            Log.e("selected", selectedUser.toString())
            name = binding.edtNameGroup.text.toString().trim()
            val request = CreateRequest(name, selectedUser)
            viewModel.createGroup("Bearer " + sharedPreferencesManager.getAuthToken(), request)
        }
    }

    private fun check() {
        name = binding.edtNameGroup.text.toString().trim()
        if (!name.isNullOrBlank() && selectedUser.size > 1) {
            binding.btnCreate.visibility = View.VISIBLE
        } else {
            binding.btnCreate.visibility = View.GONE
        }
    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.userResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    adapter.submitList(response.users)
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })

        viewModel.createGroupResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    findNavController().popBackStack()
                } else {
                    Util.showDialog(requireContext(), response.message!!)
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })

        binding.edtNameGroup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Trước khi text thay đổi (ít dùng)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Text đang thay đổi
                Log.d("EditText", "onTextChanged: $s")
                check()
            }

            override fun afterTextChanged(s: Editable?) {
                // Sau khi text thay đổi xong
                Log.d("EditText", "afterTextChanged: ${s.toString()}")
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SelectUserAdapter(this)
        binding.rcv.adapter = adapter
    }

    override fun clickUser(user: User) {

    }

    override fun selectUser(user: User, isCheck: Boolean) {
        if (isCheck) {
            selectedUser.add(user._id)
        } else {
            selectedUser.remove(user._id)
        }
        check()
    }


}