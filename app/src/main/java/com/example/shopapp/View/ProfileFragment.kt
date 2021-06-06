package com.example.shopapp.View

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopapp.R
import com.example.shopapp.Model.Address
import com.example.shopapp.ViewModel.ProfileViewModel
import com.example.shopapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var numberDialog: Dialog
    private lateinit var addressDialog: BottomSheetDialog
    private var number = ""
    private var address = Address("", "", "", "")

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
        binding.fragmentProfileOrders.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToOrdersFragment())
        }
        numberDialog = Dialog(requireContext())
        binding.fragmentProfileNumber.setOnClickListener {
            numberDialog.setContentView(R.layout.profile_number_dialog)
            val numberInput = numberDialog.findViewById<EditText>(R.id.number_dialog_number)
            numberInput.setText(number)
            val saveInput = numberDialog.findViewById<Button>(R.id.number_dialog_save)
            saveInput.setOnClickListener {
                if (numberInput.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Gerekli Alan Doldurulmalı", Toast.LENGTH_SHORT).show()
                }else {
                    viewModel.saveNumber(numberInput.text.toString())
                    numberDialog.dismiss()
                }
            }

            numberDialog.create()
            numberDialog.show()
        }
        addressDialog = BottomSheetDialog(requireContext())
        binding.fragmentProfileAddress.setOnClickListener {
            addressDialog.setContentView(R.layout.profile_address_dialog)
            val cityInput = addressDialog.findViewById<EditText>(R.id.address_dialog_city).also {
                it?.setText(address.city)
            }
            val districtInput = addressDialog.findViewById<EditText>(R.id.address_dialog_district).also {
                it?.setText(address.district)
            }
            val neighborhoodInput = addressDialog.findViewById<EditText>(R.id.address_dialog_neighborhood).also {
                it?.setText(address.neighborhood)
            }
            val addressInput = addressDialog.findViewById<EditText>(R.id.address_dialog_address).also {
                it?.setText(address.address)
            }
            val saveInput = addressDialog.findViewById<Button>(R.id.address_dialog_save)
            saveInput?.setOnClickListener {
                if (cityInput?.text.toString().isEmpty() ||
                        districtInput?.text.toString().isEmpty() ||
                        neighborhoodInput?.text.toString().isEmpty() ||
                        addressInput?.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Bütün Alanlar Doldurulmalı", Toast.LENGTH_SHORT).show()
                }else {
                    viewModel.saveAddress(Address(
                        cityInput?.text.toString(),
                        districtInput?.text.toString(),
                        neighborhoodInput?.text.toString(),
                        addressInput?.text.toString()
                    ))
                    addressDialog.dismiss()
                }
            }

            addressDialog.create()
            addressDialog.show()
        }

        collectData()
    }

    private fun collectData() {
        viewModel.userName.observe(viewLifecycleOwner, {
            binding.fragmentProfileUserName.text = it
        })
        viewModel.email.observe(viewLifecycleOwner, {
            binding.fragmentProfileEmail.text = it
        })
        viewModel.number.observe(viewLifecycleOwner) {
            number = it
            if (number.isEmpty()) {
                binding.fragmentProfileNumberStatus.text = "Belirtilmedi"
            }else {
                binding.fragmentProfileNumberStatus.text = number
            }
        }
        viewModel.address.observe(viewLifecycleOwner) {
            address = it
            if (address.address.isEmpty()) {
                binding.fragmentProfileAddressStatus.visibility = View.VISIBLE
            }else {
                binding.fragmentProfileAddressStatus.visibility = View.GONE
            }
        }
    }

}