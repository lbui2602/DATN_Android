package com.example.datn.view.main.fragment.attendance

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
import com.example.datn.BuildConfig
import com.example.datn.databinding.FragmentAttendanceBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AttendanceFragment : Fragment() {
    lateinit var binding : FragmentAttendanceBinding
    private val viewModel: AttendanceViewModel by viewModels()
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
        setObservers()
    }

    private fun setObservers() {
        viewModel.detectFaceResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                print(response.toString())
                val api_key = RequestBody.create("text/plain".toMediaTypeOrNull(), BuildConfig.face_api_key)
                val api_secret = RequestBody.create("text/plain".toMediaTypeOrNull(), BuildConfig.face_api_secret)
                val face_token = RequestBody.create("text/plain".toMediaTypeOrNull(), response.faces.get(0).face_token)
                val outer_id = RequestBody.create("text/plain".toMediaTypeOrNull(), "datn_lbui")
                lifecycleScope.launch {
                    delay(500)
                    viewModel.addFaceToFaceSet(BuildConfig.face_api_key,BuildConfig.face_api_secret,"datn_lbui",response.faces.get(0).face_token)
                }
            } else {
                Snackbar.make(binding.root,"Thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.addFaceResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                print(response.toString())
            } else {
                Snackbar.make(binding.root,"Thất bại", Snackbar.LENGTH_SHORT).show()
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

    private fun capturePhoto() {
        Log.e("CameraFragment", "capture")
        val file = File(requireContext().externalMediaDirs.first(), "lbui.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.e("CameraFragment", "capture2")
                    val resizedFile = resizeImageFile(file, 1024) // Resize ảnh xuống tối đa 1024px
                    val imageUri = Uri.fromFile(resizedFile)
                    binding.img.setImageURI(imageUri)
                    // Gửi ảnh đã resize lên API
                    uploadImage(resizedFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                }
            })
    }

    fun resizeImageFile(file: File, maxSize: Int): File {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val rotatedBitmap = fixImageRotation(file, bitmap)
        val resizedBitmap = resizeBitmap(rotatedBitmap, maxSize)

        // Lưu ảnh mới vào file
        val resizedFile = File(file.parent, "resized_${file.name}")
        val outputStream = FileOutputStream(resizedFile)
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // Nén 80%
        outputStream.flush()
        outputStream.close()

        return resizedFile
    }

    // Hàm resize ảnh (giữ tỉ lệ gốc)
    fun resizeBitmap(image: Bitmap, maxSize: Int): Bitmap {
        val width = image.width
        val height = image.height
        val ratio: Float = width.toFloat() / height.toFloat()

        val newWidth: Int
        val newHeight: Int
        if (ratio > 1) {
            newWidth = maxSize
            newHeight = (maxSize / ratio).toInt()
        } else {
            newHeight = maxSize
            newWidth = (maxSize * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(image, newWidth, newHeight, true)
    }

    // Hàm kiểm tra và sửa hướng xoay ảnh
    fun fixImageRotation(file: File, bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(file.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap // Không cần xoay
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun uploadImage(file: File) {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image_file", file.name, requestFile)
        val api_key = RequestBody.create("text/plain".toMediaTypeOrNull(), BuildConfig.face_api_key)
        val api_secret = RequestBody.create("text/plain".toMediaTypeOrNull(), BuildConfig.face_api_secret)
        val faceset_token = RequestBody.create("text/plain".toMediaTypeOrNull(), BuildConfig.faceset_token)
        viewModel.detectFace(body,api_key, api_secret)
    }
}