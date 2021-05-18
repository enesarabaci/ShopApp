package com.example.shopapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.Repository
import com.example.shopapp.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val data: StateFlow<QueryEvent> = _data

    init {
        getOrders()
    }

    private fun getOrders() {
        _data.value = QueryEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val resource = repo.getOrders()
            when (resource) {
                is Resource.Success -> _data.value = QueryEvent.Success(resource.data)
                is Resource.Message -> _data.value = QueryEvent.Error(resource.message!!)
            }
        }
    }

}