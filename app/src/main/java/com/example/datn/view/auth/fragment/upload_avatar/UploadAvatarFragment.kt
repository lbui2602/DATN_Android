package com.example.datn.view.auth.fragment.upload_avatar

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        }else{
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
        viewModel.multipartFile.observe(viewLifecycleOwner, Observer { file->
            if(file !=null){
                this.file = file
            }
        })
        viewModel.detectFaceResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                print(response.toString())
                face_token =response.faces.get(0).face_token
                lifecycleScope.launch {
                    viewModel.updateFaceToken(FaceTokenRequest(sharedPreferencesManager.getUserId().toString(),response.faces.get(0).face_token))
                }
            } else {
                Snackbar.make(binding.root,"Thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.updateFaceTokenResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null && response.code.toInt() ==1) {
                lifecycleScope.launch {
                    delay(500)
                    viewModel.addFaceToFaceSet(
                        BuildConfig.face_api_key,
                        BuildConfig.face_api_secret,
                        BuildConfig.outer_id,
                        face_token)
                }
            } else {
                Snackbar.make(binding.root,"Thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.addFaceResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null) {
                print(response.toString())
                lifecycleScope.launch {
                    if(file !=null){
                        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), sharedPreferencesManager.getUserId().toString())
                        viewModel.uploadAvatar(userId,file)
                    }else{
                        Log.e("hi","hi")
                    }
                }
            } else {
                Snackbar.make(binding.root,"Thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.uploadAvatarResponse.observe(viewLifecycleOwner, Observer { response->
            if (response != null && response.code.toInt() ==1) {
                Util.showDialog(requireContext(),response.message,"OK",{
                    findNavController().popBackStack(R.id.loginFragment,true)
                })
            } else {
                Snackbar.make(binding.root,"Thất bại", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading->
            if(isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
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