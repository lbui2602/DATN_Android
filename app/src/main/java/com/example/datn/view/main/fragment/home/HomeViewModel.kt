package com.example.datn.view.main.fragment.home

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import kotlin.math.*

class HomeViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

}