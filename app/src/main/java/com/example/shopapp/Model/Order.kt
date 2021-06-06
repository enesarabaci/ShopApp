package com.example.shopapp.Model

import com.google.firebase.Timestamp

data class Order(
    var address: Address,
    var cardNumber: String,
    var month: String,
    var year: String,
    var cvv: String,
    var price: Int,
    var products: List<String>,
    var date: Timestamp? = null,
    var status: String? = null
)