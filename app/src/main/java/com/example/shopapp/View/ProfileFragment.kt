package com.example.shopapp.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shopapp.R
import com.example.shopapp.ViewModel.ProfileViewModel
import com.example.shopapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProfileBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentProfileSignOut.setOnClickListener {
            auth.currentUser?.let {
                auth.signOut()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
        }

        collectData()
    }

    private fun collectData() {
        viewModel.userName.observe(viewLifecycleOwner, {
            binding.fragmentProfileUserName.text= it
        })
        viewModel.email.observe(viewLifecycleOwner, {
            binding.fragmentProfileEmail.text = it
        })
    }

}