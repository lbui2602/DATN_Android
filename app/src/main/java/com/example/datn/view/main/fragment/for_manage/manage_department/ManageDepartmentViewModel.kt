package com.example.datn.view.main.fragment.for_manage.manage_department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.department.CreateDepartmentRequest
import com.example.datn.models.department.DepartmentResponse
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.department.UpdateDepartmentRequest
import com.example.datn.models.training.TrainingResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageDepartmentViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _departmentResponse = MutableLiveData<DepartmentsResponses?>()
    val departmentResponse: LiveData<DepartmentsResponses?> get() = _departmentResponse

    private val _updateResponse = MutableLiveData<DepartmentResponse?>()
    val updateResponse: LiveData<DepartmentResponse?> get() = _updateResponse

    private val _deleteResponse = MutableLiveData<TrainingResponse?>()
    val deleteResponse: LiveData<TrainingResponse?> get() = _deleteResponse

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

    fun updateDepartment(token : String,id:String, request: UpdateDepartmentRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.updateDepartment(token,id,request)
                if(response!=null){
                    _updateResponse.postValue(response)
                }
            } catch (e: Exception) {
                print(e.toString())
                _updateResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun createDepartment(token : String, request: CreateDepartmentRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.createDepartment(token,request)
                if(response!=null){
                    _updateResponse.postValue(response)
                }
            } catch (e: Exception) {
                print(e.toString())
                _updateResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun deleteDepartment(token : String,id:String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.deleteDepartment(token,id)
                if(response!=null){
                    _deleteResponse.postValue(response)
                }
            } catch (e: Exception) {
                print(e.toString())
                _deleteResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}