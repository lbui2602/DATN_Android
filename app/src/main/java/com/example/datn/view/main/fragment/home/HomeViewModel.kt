package com.example.datn.view.main.fragment.home

import androidx.lifecycle.ViewModel
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

}