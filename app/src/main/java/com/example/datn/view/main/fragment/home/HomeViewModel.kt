package com.example.datn.view.main.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

}