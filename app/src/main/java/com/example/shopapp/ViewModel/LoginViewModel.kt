package com.example.shopapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.EntryEvent
import com.example.shopapp.Repo.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repo: RepositoryInterface) : ViewModel() {

    private val _result = MutableStateFlow<EntryEvent>(EntryEvent.Empty)
    val result: StateFlow<EntryEvent> = _result

    init {
        if (repo.getEmail() != "") {
            _result.value = EntryEvent.Success
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            _result.value = repo.loginUser(email, password)
        }else {
            _result.value = EntryEvent.Error("Boş alanları doldurunuz.")
        }
    }

}