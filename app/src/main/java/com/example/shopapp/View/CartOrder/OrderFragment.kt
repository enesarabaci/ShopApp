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

    private val viewModel: OrderViewModel by viewModels()
    private lateinit var binding: FragmentOrderBinding
    private val args: OrderFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentOrderBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarOrderFragment)
            setupActionBarWithNavController(findNavController(R.id.fragment_cart_order))
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

        binding.fragmentOrderTotalPrice.text = "${args.price} TL"

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
                    args.price,
                    args.links.toList()
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