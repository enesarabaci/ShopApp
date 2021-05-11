package com.example.shopapp.Model

import com.google.firebase.Timestamp

data class Comment(
    val userName: String,
    val comment: String,
    val date: Timestamp
)