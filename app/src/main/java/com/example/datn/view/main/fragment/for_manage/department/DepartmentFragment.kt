package com.example.datn.view.main.fragment.for_manage.department

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
import com.example.datn.click.IClickDepartment
import com.example.datn.databinding.FragmentDepartmentBinding
import com.example.datn.models.department.Department
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.DepartmentAdapter
import com.example.datn.view.main.adapter.UserAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentFragment : BaseFragment(), IClickDepartment {
    private lateinit var binding: FragmentDepartmentBinding
    private val viewModel: DepartmentViewModel by viewModels()
    private lateinit var adapter: DepartmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDepartments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDepartmentBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun setView() {
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = DepartmentAdapter(this)
        binding.rcv.adapter = adapter
    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setObserves() {
        viewModel.departmentResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    adapter.submitList(response.departments)
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    override fun clickDepartment(department: Department) {
        val bundle = Bundle()
        bundle.putString("idDepartment", department._id)
        findNavController().navigate(R.id.action_departmentFragment_to_listStaffFragment, bundle)
    }
}