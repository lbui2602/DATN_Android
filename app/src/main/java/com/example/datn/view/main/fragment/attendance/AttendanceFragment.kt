package com.example.datn.view.main.fragment.attendance

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.datn.BuildConfig
import com.example.datn.click.IClickAttendance
import com.example.datn.databinding.FragmentAttendanceBinding
import com.example.datn.models.attendance.Attendance
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.auth.adapter.AttendanceAdapter
import com.example.datn.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceFragment : Fragment(),IClickAttendance {
    lateinit var binding : FragmentAttendanceBinding
    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var imageCapture: ImageCapture
    lateinit var file : MultipartBody.Part
    lateinit var adapter: AttendanceAdapter
    private var list = mutableListOf<Attendance>()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
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
        binding.imgCapture.setOnClickListener {
            viewModel.capturePhoto(requireContext(),imageCapture)
        }
        setView()
        setRecyclerView()
        setObservers()
    }

    private fun setView() {
        val request = GetAttendanceByUserIdRequest(
            sharedPreferencesManager.getUserId().toString(),Util.formatDate()
        )
        viewModel.getAttendanceByUserIdAndDate("Bearer "+sharedPreferencesManager.getAuthToken().toString(),request)
    }

    private fun setRecyclerView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AttendanceAdapter(list,this)
        binding.rcv.adapter = adapter
    }

    private fun setObservers() {
        viewModel.attendanceResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code.toInt() == 1) {
                adapter.updateList(response.attendances.toMutableList())
            } else {
                Log.e("setObservers",response.toString())
                Snackbar.make(binding.root,"Điểm danh thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.attendanceByDateResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code.toInt() == 1) {
                if(response.attendances !=null){
                    adapter.updateList(response.attendances.toMutableList())
                }
            }
        })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()

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