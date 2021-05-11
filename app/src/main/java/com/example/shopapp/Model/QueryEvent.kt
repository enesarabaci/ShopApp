package com.example.shopapp.Model

sealed class QueryEvent {
    class Success<T>(val data: T) : QueryEvent()
    class Error(val message: String) : QueryEvent()
    object Loading : QueryEvent()
    object Empty : QueryEvent()
}