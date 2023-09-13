package com.example.coinlibrary.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.example.coinlibrary.R
import com.example.coinlibrary.compose.BaseFragment
import com.example.coinlibrary.compose.viewBinding
import com.example.coinlibrary.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSplashScreen : BaseFragment(R.layout.fragment_splash_screen) {

    private val binding: FragmentSplashScreenBinding by viewBinding(FragmentSplashScreenBinding::bind)

    companion object {
        const val SPLASH_SCREEN_DELAY_TIME: Long = 2000
    }

    override fun observeVariables() = Unit

    override fun initUI(savedInstanceState: Bundle?) {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_fragmentSplashScreen_to_fragmentLogin)
        }, SPLASH_SCREEN_DELAY_TIME)
    }
}