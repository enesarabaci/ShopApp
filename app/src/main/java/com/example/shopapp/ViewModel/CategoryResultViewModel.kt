package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.RepositoryInterface
import com.example.shopapp.Resource
import com.example.shopapp.Util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryResultViewModel @Inject constructor(
    val repo: RepositoryInterface
) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val data: StateFlow<QueryEvent> = _data

    private val _favorites = MutableLiveData<List<String>>()
    val favorites: LiveData<List<String>> = _favorites

    private var page = 0
    private var filtering = Util.BEST_SELLERS

    fun getCategoryResult(link: String) {
        page++
        _data.value = QueryEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val resource = repo.searchProduct(link, page, filtering)
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