package com.example.coinlibrary.ui.favorites.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.coinlibrary.R
import com.example.coinlibrary.databinding.LayoutFavoriteCoinBinding
import com.example.coinlibrary.db.CoinEntity
import com.example.coinlibrary.extension.loadImageWithUrl

class FavoriteListAdapter(private val onFavoriteButtonClicked: (String) -> Unit) :
    Adapter<FavoriteListAdapter.FavoriteCoinViewHolder>() {
    private val items = mutableListOf<CoinEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newList: List<CoinEntity>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class FavoriteCoinViewHolder(private val binding: LayoutFavoriteCoinBinding) :
        ViewHolder(binding.root) {
        fun bind(item: CoinEntity) {
            binding.apply {
                if (item.isFavorited)
                    ivFavorite.setImageResource(R.drawable.ic_heart_filled)
                else
                    ivFavorite.setImageResource(R.drawable.ic_heart_empty)
                ivCoin.loadImageWithUrl(item.image)
                tvCoinName.text = item.name
                tvCoinSymbol.text = item.symbol.uppercase()
                ivFavorite.setOnClickListener {
                    onFavoriteButtonClicked(item.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutFavoriteCoinBinding.inflate(inflater)
        return FavoriteCoinViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FavoriteCoinViewHolder, position: Int) {
        holder.bind(item = items[position])
    }
}