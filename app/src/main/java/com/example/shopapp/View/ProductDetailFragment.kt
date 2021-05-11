package com.example.shopapp.View

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Adapter.CommentsRecyclerAdapter
import com.example.shopapp.Adapter.ProductsRecyclerAdapter
import com.example.shopapp.Adapter.ViewPagerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.ViewModel.ProductDetailViewModel
import com.example.shopapp.databinding.FragmentProductDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private lateinit var binding: FragmentProductDetailBinding
    private val args: ProductDetailFragmentArgs by navArgs()
    private val viewModel: ProductDetailViewModel by viewModels()
    private val recyclerAdapter = ProductsRecyclerAdapter(R.layout.same_product_item)
    private val commentsRecyclerAdapter = CommentsRecyclerAdapter()
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var product: Product? = null
    private var favorite = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProductDetailBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ViewPagerAdapter(requireContext())

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.closeButton.setOnClickListener {
            activity?.finish()
        }

        var link = args.link
        if (link == null) {
            link = activity?.intent?.getStringExtra("link")
        }
        viewModel.getData(link)

        collectData()
        setupRecyclerView()

        binding.fragmentDetailViewPager.adapter = viewPagerAdapter
        binding.fragmentDetailFavorite.setOnClickListener {
            product?.let {
                if (favorite) {
                    viewModel.deleteFromFavorites(it.link)
                } else {
                    viewModel.addToFavorites(it.link)
                }
            }
        }
        binding.fragmentCartConfirm.setOnClickListener {
            viewModel.addToCart(product?.link)
        }
        binding.fragmentDetailComment.setOnClickListener {
            bottomSheet()
        }
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { queryEvent ->
                when (queryEvent) {
                    is QueryEvent.Success<*> -> {
                        binding.fragmentDetailProgressBar.visibility = View.GONE

                        product = queryEvent.data as Product
                        showStars(product?.star)
                        updateViews()
                        viewModel.getComments(product?.link)
                        viewModel.getSameProducts(product?.category)
                        viewPagerAdapter?.updateList(product?.images)
                    }
                    is QueryEvent.Error -> {
                        Toast.makeText(requireContext(), queryEvent.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.fragmentDetailProgressBar.visibility = View.GONE
                    }
                    is QueryEvent.Loading -> {
                        binding.fragmentDetailProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.sameProducts.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.list = it
            viewModel.getFavorites()
        })
        viewModel.favorites.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.updateFavorites(it)
            if (it.contains(product?.link)) {
                binding.fragmentDetailFavorite.setCardBackgroundColor(Color.RED)
                favorite = true
            } else {
                binding.fragmentDetailFavorite.setCardBackgroundColor(Color.GRAY)
                favorite = false
            }
        })
        viewModel.message.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.comments.observe(viewLifecycleOwner, {
            commentsRecyclerAdapter.updateList(it)
        })
    }

    private fun setupRecyclerView() {
        binding.fragmentDetailRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = recyclerAdapter
            setHasFixedSize(true)
        }
        recyclerAdapter.setOnFavoriteClickListener { link, add ->
            if (add) {
                viewModel.addToFavorites(link)
            } else {
                viewModel.deleteFromFavorites(link)
            }
        }
        recyclerAdapter.setOnItemClickListener { link ->
            findNavController().navigate(ProductDetailFragmentDirections.actionProductDetailFragmentSelf(link))
        }

        binding.fragmentDetailCommentRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = commentsRecyclerAdapter
            setHasFixedSize(true)
        }
    }

    private fun updateViews() {
        binding.apply {
            fragmentDetailTitle.text = product?.title
            val featuresString = StringBuilder()
            product?.features?.forEach {
                featuresString.append("$it \n")
            }
            fragmentDetailFeatures.text = featuresString
            fragmentDetailTotalPrice.text = "${product?.price} TL"
        }
    }

    private fun showStars(star: String?) {
        star?.let {
            when (it) {
                "width:20%;" -> {
                    binding.star1.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                }
                "width:40%;" -> {
                    binding.star1.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star2.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                }
                "width:60%;" -> {
                    binding.star1.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star2.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star3.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                }
                "width:80%;" -> {
                    binding.star1.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star2.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star3.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star4.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                }
                "width:100%;" -> {
                    binding.star1.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star2.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star3.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star4.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                    binding.star5.setColorFilter(ContextCompat.getColor(binding.star1.context, R.color.brown))
                }
            }
        }
    }

    private fun bottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.comment_bottom_sheet)
        val sendButton = bottomSheetDialog.findViewById<Button>(R.id.bottom_sheet_send)
        val comment = bottomSheetDialog.findViewById<EditText>(R.id.bottom_sheet_comment)

        sendButton?.setOnClickListener {
            if (comment != null && comment.text.toString().isNotEmpty() && product != null) {
                viewModel.sendComment(comment.text.toString(), product!!.link)
                bottomSheetDialog.dismiss()
            }
        }

        bottomSheetDialog.show()
    }

}