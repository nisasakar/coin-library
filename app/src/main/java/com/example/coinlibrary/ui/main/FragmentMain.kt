package com.example.coinlibrary.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.coinlibrary.R
import com.example.coinlibrary.compose.BaseFragment
import com.example.coinlibrary.compose.viewBinding
import com.example.coinlibrary.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMain : BaseFragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private lateinit var navHostFragment: NavHostFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    override fun observeVariables() = Unit

    override fun initUI(savedInstanceState: Bundle?) = Unit
}