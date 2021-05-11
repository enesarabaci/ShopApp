package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.shopapp.Resource
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Loading)
    val data: StateFlow<QueryEvent> = _data

    private val _favorites = MutableLiveData<List<String>>(listOf())
    val favorites: LiveData<List<String>> = _favorites

    init {
        getOpportunityProducts()
    }

    private fun getOpportunityProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val resource = repo.getOpportunityProducts()
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

}