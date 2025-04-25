package com.example.datn.view.main.fragment.attendance

import android.content.Context
import android.util.JsonToken
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.BuildConfig
import com.example.datn.models.attendance.AttendanceByDateResponse
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.models.face_api.AddFaceResponse
import com.example.datn.models.face_api.CompareFaceResponse
import com.example.datn.models.face_api.DetectFaceResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _attendanceResponse = MutableLiveData<AttendanceResponse?>()
    val attendanceResponse: LiveData<AttendanceResponse?> get() = _attendanceResponse

    private val _attendanceByDateResponse = MutableLiveData<AttendanceByDateResponse?>()
    val attendanceByDateResponse: LiveData<AttendanceByDateResponse?> get() = _attendanceByDateResponse

    private val _file = MutableLiveData<File?>()
    val file: LiveData<File?> get() = _file

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun capturePhoto(context : Context, imageCapture: ImageCapture) {
        _isLoading.value = true
        Log.e("CameraFragment", "capture")
        val file = File(context.externalMediaDirs.first(), "lbui.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.e("CameraFragment", "capture2")
//                    val resizedFile = Util.resizeImageFile(file)
//                    _file.value = resizedFile
//                    uploadImageToCompare(resizedFile)
                    uploadImage(file)
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                }
            })
    }
    fun uploadImage(file: File) {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val fileName = RequestBody.create("text/plain".toMediaTypeOrNull(), sharedPreferencesManager.getImage().toString())
        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), sharedPreferencesManager.getUserId().toString())
        val time = RequestBody.create("text/plain".toMediaTypeOrNull(), Util.formatTime())
        val date = RequestBody.create("text/plain".toMediaTypeOrNull(),Util.formatDate())
//        attendance("Bearer "+sharedPreferencesManager.getAuthToken().toString(),
//            body,
//            userId,
//            time,date)
        compare("Bearer "+sharedPreferencesManager.getAuthToken().toString(),
            body,
            fileName,
            userId,
            time,date)
    }
    fun attendance(token: String,image :MultipartBody.Part,userId: RequestBody,time : RequestBody,date : RequestBody){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.attendance(token, image,userId, time, date)
                if(response != null){
                    _attendanceResponse.postValue(response)
                }
                else{
                    Log.e("attendance ","null")
                    _attendanceResponse.postValue(null)
                }

            } catch (e: Exception) {
                Log.e("attendance ",e.toString())
                _attendanceResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun compare(token: String,image :MultipartBody.Part,fileName : RequestBody,userId: RequestBody,time : RequestBody,date : RequestBody){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.compare(token, image,fileName,userId, time, date)
                if(response != null){
                    _attendanceResponse.postValue(response)
                }
                else{
                    Log.e("attendance ","null")
                    _attendanceResponse.postValue(null)
                }

            } catch (e: Exception) {
                Log.e("attendance ",e.toString())
                _attendanceResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun getAttendanceByUserIdAndDate(token:String,request : GetAttendanceByUserIdRequest){
        Log.e("getAttendanceByUserIdAndDate","hi")
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getAttendanceByUserIdAndDate(token, request)
                if(response!=null){
                    _attendanceByDateResponse.postValue(response)
                }
                else{
                    _attendanceByDateResponse.postValue(null)
                }

            } catch (e: Exception) {
                Log.e("getAttendanceByUserIdAndDate",e.toString())
                _attendanceByDateResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}