package com.example.shopapp.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Model.Product
import com.example.shopapp.R
import com.example.shopapp.Util.loadImage

class CartRecyclerAdapter : RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>() {

    private var onItemClickListener: ((String) -> Unit)? = null
    private var onItemCheckListener: ((ArrayList<Product>) -> Unit)? = null
    private var onCountClickListener: ((String, Int, ArrayList<Product>) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cart_item, parent, false)
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
    fun setOnItemCheckListener(listener: (ArrayList<Product>) -> Unit) {
        onItemCheckListener = listener
    }
    fun setOnCountClickListener(listener: ((String, Int, ArrayList<Product>) -> Unit)) {
        onCountClickListener = listener
    }

    override fun onBindViewHolder(holder: CartRecyclerAdapter.ViewHolder, position: Int) {
        val currentProduct = list.get(position)
        holder.apply {
            image.loadImage(currentProduct.img)
            title.text = currentProduct.title
            price.text = "${currentProduct.price} TL"
            count.text = "Adet: ${currentProduct.count}"
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
            check.setOnCheckedChangeListener { compoundButton, b ->
                onItemCheckListener?.let {
                    val newProduct = currentProduct
                    newProduct.checked = b
                    updateList(currentProduct, newProduct)
                    currentProduct.checked = b

                    it(ArrayList(list))
                }
            }
            addCount.setOnClickListener {
                onCountClickListener?.let {
                    val newProduct = currentProduct
                    newProduct.count++
                    updateList(currentProduct, newProduct)
                    count.text = "Adet: ${currentProduct.count}"

                    it(currentProduct.link, currentProduct.count, ArrayList(list))
                }
            }
            removeCount.setOnClickListener {
                onCountClickListener?.let {
                    if (currentProduct.count > 1) {
                        val newProduct = currentProduct
                        newProduct.count--
                        updateList(currentProduct, newProduct)
                        count.text = "Adet: ${currentProduct.count}"

                        it(currentProduct.link, currentProduct.count, ArrayList(list))
                    }
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
        var check: CheckBox
        var removeCount: ImageButton
        var addCount: ImageButton
        var count: TextView
        var star1: ImageView
        var star2: ImageView
        var star3: ImageView
        var star4: ImageView
        var star5: ImageView

        init {
            product = view.findViewById(R.id.cart_item)
            image = view.findViewById(R.id.cart_item_image)
            title = view.findViewById(R.id.cart_item_title)
            price = view.findViewById(R.id.cart_item_price)
            check = view.findViewById(R.id.cart_item_checkBox)
            removeCount = view.findViewById(R.id.cart_item_remove)
            addCount = view.findViewById(R.id.cart_item_add)
            count = view.findViewById(R.id.cart_item_count)
            star1 = view.findViewById(R.id.star1)
            star2 = view.findViewById(R.id.star2)
            star3 = view.findViewById(R.id.star3)
            star4 = view.findViewById(R.id.star4)
            star5 = view.findViewById(R.id.star5)
        }
    }

    private fun updateList(oldItem: Product, newItem: Product) {
        val position = list.indexOfFirst { it == oldItem }
        list.get(position).checked = newItem.checked
        list.get(position).count = newItem.count
        list.get(position).price = newItem.price
        list.get(position).title = newItem.title
        list.get(position).link = newItem.link
        list.get(position).img = newItem.img
        list.get(position).star = newItem.star
    }

}