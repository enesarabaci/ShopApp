package com.example.shopapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import com.example.shopapp.R
import com.example.shopapp.databinding.ActivityBaseBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragment_base)

        binding.apply {
            bottomNavigatinView.setOnNavigationItemSelectedListener(object: BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    if (!bottomNavigatinView.menu.findItem(item.itemId).isChecked) {
                        navController.navigate(item.itemId)
                    }
                    return true
                }
            })
        }


    }
}