package com.example.shopapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.shopapp.Model.QueryEvent
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(val db: FirebaseFirestore) : ViewModel() {

    private val _data = MutableStateFlow<QueryEvent>(QueryEvent.Empty)
    val data: StateFlow<QueryEvent> = _data

    init {
        getCategories()
    }

    private fun getCategories() {
        _data.value = QueryEvent.Loading
        val result = ArrayList<HashMap<String, Any>>()
        db.collection("categories").get().addOnSuccessListener {
            for (d in it.documents) {
                result.add(hashMapOf("category" to d.id, "data" to d.get("data") as ArrayList<HashMap<String, String>>))
            }
            _data.value = QueryEvent.Success(result)
        }.addOnFailureListener { e ->
            _data.value = QueryEvent.Error(e.localizedMessage ?: "Error!")
        }
    }

}