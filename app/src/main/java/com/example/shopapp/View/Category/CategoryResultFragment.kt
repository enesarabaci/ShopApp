package com.example.shopapp.View.Category

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Adapter.ProductsRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.Util.Util
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
    private var link = ""

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

        link = args.link

        binding.toolbarCategoryResultFragment.title = args.title
        viewModel.getCategoryResult(link)

        binding.fragmentCategoryResultRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    if (!binding.fragmentCategoryResultProgressBar.isVisible) {
                        viewModel.getCategoryResult(link)
                    }
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentCategoryResultProgressBar.visibility = View.GONE
                        val result = queryEvent.data as List<Product>
                        recyclerAdapter.updateList(result)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filtering_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.best_sellers -> viewModel.changeFilter(Util.BEST_SELLERS).also {
                item.setChecked(true)
            }
            R.id.increasing_price -> viewModel.changeFilter(Util.INCREASING_PRICE).also {
                item.setChecked(true)
            }
            R.id.descending_price -> viewModel.changeFilter(Util.DESCENDING_PRICE).also {
                item.setChecked(true)
            }
        }
        recyclerAdapter.list = listOf()
        viewModel.getCategoryResult(link)

        return super.onOptionsItemSelected(item)
    }

}