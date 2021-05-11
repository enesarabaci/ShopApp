package com.example.shopapp.Model

sealed class EntryEvent(val error: String? = null) {
    object Success : EntryEvent()
    class Error(error: String?) : EntryEvent(error)
    object Empty : EntryEvent()
}