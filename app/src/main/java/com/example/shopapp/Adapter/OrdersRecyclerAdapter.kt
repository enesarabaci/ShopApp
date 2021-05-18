package com.example.shopapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Model.Order
import com.example.shopapp.R

class OrdersRecyclerAdapter : RecyclerView.Adapter<OrdersRecyclerAdapter.ViewHolder>() {

    val list = ArrayList<Order>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrdersRecyclerAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.order_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersRecyclerAdapter.ViewHolder, position: Int) {
        val currentItem = list.get(position)
        holder.apply {
            date.text = currentItem.date?.toDate().toString()
            price.text = "${currentItem.price} TL"
            status.text = currentItem.status
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var date: TextView
        var price: TextView
        var status: TextView

        init {
            date = view.findViewById(R.id.order_item_date)
            price = view.findViewById(R.id.order_item_price)
            status = view.findViewById(R.id.order_item_status)
        }
    }

    fun updateList(newList: List<Order>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}