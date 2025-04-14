package com.example.datn.view.main.fragment.working_day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.update_user.UpdateUserRequest
import com.example.datn.models.working_day.DetailWorkingDayResponse
import com.example.datn.models.working_day.WorkingDay
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailWorkingDayViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _detailWorkingDayResponse = MutableLiveData<DetailWorkingDayResponse?>()
    val detailWorkingDayResponse: LiveData<DetailWorkingDayResponse?> get() = _detailWorkingDayResponse

    fun getDetailWorkingDay(token : String, workingDayId: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getDetailWorkingDay(token,workingDayId)
                if(response != null){
                    _detailWorkingDayResponse.postValue(response)
                }
            } catch (e: Exception) {
                _detailWorkingDayResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}