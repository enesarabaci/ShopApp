package com.example.shopapp

sealed class Resource<T>(val data: T?, val message: String?) {
    class Success<T>(data: T) : Resource<T>(data, null)
    class Message<T>(data: String) : Resource<T>(null, data)
}
