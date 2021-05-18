package com.example.shopapp.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Model.Product
import com.example.shopapp.R
import com.example.shopapp.Util.loadImage

class ProductsRecyclerAdapter(private val productItem: Int) :
    RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder>() {

    private val favoriteProducts = ArrayList<String>()
    private var onFavoriteClickListener: ((String, Boolean) -> Unit)? = null
    private var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(productItem, parent, false)
        return ViewHolder(view)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var list: List<Product>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    fun setOnFavoriteClickListener(listener: (String, Boolean) -> Unit) {
        onFavoriteClickListener = listener
    }

    fun setOnItemClickListener(listener: ((String) -> Unit)) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = list.get(position)
        holder.apply {
            title.text = currentProduct.title
            price.text = "${currentProduct.price} TL"
            image.loadImage(currentProduct.img)
            currentProduct.star?.apply {
                when (this) {
                    "width:20%;" -> {
                        holder.star1.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                    }
                    "width:40%;" -> {
                        holder.star1.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star2.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                    }
                    "width:60%;" -> {
                        holder.star1.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star2.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star3.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                    }
                    "width:80%;" -> {
                        holder.star1.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star2.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star3.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star4.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                    }
                    "width:100%;" -> {
                        holder.star1.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star2.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star3.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star4.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                        holder.star5.setColorFilter(ContextCompat.getColor(holder.star1.context, R.color.brown))
                    }
                }
            }
            if (favoriteProducts.contains(currentProduct.link)) {
                favorite.setCardBackgroundColor(Color.RED)
            } else {
                favorite.setCardBackgroundColor(Color.GRAY)
            }
            favorite.setOnClickListener {
                onFavoriteClickListener?.let {
                    currentProduct.link.apply {
                        if (favoriteProducts.contains(this)) {
                            it(this, false)
                        } else {
                            it(this, true)
                        }
                    }
                }
            }
            product.setOnClickListener {
                onItemClickListener?.let {
                    it(currentProduct.link)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var product: CardView
        var image: ImageView
        var title: TextView
        var price: TextView
        var favorite: CardView
        var star1: ImageView
        var star2: ImageView
        var star3: ImageView
        var star4: ImageView
        var star5: ImageView

        init {
            product = view.findViewById(R.id.product_item)
            image = view.findViewById(R.id.product_item_image)
            title = view.findViewById(R.id.product_item_title)
            price = view.findViewById(R.id.product_item_price)
            favorite = view.findViewById(R.id.product_item_favorite)
            star1 = view.findViewById(R.id.star1)
            star2 = view.findViewById(R.id.star2)
            star3 = view.findViewById(R.id.star3)
            star4 = view.findViewById(R.id.star4)
            star5 = view.findViewById(R.id.star5)
        }
    }

    fun updateFavorites(favorites: List<String>) {
        favoriteProducts.clear()
        favoriteProducts.addAll(favorites)
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Product>) {
        val combineList = ArrayList<Product>()
        combineList.apply {
            addAll(list)
            addAll(newList)
        }
        list = combineList
    }

}