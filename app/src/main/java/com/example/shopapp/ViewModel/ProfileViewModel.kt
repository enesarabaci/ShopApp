package com.example.shopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.Model.Address
import com.example.shopapp.Repo.Repository
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
    private var _number = MutableLiveData<String>()
    val number: LiveData<String> = _number
    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address> = _address

    private var _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    init {
        getUserName()
        getEmail()
        getAddress()
        getNumber()
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

    fun saveAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _address.value = repo.saveAddress(address)
            }
        }
    }

    fun saveNumber(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _number.value = repo.saveNumber(number)
            }
        }
    }

    private fun getAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _address.value = repo.getAddress()
            }
        }
    }

    private fun getNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _number.value = repo.getNumber()
            }
        }
    }

}