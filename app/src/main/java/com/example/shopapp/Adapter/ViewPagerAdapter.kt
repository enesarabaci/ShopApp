package com.example.shopapp.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.shopapp.Util.loadImage

class ViewPagerAdapter(private val context: Context) : PagerAdapter() {

    val images = ArrayList<String>()

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.loadImage(images.get(position))
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

    fun updateList(list: ArrayList<String>?) {
        list?.let {
            images.clear()
            images.addAll(it)
            notifyDataSetChanged()
        }
    }

}