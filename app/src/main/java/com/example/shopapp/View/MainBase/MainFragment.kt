package com.example.shopapp.View.MainBase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopapp.Adapter.ProductsRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.View.ProductDetailActivity
import com.example.shopapp.ViewModel.MainViewModel
import com.example.shopapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val recyclerAdapter = ProductsRecyclerAdapter(R.layout.product_item)
    lateinit var extras: FragmentNavigator.Extras

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        extras = FragmentNavigatorExtras(binding.fragmentMainBaseSearch to "search_second")

        binding.apply {
            fragmentMainBaseSearch.setOnClickListener {
                findNavController().navigate(
                    R.id.action_mainFragment_to_searchFragment,
                    null,
                    null,
                    extras
                )
            }
        }
        viewModel.getFavorites()

        collectData()
        setupRecyclerView()


    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentMainProgressBar.visibility = View.GONE
                        binding.fragmentMainTitle.visibility = View.VISIBLE
                        val list = queryEvent.data as List<Product>
                        recyclerAdapter.list = list
                    }
                    is QueryEvent.Error -> {
                        Toast.makeText(requireContext(), queryEvent.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.fragmentMainProgressBar.visibility = View.GONE
                    }
                    is QueryEvent.Loading -> {
                        binding.fragmentMainProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.favorites.observe(viewLifecycleOwner, {
            recyclerAdapter.updateFavorites(it)
        })
    }

    private fun setupRecyclerView() {
        binding.fragmentMainRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = recyclerAdapter
            isNestedScrollingEnabled = false
        }
        recyclerAdapter.setOnFavoriteClickListener { link, add ->
            if (add) {
                viewModel.addToFavorites(link)
            }else {
                viewModel.deleteFromFavorites(link)
            }
        }
        recyclerAdapter.setOnItemClickListener { link ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("link", link)
            startActivity(intent)
        }
    }

}