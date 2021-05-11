package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.Comment
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

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val data: StateFlow<QueryEvent> = _data

    private val _sameProducts = MutableLiveData<List<Product>>()
    val sameProducts: LiveData<List<Product>> = _sameProducts

    private val _favorites = MutableLiveData<List<String>>()
    val favorites: LiveData<List<String>> = _favorites

    private val _message = MutableLiveData<String>("")
    val message: LiveData<String> = _message

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    fun getData(link: String?) {
        _data.value = QueryEvent.Loading
        link?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val resource = repo.getProductDetail(link)
                when (resource) {
                    is Resource.Success -> {
                        _data.value = QueryEvent.Success(resource.data)
                    }
                    is Resource.Message -> {
                        _data.value = QueryEvent.Error(resource.message!!)
                    }
                }
            }
        } ?: kotlin.run {
            _data.value = QueryEvent.Error("Link Error!")
        }
    }

    fun getSameProducts(name: String?) {
        name?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val searchTerm = "/arama/$it/"
                val resource = repo.searchProduct(searchTerm)
                when (resource) {
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _sameProducts.value = resource.data!!
                        }
                    }
                }
            }
        }
    }

    fun addToFavorites(link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addFavoriteProduct(link)
            getFavorites()
        }
    }

    fun deleteFromFavorites(link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavoriteProduct(link)
            getFavorites()
        }
    }

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val resource = repo.getFavoriteLinks()
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        withContext(Dispatchers.Main) {
                            _favorites.value = it
                        }
                    }
                }
            }
        }
    }

    fun addToCart(link: String?) {
        link?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val message = repo.addToCart(link)
                withContext(Dispatchers.Main) {
                    _message.value = message
                }
            }
        }
    }

    fun sendComment(comment: String, link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val message = repo.sendComment(comment, link)
            withContext(Dispatchers.Main) {
                _message.value = message
            }
            getComments(link)
        }
    }

    fun getComments(link: String?) {
        link?.let {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    _comments.value = repo.getComments(it)
                }
            }
        }
    }

}