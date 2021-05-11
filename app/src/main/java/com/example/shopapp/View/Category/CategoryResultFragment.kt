package com.example.shopapp.View.Category

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopapp.Adapter.ProductsRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.View.ProductDetailActivity
import com.example.shopapp.ViewModel.CategoryResultViewModel
import com.example.shopapp.databinding.FragmentCategoryResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoryResultFragment : Fragment(R.layout.fragment_category_result) {

    private lateinit var binding: FragmentCategoryResultBinding
    private val args: CategoryResultFragmentArgs by navArgs()
    private val recyclerAdapter = ProductsRecyclerAdapter(R.layout.product_item)
    private val viewModel: CategoryResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCategoryResultBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarCategoryResultFragment)
            setupActionBarWithNavController(findNavController(R.id.fragment_category))
        }
        setHasOptionsMenu(true)

        binding.apply {
            toolbarCategoryResultFragment.setNavigationOnClickListener(object :
                View.OnClickListener {
                override fun onClick(p0: View?) {
                    activity?.onBackPressed()
                }
            })
        }

        viewModel.getFavorites()

        collectData()
        setupRecylcerView()

        binding.toolbarCategoryResultFragment.title = args.title
        viewModel.getCategoryResult(args.link)

    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentCategoryResultProgressBar.visibility = View.GONE
                        val result = queryEvent.data as List<Product>
                        recyclerAdapter.list = result
                    }
                    is QueryEvent.Error -> {
                        Toast.makeText(requireContext(), queryEvent.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.fragmentCategoryResultProgressBar.visibility = View.GONE
                    }
                    is QueryEvent.Loading -> {
                        binding.fragmentCategoryResultProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.favorites.observe(viewLifecycleOwner, {
            recyclerAdapter.updateFavorites(it)
        })
    }

    private fun setupRecylcerView() {
        binding.fragmentCategoryResultRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = recyclerAdapter
            setHasFixedSize(true)
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