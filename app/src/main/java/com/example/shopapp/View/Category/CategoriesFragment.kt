package com.example.shopapp.View.Category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopapp.Adapter.CategoryFirstAdapter
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.ViewModel.CategoriesViewModel
import com.example.shopapp.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private lateinit var binding: FragmentCategoriesBinding
    private val viewModel: CategoriesViewModel by viewModels()
    private var list = ArrayList<HashMap<String, Any>>()
    private val recyclerAdapter = CategoryFirstAdapter(list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCategoriesBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        collectData()
        setupRecyclerView()
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentCategoriesProgressBar.visibility = View.GONE
                        list = queryEvent.data as ArrayList<HashMap<String, Any>>
                        recyclerAdapter.updateList(list)
                    }
                    is QueryEvent.Error -> {
                        binding.fragmentCategoriesProgressBar.visibility = View.GONE
                    }
                    is QueryEvent.Loading -> {
                        binding.fragmentCategoriesProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.fragmentCategoriesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
            setHasFixedSize(true)
        }
        recyclerAdapter.setOnItemClickListener { title, link ->
            if (title != null && link != null ) {
                findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToCategoryResultFragment(title, link))
            }
        }
    }

}