package com.example.datn.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.datn.R
import com.example.datn.databinding.ActivityMainBinding
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.auth.AuthActivity
import com.example.datn.view.main.fragment.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bnvMain.setupWithNavController(navController)
        joinGroup()
        viewModel.connect()
        setObserves()
    }
    private fun getCurrentFragmentTag(): String? {
        val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull()
        return currentFragment?.javaClass?.simpleName
    }
    private fun joinGroup(){
        viewModel.getGroupsByUserId(sharedPreferencesManager.getUserId().toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
        viewModel.disconnect()
    }
    private fun setObserves(){
        viewModel.groupsResponse.observe(this, Observer { response->
            if (response != null) {
                if(response.code.toInt() == 1){
                    response.groups.forEach { gr->
                        viewModel.joinChatGroup(gr._id)
                    }
                }else{
                    Util.showDialog(this,response.message)
                }
            } else {
                Snackbar.make(binding.root,"Fail to load data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.isLogout.observe(this, Observer { isLogout->
            if(isLogout == true){
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        })
        viewModel.message.observe(this, Observer { message->
            if (message != null) {
                val currentFragment = getCurrentFragmentTag()
                if(!currentFragment.equals("ChatFragment")){
                    Util.showCustomSnackbar(
                        view = binding.root,
                        message = message,
                        action = {
                            if (navController.currentDestination?.id != R.id.chatFragment) {
                                lifecycleScope.launch {
                                    val response = viewModel.getGroupById(
                                        "Bearer "+sharedPreferencesManager.getAuthToken().toString(),
                                        message.groupId
                                    )
                                    if(response != null && response.code.toInt() == 1){
                                        val bundle = Bundle().apply {
                                            putString("groupId",response.group._id)
                                            putString("groupName",response.group.name)
                                        }
                                        navController.navigate(R.id.chatFragment,bundle)
                                    }
                                }
                            }
                        }
                    )
                    Util.playTingTingSound(this)
                    viewModel.clearMessage()
                }
            }
        })
    }
}