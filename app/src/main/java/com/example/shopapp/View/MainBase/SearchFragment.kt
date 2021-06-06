package com.example.shopapp.View.MainBase

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Adapter.ProductsRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.Util.Util.BEST_SELLERS
import com.example.shopapp.Util.Util.DESCENDING_PRICE
import com.example.shopapp.Util.Util.INCREASING_PRICE
import com.example.shopapp.View.ProductDetailActivity
import com.example.shopapp.ViewModel.SearchViewModel
import com.example.shopapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val recyclerAdapter = ProductsRecyclerAdapter(R.layout.product_item)
    private var searchTerm = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarSearchFragment)
            setupActionBarWithNavController(findNavController(R.id.fragment_main))
        }
        setHasOptionsMenu(true)

        binding.apply {
            toolbarSearchFragment.setNavigationOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    activity?.onBackPressed()
                }
            })
        }

        viewModel.getFavorites()

        collectData()
        setupRecyclerView()

        binding.fragmentSearchText.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(
                searchText: TextView?,
                actionId: Int,
                p2: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText?.let {
                        if (it.text.toString().isNotEmpty()) {
                            UIUtil.hideKeyboard(requireActivity())
                            binding.fragmentSearchText.clearFocus()
                            searchTerm = it.text.toString()
                            recyclerAdapter.list = listOf()
                            viewModel.page = 0
                            viewModel.searchProduct(searchTerm)
                        }
                    }
                }
                return true
            }
        })

        binding.fragmentSearchRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    if (!binding.fragmentSearchProgressBar.isVisible) {
                        viewModel.searchProduct(searchTerm)
                    }
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentSearchProgressBar.visibility = View.GONE

                        val list = queryEvent.data as List<Product>
                        recyclerAdapter.updateList(list)
                    }
                    is QueryEvent.Error -> {
                        Toast.makeText(requireContext(), queryEvent.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.fragmentSearchProgressBar.visibility = View.GONE
                    }
                    is QueryEvent.Loading -> {
                        binding.fragmentSearchProgressBar.visibility = View.VISIBLE
                    }
                }
            }

        }
        viewModel.favorites.observe(viewLifecycleOwner, {
            recyclerAdapter.updateFavorites(it)
        })
    }

    private fun setupRecyclerView() {
        binding.fragmentSearchRv.apply {
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
            R.id.best_sellers -> viewModel.changeFilter(BEST_SELLERS).also {
                item.setChecked(true)
            }
            R.id.increasing_price -> viewModel.changeFilter(INCREASING_PRICE).also {
                item.setChecked(true)
            }
            R.id.descending_price -> viewModel.changeFilter(DESCENDING_PRICE).also {
                item.setChecked(true)
            }
        }
        recyclerAdapter.list = listOf()
        viewModel.searchProduct(searchTerm)

        return super.onOptionsItemSelected(item)
    }

}