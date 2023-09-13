package com.example.coinlibrary.ui.coinlist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.coinlibrary.data.model.CoinItem
import com.example.coinlibrary.databinding.LayoutItemCoinBinding
import com.example.coinlibrary.extension.loadImageWithUrl
import com.example.coinlibrary.R

class CoinListAdapter(private val onItemClick: (String) -> Unit) :
    Adapter<CoinListAdapter.CoinViewHolder>() {

    private val items = mutableListOf<CoinItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newList: List<CoinItem>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class CoinViewHolder(private val binding: LayoutItemCoinBinding) :
        ViewHolder(binding.root) {
        fun bind(item: CoinItem) {
            binding.apply {
                root.setOnClickListener {
                    onItemClick(item.id)
                }
                tvCoinName.text = item.name
                tvCoinSymbol.text = item.symbol.uppercase()
                ivCoin.loadImageWithUrl(item.image)
                tvCoinPrice.text = item.currentPrice.toString()
                item.changePercent.let {
                    tvCoinPriceChange.text = it.toString()
                    if (it != null) {
                        ivCoinPriceChange.setBackgroundResource(
                            if (it.toString()
                                    .contains("-")
                            ) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemCoinBinding.inflate(inflater)
        return CoinViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(item = items[position])
    }
}