package com.example.shopapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

    }
}