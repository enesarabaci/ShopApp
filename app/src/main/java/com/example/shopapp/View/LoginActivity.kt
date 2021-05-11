package com.example.shopapp.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.Model.EntryEvent
import com.example.shopapp.ViewModel.LoginViewModel
import com.example.shopapp.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenCreated {
            viewModel.result.collect { entryEvent ->
                when (entryEvent) {
                    is EntryEvent.Success -> {
                        startActivity(Intent(this@LoginActivity, BaseActivity::class.java))
                        finish()
                    }
                    is EntryEvent.Error -> {
                        Toast.makeText(this@LoginActivity, entryEvent.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun login(view: View) {
        val email = binding.loginEmail.text.toString()
        val password = binding.loginPassword.text.toString()

        viewModel.login(email, password)
    }

    fun loginToRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

}