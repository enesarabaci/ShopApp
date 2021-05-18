package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.Repository
import com.example.shopapp.Resource
import com.example.shopapp.Util.Util.BEST_SELLERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repo: Repository,
    val db: FirebaseFirestore,
    val auth: FirebaseAuth
) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val data: StateFlow<QueryEvent> = _data

    private val _favorites = MutableLiveData<List<String>>()
    val favorites: LiveData<List<String>> = _favorites

    private var page = 0
    private var filtering = BEST_SELLERS

    fun searchProduct(search: String) {
        _data.value = QueryEvent.Loading
        page++
        viewModelScope.launch(Dispatchers.IO) {
            val searchTerm = "/arama/$search/"
            val resource = repo.searchProduct(searchTerm, page, filtering)

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

    fun changeFilter(newFilter: String) {
        filtering = newFilter
        page = 0
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