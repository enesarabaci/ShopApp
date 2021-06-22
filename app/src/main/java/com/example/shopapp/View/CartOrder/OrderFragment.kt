package com.example.shopapp.View.CartOrder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.shopapp.Model.Address
import com.example.shopapp.Model.Order
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.ViewModel.OrderViewModel
import com.example.shopapp.databinding.FragmentOrderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order) {

    lateinit var viewModel: OrderViewModel
    private val orderViewModel: OrderViewModel by viewModels()
    private lateinit var binding: FragmentOrderBinding
    private val args: OrderFragmentArgs by navArgs()
    private var price = 0
    private var links = emptyArray<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentOrderBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        viewModel = orderViewModel

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarOrderFragment)
            try {
                setupActionBarWithNavController(findNavController(R.id.fragment_cart_order))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage ?: "Error!", Toast.LENGTH_SHORT).show()
            }
        }
        setHasOptionsMenu(true)
        binding.toolbarOrderFragment.title = "Siparişi Onayla"

        binding.apply {
            toolbarOrderFragment.setNavigationOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    activity?.onBackPressed()
                }
            })
        }

        collectData()

        try {
            price = args.price
            links = args.links
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.localizedMessage ?: "Error!", Toast.LENGTH_SHORT).show()
        }

        binding.fragmentOrderTotalPrice.text = "$price TL"

        binding.fragmentOrderConfirm.setOnClickListener {
            binding.apply {
                viewModel.confirmOrder(
                    Order(
                        Address(
                            fragmentOrderCity.text.toString(),
                            fragmentOrderDistrict.text.toString(),
                            fragmentOrderNeighborhood.text.toString(),
                            fragmentOrderAddress.text.toString()
                        ),
                    fragmentOrderCardNumber.text.toString(),
                    fragmentOrderMonth.text.toString(),
                    fragmentOrderYear.text.toString(),
                    fragmentOrderCvv.text.toString(),
                    price,
                    links.toList()
                    )
                )
            }
        }
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.result.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        Toast.makeText(requireContext(), queryEvent.data as String, Toast.LENGTH_SHORT).show()
                        activity?.onBackPressed()
                    }
                    is QueryEvent.Error -> {
                        Toast.makeText(requireContext(), queryEvent.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

}