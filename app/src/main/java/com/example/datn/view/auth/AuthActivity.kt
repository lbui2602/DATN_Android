package com.example.datn.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.datn.R
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        if(!sharedPreferencesManager.getAuthToken().isNullOrEmpty()){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}