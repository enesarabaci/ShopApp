package com.example.shopapp.Model

class Product(
    var title: String,
    var img: String,
    var price: String,
    var link: String,
    var star: String? = null,
    var features: ArrayList<String>? = null,
    var images: ArrayList<String>? = null,
    var category: String? = null
) {
    var count = 0
    var checked = true
}