package com.example.shopapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R

class CategoryFirstAdapter(val list: ArrayList<HashMap<String, Any>>) :
    RecyclerView.Adapter<CategoryFirstAdapter.ViewHolder>() {

    private var onItemClickListener: ((String?, String?) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.category_first_item, parent, false)
        return ViewHolder(view)
    }

    fun setOnItemClickListener(listener: (String?, String?) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: CategoryFirstAdapter.ViewHolder, position: Int) {
        val secondAdapter = CategorySecondAdapter(ArrayList())
        holder.apply {
            cfit.text = list.get(position).get("category") as String
            cfirv.layoutManager = GridLayoutManager(cfirv.context, 3)
            cfirv.adapter = secondAdapter
            cfirv.setHasFixedSize(true)
            secondAdapter.setOnItemClickListener { title, link ->
                onItemClickListener?.let {
                    it(title, link)
                }
            }

            cfi.setOnClickListener {
                if (secondAdapter.list.isEmpty()) {
                    cfia.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    secondAdapter.updateList(list.get(position).get("data") as ArrayList<HashMap<String, String>>)
                }else {
                    cfia.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    secondAdapter.updateList(ArrayList())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cfi: LinearLayout
        var cfit: TextView
        var cfia: ImageView
        var cfirv: RecyclerView

        init {
            cfi = view.findViewById(R.id.category_first_item)
            cfit = view.findViewById(R.id.category_first_item_title)
            cfia = view.findViewById(R.id.category_first_item_arrow)
            cfirv = view.findViewById(R.id.category_first_item_rv)
        }
    }

    fun updateList(newList: ArrayList<HashMap<String, Any>>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}