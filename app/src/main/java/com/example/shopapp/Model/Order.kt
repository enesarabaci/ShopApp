package com.example.shopapp.Model

import com.google.firebase.Timestamp
import java.util.*

data class Order(
    var city: String,
    var district: String,
    var neighborhood: String,
    var address: String,
    var cardNumber: String,
    var month: String,
    var year: String,
    var cvv: String,
    var price: Int,
    var products: List<String>,
    var date: Timestamp? = null,
    var status: String? = null
)