package com.example.shopapp.View

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopapp.Adapter.OrdersRecyclerAdapter
import com.example.shopapp.Model.Order
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.ViewModel.OrdersViewModel
import com.example.shopapp.databinding.FragmentOrdersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var binding: FragmentOrdersBinding
    private val recyclerAdapter = OrdersRecyclerAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentOrdersBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarOrdersFragment)
            setupActionBarWithNavController(findNavController(R.id.fragment_base))
        }
        setHasOptionsMenu(true)
        binding.toolbarOrdersFragment.title = "Siparişlerim"

        binding.apply {
            toolbarOrdersFragment.setNavigationOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    activity?.onBackPressed()
                }
            })
        }

        collectData()
        setupRecyclerView()
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentOrdersProgressBar.visibility = View.GONE

                        val list = queryEvent.data as List<Order>
                        recyclerAdapter.updateList(list)
                    }
                    is QueryEvent.Error -> {
                        Toast.makeText(requireContext(), queryEvent.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.fragmentOrdersProgressBar.visibility = View.GONE
                    }
                    is QueryEvent.Loading -> {
                        binding.fragmentOrdersProgressBar.visibility = View.VISIBLE
                    }
                }
            }

        }
    }

    private fun setupRecyclerView() {
        binding.fragmentOrdersRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
            setHasFixedSize(true)
        }
    }

}