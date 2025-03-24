package com.example.datn.view.main.fragment.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _attendanceResponse = MutableLiveData<AttendanceResponse?>()
    val attendanceResponse: LiveData<AttendanceResponse?> get() = _attendanceResponse
}