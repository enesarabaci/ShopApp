package com.example.shopapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.Order
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    private val _result = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val result: StateFlow<QueryEvent> = _result

    fun confirmOrder(order: Order) {
        if (
            order.address.city.isEmpty() ||
            order.address.district.isEmpty() ||
            order.address.neighborhood.isEmpty() ||
            order.address.address.isEmpty() ||
            order.cardNumber.isEmpty() ||
            order.month.isEmpty() ||
            order.year.isEmpty() ||
            order.cvv.isEmpty()
        ) {
            _result.value = QueryEvent.Error("Bütün Alanlar Doldurulmalı!")
        } else {
            addToDatabase(order)
        }
    }

    private fun addToDatabase(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repo.saveOrder(order)
            _result.value = QueryEvent.Success(data)
        }
    }

}