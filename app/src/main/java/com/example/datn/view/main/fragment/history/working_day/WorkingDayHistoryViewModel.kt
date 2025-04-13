package com.example.datn.view.main.fragment.history.working_day

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.attendance.AttendanceByUserIdResponse
import com.example.datn.models.working_day.WorkingDayByMonthResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkingDayHistoryViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {
    private val _workingDayResponse = MutableLiveData<WorkingDayByMonthResponse?>()
    val workingDayResponse: LiveData<WorkingDayByMonthResponse?> get() = _workingDayResponse

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun getWorkingDayByUserIdAndMonth(token: String, userId: String, month: String, year: String){
        print("luong start")
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getWorkingDayByUserIdAndMonth(token,userId,month,year)
                Log.e("respon viewmodel",response.toString())
                if(response!=null){
                    _workingDayResponse.postValue(response)
                }
                else{
                    _workingDayResponse.postValue(null)
                }

            } catch (e: Exception) {
                _workingDayResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}