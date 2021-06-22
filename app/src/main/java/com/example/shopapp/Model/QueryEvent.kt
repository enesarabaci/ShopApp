package com.example.shopapp.Model

sealed class QueryEvent(val error: Boolean = false) {
    class Success<T>(val data: T) : QueryEvent()
    class Error(val message: String) : QueryEvent(error = true)
    object Loading : QueryEvent()
    object Empty : QueryEvent()
}