package com.example.shopapp.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class FavoritesRecyclerAdapter : RecyclerView.Adapter<FavoritesRecyclerAdapter.ViewHolder>() {

    private var onItemClickListener: ((String) -> Unit)? = null
    private var onButtonClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesRecyclerAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(view)
    }

    private val diffUtil = object: DiffUtil.ItemCallback<Product>() {
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

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
    fun setOnButtonClickListener(listener: (String) -> Unit) {
        onButtonClickListener = listener
    }

    override fun onBindViewHolder(holder: FavoritesRecyclerAdapter.ViewHolder, position: Int) {
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
            product.setOnClickListener {
                onItemClickListener?.let {
                    it(currentProduct.link)
                }
            }
            cart.setOnClickListener {
                onButtonClickListener?.let {
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
        var cart: Button
        var star1: ImageView
        var star2: ImageView
        var star3: ImageView
        var star4: ImageView
        var star5: ImageView

        init {
            product = view.findViewById(R.id.favorite_item)
            image = view.findViewById(R.id.favorite_item_image)
            title = view.findViewById(R.id.favorite_item_title)
            price = view.findViewById(R.id.favorite_item_price)
            cart = view.findViewById(R.id.favorite_item_add_to_cart)
            star1 = view.findViewById(R.id.star1)
            star2 = view.findViewById(R.id.star2)
            star3 = view.findViewById(R.id.star3)
            star4 = view.findViewById(R.id.star4)
            star5 = view.findViewById(R.id.star5)
        }
    }

}