package com.example.shopapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.Model.EntryEvent
import com.example.shopapp.ViewModel.RegisterViewModel
import com.example.shopapp.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launchWhenCreated {
            viewModel.result.collect{ entryEvent ->
                when (entryEvent) {
                    is EntryEvent.Success -> {
                        onBackPressed()
                    }
                    is EntryEvent.Error -> {
                        Toast.makeText(this@RegisterActivity, entryEvent.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun register(view: View) {
        val firstName = binding.registerFirstName.text.toString()
        val lastName = binding.registerLastName.text.toString()
        val email = binding.registerEmail.text.toString()
        val passwordFirst = binding.registerPasswordFirst.text.toString()
        val passwordSecond = binding.registerPasswordSecond.text.toString()

        viewModel.register(firstName, lastName, email, passwordFirst, passwordSecond)
    }

    fun registerToLogin(view: View) {
        onBackPressed()
    }

}