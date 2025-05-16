package com.example.datn.view.main.fragment.for_manage.manage_department

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.click.IClickDepartment
import com.example.datn.databinding.FragmentDepartmentBinding
import com.example.datn.databinding.FragmentManageDepartmentBinding
import com.example.datn.models.department.CreateDepartmentRequest
import com.example.datn.models.department.Department
import com.example.datn.models.department.UpdateDepartmentRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.DepartmentAdapter
import com.example.datn.view.main.adapter.ManageDepartmentAdapter
import com.example.datn.view.main.fragment.for_manage.department.DepartmentViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ManageDepartmentFragment : BaseFragment(), IClickDepartment {
    private lateinit var binding: FragmentManageDepartmentBinding
    private val viewModel: ManageDepartmentViewModel by viewModels()
    private lateinit var adapter: DepartmentAdapter

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDepartments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageDepartmentBinding.inflate(layoutInflater, container, false)
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

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val department = adapter.getItemAt(position)

                if (direction == ItemTouchHelper.RIGHT) {
                    adapter.notifyItemChanged(position)
                    deleteDepartment(department)
                } else if (direction == ItemTouchHelper.LEFT) {
                    adapter.notifyItemChanged(position)
                    Util.showAddOrUpdateDialog(requireContext(), true, department) { name ->
                        updateDepartment(department._id, UpdateDepartmentRequest(name))
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val itemView = viewHolder.itemView
                val paint = Paint()
                val textPaint = Paint()
                textPaint.color = Color.WHITE
                textPaint.textSize = 40f
                textPaint.isAntiAlias = true

                val textMargin = 50f

                if (dX > 0) {
                    // Vuốt phải - XÓA
                    paint.color = Color.RED
                    c.drawRect(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.left + dX,
                        itemView.bottom.toFloat(),
                        paint
                    )
                    c.drawText(
                        "Xóa",
                        itemView.left + textMargin,
                        itemView.top + itemView.height / 2f + 15f,
                        textPaint
                    )
                } else if (dX < 0) {
                    // Vuốt trái - CẬP NHẬT
                    paint.color = Color.BLUE
                    c.drawRect(
                        itemView.right + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat(),
                        paint
                    )
                    val textWidth = textPaint.measureText("Cập nhật")
                    c.drawText(
                        "Cập nhật",
                        itemView.right - textMargin - textWidth,
                        itemView.top + itemView.height / 2f + 15f,
                        textPaint
                    )
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rcv)

    }

    override fun setAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnAdd.setOnClickListener {
            Util.showAddOrUpdateDialog(requireContext(), false) { name ->
                val id = Util.convertToSnakeCase(name)
                viewModel.createDepartment(
                    "Bearer " + sharedPreferencesManager.getAuthToken(),
                    CreateDepartmentRequest(id, name)
                )
            }
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
        viewModel.updateResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    Util.showDialog(requireContext(), response.message.toString(), "OK", {
                        viewModel.getDepartments()
                    })
                } else {
                    Util.showDialog(requireContext(), response.message.toString())
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.deleteResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.code.toInt() == 1) {
                    Util.showDialog(requireContext(), response.message.toString(), "OK", {
                        viewModel.getDepartments()
                    })
                }
            } else {
                Snackbar.make(binding.root, "Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    private fun deleteDepartment(department: Department) {
        Util.showDialog(
            requireContext(), "Bạn có chắc chắn muốn xóa phòng ban này ?", "OK",
            {
                viewModel.deleteDepartment(
                    "Bearer " + sharedPreferencesManager.getAuthToken(),
                    department._id
                )
            }, "Hủy"
        )
    }

    private fun updateDepartment(id: String, request: UpdateDepartmentRequest) {
        viewModel.updateDepartment("Bearer " + sharedPreferencesManager.getAuthToken(), id, request)
    }

    override fun clickDepartment(department: Department) {

    }
}