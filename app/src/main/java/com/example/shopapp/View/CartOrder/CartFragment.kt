package com.example.shopapp.View.CartOrder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Adapter.CartRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.View.ProductDetailActivity
import com.example.shopapp.ViewModel.CartViewModel
import com.example.shopapp.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    private val recyclerAdapter = CartRecyclerAdapter()
    private var totalPrice = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCartBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        collectData()
        setupRecyclerView()

        binding.fragmentCartConfirm.setOnClickListener {
            if (totalPrice > 0) {
                val links = ArrayList<String>()
                recyclerAdapter.list.forEach {
                    if (it.checked) {
                        links.add(it.link)
                    }
                }
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToOrderFragment(links.toTypedArray(), totalPrice))
            }
        }
    }

    override fun onResume() {
        viewModel.getCartProducts()
        super.onResume()
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentCartProgressBar.visibility = View.GONE

                        val list = queryEvent.data as List<Product>
                        recyclerAdapter.list = list
                        viewModel.calculateTotalPrice(ArrayList(list))
                    }
                    is QueryEvent.Error -> {
                        Toast.makeText(requireContext(), queryEvent.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.fragmentCartProgressBar.visibility = View.GONE
                    }
                    is QueryEvent.Loading -> {
                        binding.fragmentCartProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.total.observe(viewLifecycleOwner, {
            totalPrice = it
            binding.fragmentCartTotalPrice.text = "$totalPrice TL"
        })
    }

    private fun setupRecyclerView() {
        binding.fragmentCartRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        recyclerAdapter.setOnItemClickListener { link ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("link", link)
            startActivity(intent)
        }
        recyclerAdapter.setOnItemCheckListener { list ->
            viewModel.calculateTotalPrice(list)
        }
        recyclerAdapter.setOnCountClickListener { link, count, list ->
            viewModel.updateCount(link, count)
            viewModel.calculateTotalPrice(list)
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.fragmentCartRv)
    }

    private val simpleCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val ml = recyclerAdapter.list.toMutableList()
            viewModel.deleteCartProduct(ml.get(position).link)
            ml.removeAt(position)
            recyclerAdapter.list = ml.toList()
            viewModel.calculateTotalPrice(ArrayList(ml))
        }

    }

}