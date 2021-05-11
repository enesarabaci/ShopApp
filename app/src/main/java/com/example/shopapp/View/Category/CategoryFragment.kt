package com.example.shopapp.View.Category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCategoryBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }
}