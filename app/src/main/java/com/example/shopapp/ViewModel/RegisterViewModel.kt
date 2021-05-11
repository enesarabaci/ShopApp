package com.example.shopapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.shopapp.Model.EntryEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val auth: FirebaseAuth, val db: FirebaseFirestore) : ViewModel() {

    private val _result = MutableStateFlow<EntryEvent>(EntryEvent.Empty)
    val result: StateFlow<EntryEvent> = _result

    fun register(firstName: String, lastName: String, email: String, passwordFirst: String, passwordSecond: String) {
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && passwordFirst.isNotEmpty() && passwordSecond.isNotEmpty()) {
            if (passwordFirst == passwordSecond) {
                auth.createUserWithEmailAndPassword(email, passwordFirst).addOnSuccessListener {

                    val hm = HashMap<String, String>()
                    hm.put("firstName", firstName)
                    hm.put("lastName", lastName)
                    db.collection("users")
                        .document(email)
                        .set(hm)
                        .addOnSuccessListener {
                            _result.value = EntryEvent.Success
                        }.addOnFailureListener { exception ->
                            _result.value = EntryEvent.Error(exception.localizedMessage)
                        }


                }.addOnFailureListener { exception ->
                    _result.value = EntryEvent.Error(exception.localizedMessage)
                }
            } else {
                _result.value = EntryEvent.Error("Şifreler eşleşmiyor.")
            }
        } else {
            _result.value = EntryEvent.Error("Bütün alanları doldurunuz.")
        }
    }

}