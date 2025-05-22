package com.example.datn.view.auth.fragment.upload_avatar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
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
import androidx.navigation.fragment.findNavController
import com.example.datn.BuildConfig
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentUploadAvatarBinding
import com.example.datn.models.face_token.FaceTokenRequest
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.fragment.attendance.AttendanceViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
@AndroidEntryPoint
class UploadAvatarFragment : BaseFragment() {
    private lateinit var binding : FragmentUploadAvatarBinding
    private lateinit var imageCapture: ImageCapture
    private val viewModel: UploadAvatarViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var file : MultipartBody.Part
    var face_token = ""
    var isFromMain = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFromMain = arguments?.getBoolean("isFromMain") ?: false
        if(isFromMain){
            (requireActivity() as MainActivity).binding.bnvMain.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadAvatarBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
        } else {
            startCamera()
        }
    }

    override fun setView() {

    }

    override fun setAction() {
        binding.imgCapture.setOnClickListener {
            viewModel.capturePhoto(requireContext(),imageCapture)
        }
    }

    override fun setObserves() {
        viewModel.uploadAvatarResponse.observe(viewLifecycleOwner, Observer { response->
            if(response != null){
                if (response.code.toInt() == 1) {
                    Util.showDialog(requireContext(),response.message,"OK",{
                        if(isFromMain){
                            findNavController().popBackStack()
                        }else {
                            findNavController().popBackStack(R.id.loginFragment, true)
                        }
                    })
                } else {
                    Util.showDialog(requireContext(),response.message)
                }
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
                binding.imgCapture.isEnabled = false
            }else{
                binding.progressBar.visibility = View.GONE
                binding.imgCapture.isEnabled = true
            }
        })
    }

    override fun setTabBar() {
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

    override fun onDestroy() {
        super.onDestroy()
        if(!isFromMain){
            sharedPreferencesManager.clearUserId()
            sharedPreferencesManager.clearAuthToken()
            sharedPreferencesManager.clearUserRole()
            sharedPreferencesManager.clearDepartment()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    // Người dùng từ chối cấp quyền
                    Util.showDialog(
                        requireContext(),
                        "Ứng dụng cần quyền truy cập máy ảnh để tiếp tục. Vui lòng bật quyền trong Cài đặt.",
                        "Đi đến cài đặt",
                        {
                            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", requireContext().packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        },
                        "Huỷ",
                        {}
                    )
                }
            }
        }
    }

}