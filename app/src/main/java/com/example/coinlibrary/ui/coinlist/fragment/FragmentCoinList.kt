package com.example.coinlibrary.ui.coinlist.fragment

import android.os.Bundle
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.coinlibrary.R
import com.example.coinlibrary.compose.BaseFragment
import com.example.coinlibrary.compose.viewBinding
import com.example.coinlibrary.data.base.NetworkResult
import com.example.coinlibrary.databinding.FragmentCoinListBinding
import com.example.coinlibrary.extension.showView
import com.example.coinlibrary.ui.coinlist.adapter.CoinListAdapter
import com.example.coinlibrary.ui.coinlist.viewmodel.CoinListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentCoinList : BaseFragment(R.layout.fragment_coin_list) {

    private val binding: FragmentCoinListBinding by viewBinding(FragmentCoinListBinding::bind)
    private val viewModel: CoinListViewModel by viewModels()

    private lateinit var adapter: CoinListAdapter

    override fun observeVariables() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.coinListResponse.collect { result ->
                        when (result) {
                            is NetworkResult.Success -> {
                                viewModel.insertCoinsToDb(result.data)
                            }

                            is NetworkResult.Loading -> {
                                if (result.isLoading) {
                                    binding.pbCoinList.showView(true)
                                    binding.rvCoinList.showView(false)
                                } else {
                                    binding.pbCoinList.showView(false)
                                    binding.rvCoinList.showView(true)
                                }
                            }

                            else -> {}
                        }
                    }
                }
                launch {
                    viewModel.coinsFromDb.collect {
                        adapter.updateItems(it)
                    }
                }
            }
        }
    }

    override fun initUI(savedInstanceState: Bundle?) {
        adapter = CoinListAdapter { coinId ->
            val bundle = Bundle()
            bundle.putString(COIN_ID, coinId)
            findNavController().navigate(R.id.action_fragmentCoinList_to_fragmentCoinDetail, bundle)
        }

        viewModel.getCoinsToDb()
        viewModel.getCoinsBySearch(query = "")

        binding.apply {
            rvCoinList.adapter = adapter
            srlCoinList.setOnRefreshListener {
                viewModel.getCoinsToDb()
                viewModel.getCoinsBySearch(query = "")
                srlCoinList.isRefreshing = false
            }
            svCoinList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query != null) {
                        viewModel.getCoinsBySearch(query = query)
                    }
                    return false
                }
            })

        }
    }

    companion object {
        const val COIN_ID = "coin_id"
    }
}
