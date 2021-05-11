package com.example.shopapp.View.MainBase

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentMainBaseBinding

class MainBaseFragment : Fragment(R.layout.fragment_main_base) {

    private lateinit var binding: FragmentMainBaseBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainBaseBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

    }
}