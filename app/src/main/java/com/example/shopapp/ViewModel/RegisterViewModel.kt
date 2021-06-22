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
class RegisterViewModel @Inject constructor(val repo: RepositoryInterface) : ViewModel() {

    private val _result = MutableStateFlow<EntryEvent>(EntryEvent.Empty)
    val result: StateFlow<EntryEvent> = _result

    fun register(firstName: String, lastName: String, email: String, passwordFirst: String, passwordSecond: String) {
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && passwordFirst.isNotEmpty() && passwordSecond.isNotEmpty()) {
            if (passwordFirst == passwordSecond) {
                viewModelScope.launch {
                    _result.value = repo.createUser(email, passwordFirst, firstName, lastName)
                }
            } else {
                _result.value = EntryEvent.Error("Şifreler eşleşmiyor.")
            }
        } else {
            _result.value = EntryEvent.Error("Bütün alanları doldurunuz.")
        }
    }

}