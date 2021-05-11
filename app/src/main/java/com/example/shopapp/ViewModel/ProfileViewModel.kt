package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Repo.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private var _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private var _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    init {
        getUserName()
        getEmail()
    }

    private fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _userName.value = repo.getUserName()
            }
        }
    }
    private fun getEmail() {
        _email.value = repo.getEmail()
    }

}