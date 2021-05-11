package com.example.shopapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R
import com.example.shopapp.Util.loadImage

class CategorySecondAdapter(val list: ArrayList<HashMap<String, String>>) : RecyclerView.Adapter<CategorySecondAdapter.ViewHolder>() {

    private var onItemClickListener: ((String?, String?) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.category_second_item, parent, false)
        return ViewHolder(view)
    }

    fun setOnItemClickListener(listener: (String?, String?) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: CategorySecondAdapter.ViewHolder, position: Int) {
        list.get(position).apply {
            holder.csit.text = get("Title")
            holder.csii.loadImage(get("image"))
            holder.csi.setOnClickListener {
                onItemClickListener?.let {
                    it(get("Title"), get("Link"))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var csi: CardView
        var csii: ImageView
        var csit: TextView
        init {
            csi = view.findViewById(R.id.category_second_item)
            csii = view.findViewById(R.id.category_second_item_image)
            csit = view.findViewById(R.id.category_second_item_title)
        }
    }

    fun updateList(newList: List<HashMap<String, String>>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}