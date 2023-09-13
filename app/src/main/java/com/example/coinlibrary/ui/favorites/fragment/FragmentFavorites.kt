package com.example.coinlibrary.ui.favorites.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coinlibrary.R
import com.example.coinlibrary.compose.BaseFragment
import com.example.coinlibrary.compose.viewBinding
import com.example.coinlibrary.databinding.FragmentFavoritesBinding
import com.example.coinlibrary.extension.showView
import com.example.coinlibrary.ui.favorites.adapter.FavoriteListAdapter
import com.example.coinlibrary.ui.favorites.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentFavorites : BaseFragment(R.layout.fragment_favorites) {

    private val binding: FragmentFavoritesBinding by viewBinding(FragmentFavoritesBinding::bind)
    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var adapter: FavoriteListAdapter

    override fun observeVariables() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoritedCoins.collect {
                    binding.tvNoResult.showView(it.isEmpty())
                    adapter.updateItems(it)
                }
            }
        }

    }

    override fun initUI(savedInstanceState: Bundle?) {
        adapter = FavoriteListAdapter { coinId ->
            viewModel.removeCoinFromFavorites(coinId)
        }

        viewModel.getFavoritedCoins()
        binding.apply {
            rvFavCoinList.adapter = adapter
            srlFavCoinList.setOnRefreshListener {
                viewModel.getFavoritedCoins()
                srlFavCoinList.isRefreshing = false
            }
        }
    }

}