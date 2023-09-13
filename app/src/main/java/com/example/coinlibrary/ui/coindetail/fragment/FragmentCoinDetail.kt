package com.example.coinlibrary.ui.coindetail.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.coinlibrary.R
import com.example.coinlibrary.compose.BaseFragment
import com.example.coinlibrary.compose.viewBinding
import com.example.coinlibrary.data.base.NetworkResult
import com.example.coinlibrary.databinding.FragmentCoinDetailBinding
import com.example.coinlibrary.extension.clearHtmlTagsWithJSoup
import com.example.coinlibrary.extension.loadImageWithUrl
import com.example.coinlibrary.extension.showView
import com.example.coinlibrary.ui.coindetail.viewmodel.CoinDetailViewModel
import com.example.coinlibrary.ui.coinlist.fragment.FragmentCoinList.Companion.COIN_ID
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartAnimationType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartSymbolStyleType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartSymbolType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentCoinDetail : BaseFragment(R.layout.fragment_coin_detail) {

    private val binding: FragmentCoinDetailBinding by viewBinding(FragmentCoinDetailBinding::bind)
    private val viewModel: CoinDetailViewModel by viewModels()
    private val chartModel = AAChartModel()

    override fun observeVariables() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.coinId.collect {
                        if (!it.isNullOrBlank()) {
                            viewModel.getCoinMarket(it, DEFAULT_DAYS_VALUE, interval = null)
                            viewModel.getCoinDetail(it)
                            viewModel.getCoinFromDb(it)
                        }

                    }
                }

                launch {
                    viewModel.coin.collect {
                        binding.apply {
                            if (it != null) {
                                chartModel.title(it.name)
                                tvCoinPrice.text =
                                    getString(R.string.current_price).replace(
                                        "%%%", it.currentPrice.toString()
                                    )
                                if (it.isFavorited)
                                    ivFavorite.setImageResource(R.drawable.ic_heart_filled)
                                else
                                    ivFavorite.setImageResource(R.drawable.ic_heart_empty)
                            }

                        }
                    }
                }

                launch {
                    viewModel.marketResponse.collect { result ->
                        when (result) {
                            is NetworkResult.Success -> {
                                binding.apply {
                                    chartModel
                                        .series(
                                            arrayOf(
                                                AASeriesElement()
                                                    .data(result.data.prices.toTypedArray())
                                            )
                                        )

                                    chart.aa_drawChartWithChartModel(chartModel)
                                }
                            }

                            is NetworkResult.Loading -> {
                                binding.pbCoinDetail.showView(result.isLoading)
                            }

                            is NetworkResult.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "${result.errorCode} - ${result.errorMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                launch {
                    viewModel.detailResponse.collect { result ->
                        when (result) {
                            is NetworkResult.Success -> {
                                binding.apply {
                                    result.data.apply {
                                        tvCoinName.text = name
                                        ivCoin.loadImageWithUrl(image?.large)
                                        tvCoinSymbol.text =
                                            getString(R.string.symbol).replace(
                                                "%%%",
                                                symbol.uppercase()
                                            )
                                        llHashingAlgorithm.showView(!hashing_algorithm.isNullOrBlank())
                                        tvHashingAlgorithm.text = hashing_algorithm
                                        tvDesc.text =
                                            description?.en?.let { clearHtmlTagsWithJSoup(input = it) }
                                    }
                                }
                            }

                            is NetworkResult.Loading -> {
                                binding.pbCoinDetail.showView(result.isLoading)
                            }

                            is NetworkResult.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    result.errorMessage.toString(),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
                }
            }

        }
    }

    override fun initUI(savedInstanceState: Bundle?) {
        arguments?.getString(COIN_ID).let {
            if (!it.isNullOrEmpty())
                viewModel.changeCoinIdValue(it)
        }

        binding.apply {
            chartModel
                .chartType(AAChartType.Spline)
                .subtitle(getString(R.string.price_change_percentage))
                .animationType(AAChartAnimationType.Elastic)
                .markerSymbol(AAChartSymbolType.Diamond)
                .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)

            ivFavorite.setOnClickListener {
                viewModel.coin.value?.let { coin ->
                    coin.isFavorited.let { isFavorited -> viewModel.favoriteCoin(!isFavorited) }
                }
            }
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSetInterval.setOnClickListener {
                etInterval.text?.let {
                    viewModel.coinId.value?.let { coinId ->
                        viewModel.getCoinMarket(
                            coinId,
                            DEFAULT_DAYS_VALUE, it.toString()
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val DEFAULT_DAYS_VALUE = "1"
    }
}