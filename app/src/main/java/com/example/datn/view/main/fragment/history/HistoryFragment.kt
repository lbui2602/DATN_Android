package com.example.datn.view.main.fragment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.view.main.MainActivity

class HistoryFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun setView() {

    }

    override fun setAction() {

    }

    override fun setObserves() {

    }

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }
}