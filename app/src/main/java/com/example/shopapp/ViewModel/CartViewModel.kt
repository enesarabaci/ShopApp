package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.RepositoryInterface
import com.example.shopapp.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val data: StateFlow<QueryEvent> = _data

    private val _total = MutableLiveData<Int>(0)
    val total: LiveData<Int> = _total

    fun getCartProducts() {
        _data.value = QueryEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val resource = repo.getCartProducts()
            when (resource) {
                is Resource.Success -> {
                    _data.value = QueryEvent.Success(resource.data)
                }
                is Resource.Message -> {
                    _data.value = QueryEvent.Error(resource.message!!)
                }
            }
        }
    }

    fun calculateTotalPrice(list: ArrayList<Product>) {
        var price = 0
        list.forEach { product ->
            if (product.checked) {
                price += (product.price.replace(".", "").toInt() * product.count)
            }
        }
        _total.value = price
    }

    fun deleteCartProduct(link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteCartProduct(link)
        }
    }

    fun updateCount(link: String, count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateCount(link, count)
        }
    }

}