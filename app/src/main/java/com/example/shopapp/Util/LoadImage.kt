package com.example.shopapp.Util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?) {
    url?.let {
        if (url.isNotEmpty()) {
            Glide.with(context).load(url).into(this)
        }
    }
}