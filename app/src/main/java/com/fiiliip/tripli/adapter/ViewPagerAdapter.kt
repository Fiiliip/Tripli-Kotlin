package com.fiiliip.tripli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.fiiliip.tripli.R

class ViewPagerAdapter(context: Context, private val images: IntArray) : PagerAdapter() {

    private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // Inflate the image_slider_item.xml
        val imageSliderItemView = mLayoutInflater.inflate(R.layout.image_slider_item, container, false)

        // Reference the image view from the image_slider_item.xml
        val imageView = imageSliderItemView.findViewById<ImageView>(R.id.image_slider_item)

        // Set the image in the imageView
        imageView.setImageResource(images[position])

        // Add the View
        container.addView(imageSliderItemView)

        return imageSliderItemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}