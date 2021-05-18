package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.Repository
import com.example.shopapp.Resource
import com.example.shopapp.Util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryResultViewModel @Inject constructor(
    val repo: Repository,
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val data: StateFlow<QueryEvent> = _data

    private val _favorites = MutableLiveData<ArrayList<String>>()
    val favorites: LiveData<ArrayList<String>> = _favorites

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
        auth.currentUser?.email?.let { email ->
            db.collection("users").document(email).collection("favorites")
                .add(hashMapOf("link" to link))
                .addOnSuccessListener {
                    getFavorites()
                }
        }
    }

    fun deleteFromFavorites(link: String) {
        auth.currentUser?.email?.let { email ->
            db.collection("users").document(email).collection("favorites")
                .whereEqualTo("link", link)
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        it.reference.delete()
                    }
                    getFavorites()
                }
        }
    }

    fun getFavorites() {
        auth.currentUser?.email?.let { email ->
            val ff = ArrayList<String>()
            db.collection("users").document(email).collection("favorites")
                .get().addOnSuccessListener {
                    ff.clear()
                    for (ds in it) {
                        ff.add(ds.get("link") as String)
                    }
                    _favorites.value = ff
                }
        }
    }

}