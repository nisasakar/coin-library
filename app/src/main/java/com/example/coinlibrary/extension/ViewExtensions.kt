package com.example.coinlibrary.extension

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.loadImageWithUrl(url: String?) {
    url?.let {
        Glide.with(context).load(it).diskCacheStrategy(DiskCacheStrategy.ALL).into(this)
    }
}

fun View.showView(visible: Boolean) {
    when (visible) {
        true -> this.visibility = View.VISIBLE
        false -> this.visibility = View.GONE
    }
}