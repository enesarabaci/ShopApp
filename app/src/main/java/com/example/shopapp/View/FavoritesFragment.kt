package com.example.shopapp.View

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Adapter.FavoritesRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.ViewModel.FavoritesViewModel
import com.example.shopapp.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModels()
    private val recyclerAdapter = FavoritesRecyclerAdapter()
    private var job: Job? = null
    private val result = ArrayList<Product>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFavoritesBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        collectData()

        binding.fragmentFavoritesSearchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    job?.cancel()
                    if (it.isNotEmpty()) {
                        job = MainScope().launch {
                            delay(1000)
                            viewModel.search = it.toString()
                            val filteredList = viewModel.makeFiltering(result)
                            recyclerAdapter.list = filteredList
                        }
                    } else {
                        viewModel.search = ""
                        recyclerAdapter.list = result
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    override fun onResume() {
        viewModel.getFavorites()
        super.onResume()
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentFavoritesProgressBar.visibility = View.GONE

                        val list = queryEvent.data as List<Product>
                        result.clear()
                        result.addAll(list)
                        val filteredList = viewModel.makeFiltering(result)
                        recyclerAdapter.list = filteredList
                    }
                    is QueryEvent.Error -> {
                        Toast.makeText(requireContext(), queryEvent.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.fragmentFavoritesProgressBar.visibility = View.GONE
                    }
                    is QueryEvent.Loading -> {
                        binding.fragmentFavoritesProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.message.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        binding.fragmentFavoritesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        recyclerAdapter.setOnItemClickListener { link ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("link", link)
            startActivity(intent)
        }
        recyclerAdapter.setOnButtonClickListener { link ->
            viewModel.addToCart(link)
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.fragmentFavoritesRv)
    }

    private val simpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
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
                viewModel.deleteFavoriteProduct(ml.get(position).link)
                ml.removeAt(position)
                recyclerAdapter.list = ml.toList()
            }

        }

}