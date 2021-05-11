package com.example.shopapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.Model.Comment
import com.example.shopapp.R

class CommentsRecyclerAdapter : RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder>() {

    val list = ArrayList<Comment>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsRecyclerAdapter.ViewHolder, position: Int) {
        val currentItem = list.get(position)
        holder.apply {
            userName.text = currentItem.userName
            comment.text = currentItem.comment
            date.text = currentItem.date.toDate().toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView
        val date: TextView
        val comment: TextView

        init {
            userName = view.findViewById(R.id.comment_item_username)
            date = view.findViewById(R.id.comment_item_date)
            comment = view.findViewById(R.id.comment_item_comment)
        }
    }

    fun updateList(newList: List<Comment>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}