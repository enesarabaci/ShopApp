package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.Repository
import com.example.shopapp.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val data: StateFlow<QueryEvent> = _data

    private val _message = MutableLiveData<String>("")
    val message: LiveData<String> = _message

    var search = ""

    fun getFavorites() {
        _data.value = QueryEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val resource = repo.getFavoriteProducts()
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

    fun deleteFavoriteProduct(link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavoriteProduct(link)
        }
    }

    fun addToCart(link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val message = repo.addToCart(link)
            withContext(Dispatchers.Main) {
                _message.value = message
            }
        }
    }

    fun makeFiltering(list: ArrayList<Product>) : ArrayList<Product> {
        val result = ArrayList<Product>()
        list.forEach {
            if (it.title.contains(search, ignoreCase = true)) {
                result.add(it)
            }
        }
        return result
    }

}