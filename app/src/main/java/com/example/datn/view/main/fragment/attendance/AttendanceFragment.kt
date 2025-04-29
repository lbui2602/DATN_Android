package com.example.datn.view.main.fragment.attendance

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datn.base.BaseFragment
import com.example.datn.click.IClickAttendance
import com.example.datn.databinding.FragmentAttendanceBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.adapter.AttendanceAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceFragment : BaseFragment(),IClickAttendance {
    lateinit var binding : FragmentAttendanceBinding
    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var imageCapture: ImageCapture
    lateinit var file : File
    lateinit var adapter: AttendanceAdapter
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    var startTime: Long = 0
    var endTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
        }else{
            startCamera()
        }
        val request = GetAttendanceByUserIdRequest(
            sharedPreferencesManager.getUserId().toString(),Util.formatDate()
        )
        viewModel.getAttendanceByUserIdAndDate("Bearer "+sharedPreferencesManager.getAuthToken().toString(),request)


    }

    override fun setView() {
        setRecyclerView()
    }

    override fun setAction() {
        binding.imgCapture.setOnClickListener {
            viewModel.capturePhoto(requireContext(),imageCapture)
            startTime = System.currentTimeMillis()
        }
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AttendanceAdapter(this)
        binding.rcv.adapter = adapter
    }

    override fun setObserves() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.file.observe(viewLifecycleOwner, Observer { response ->
            if(response !=null){
                this.file = response
            }
        })
        viewModel.attendanceResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if (response.code.toInt() == 1) {
                    endTime = System.currentTimeMillis()
                    val elapsedTime = endTime - startTime
                    Log.d("ExecutionTime", "Chạy hết $elapsedTime ms")
                    Util.showDialog(requireContext(),"Bạn đã điểm danh thành công!","OK",{
                        adapter.submitList(response.attendances.toMutableList())
                        binding.rcv.scrollToPosition(response.attendances.toMutableList().size)
                    })
                } else {
                    Util.showDialog(requireContext(),response.message.toString())
                }
            }
        })
        viewModel.attendanceByDateResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response != null){
                if (response.code.toInt() == 1) {
                    if(response.attendances !=null){
                        adapter.submitList(response.attendances.toMutableList())
                        binding.rcv.scrollToPosition(response.attendances.toMutableList().size - 1)
                    }
                }
            }
        })
    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(186, 261)) // ép ảnh đầu ra thành 640x480
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture) // 🛠 BIND imageCapture vào cameraProvider
            } catch (exc: Exception) {
                Log.e("CameraFragment", "Camera use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                }
            }
        }
    }
}