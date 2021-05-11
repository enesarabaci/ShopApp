package com.example.shopapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.shopapp.Model.EntryEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val auth: FirebaseAuth) : ViewModel() {

    private val _result = MutableStateFlow<EntryEvent>(EntryEvent.Empty)
    val result: StateFlow<EntryEvent> = _result

    init {
        auth.currentUser?.let {
            _result.value = EntryEvent.Success
        }
    }

    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                _result.value = EntryEvent.Success
            }.addOnFailureListener { exception ->
                _result.value = EntryEvent.Error(exception.localizedMessage)
            }
        }else {
            _result.value = EntryEvent.Error("Boş alanları doldurunuz.")
        }
    }

}