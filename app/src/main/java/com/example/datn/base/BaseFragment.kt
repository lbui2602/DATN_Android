package com.example.datn.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun setView()
    abstract fun setAction()
    abstract fun setObserves()
    abstract fun setTabBar()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setTabBar()
        setAction()
        setObserves()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

}