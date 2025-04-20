package com.example.datn.view.main.fragment.for_manage.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.group.GroupsResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _departmentResponse = MutableLiveData<DepartmentsResponses?>()
    val departmentResponse: LiveData<DepartmentsResponses?> get() = _departmentResponse

    fun getDepartments(){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getDepartments()
                if(response!=null){
                    _departmentResponse.postValue(response)
                }

            } catch (e: Exception) {
                print(e.toString())
                _departmentResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}