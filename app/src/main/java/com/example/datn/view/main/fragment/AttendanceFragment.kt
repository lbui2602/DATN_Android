package com.example.datn.view.main.fragment

import android.content.pm.PackageManager
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
import com.example.datn.RetrofitClient
import com.example.datn.databinding.FragmentAttendanceBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AttendanceFragment : Fragment() {
    lateinit var binding : FragmentAttendanceBinding
    private lateinit var imageCapture: ImageCapture
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
            capturePhoto()
        }
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
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
    private fun capturePhoto() {
        Log.e("CameraFragment", "capture")
        val file = File(requireContext().externalMediaDirs.first(), "lbui.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.e("CameraFragment", "capture2")
                    val imageUri = Uri.fromFile(file)
                    binding.img.setImageURI(imageUri)

                    // Gửi ảnh lên API
                    uploadImage(file)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                }
            })
    }

    private fun uploadImage(file: File) {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val fullName = RequestBody.create("text/plain".toMediaTypeOrNull(), "Nguyen Van A")
        val email = RequestBody.create("text/plain".toMediaTypeOrNull(), "anv@gmail.com")
        val password = RequestBody.create("text/plain".toMediaTypeOrNull(), "11111111")
        val phone = RequestBody.create("text/plain".toMediaTypeOrNull(), "0976705402")
        val address = RequestBody.create("text/plain".toMediaTypeOrNull(), "HN")
        val roleId = RequestBody.create("text/plain".toMediaTypeOrNull(), "2")
        val idDepartment = RequestBody.create("text/plain".toMediaTypeOrNull(), "1")

        RetrofitClient.instance.uploadImage(fullName, email, password, phone, address, roleId, idDepartment, body)
            .enqueue(object : Callback<com.example.datn.Response> {
                override fun onResponse(call: Call<com.example.datn.Response>, response: Response<com.example.datn.Response>) {
                    if (response.isSuccessful) {
                        val uploadResponse = response.body()
                        Log.e("CameraFragment", "Upload thành công: ${uploadResponse?.message}")
                    } else {
                        Log.e("CameraFragment", "Lỗi khi upload: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<com.example.datn.Response>, t: Throwable) {
                    Log.e("CameraFragment", "Upload thất bại: ${t.message}")
                }
            })

    }

}