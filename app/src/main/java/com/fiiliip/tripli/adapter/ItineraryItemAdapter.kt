package com.fiiliip.tripli.adapter

import android.net.Uri
import android.widget.ImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fiiliip.tripli.ItineraryItem
import com.fiiliip.tripli.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItineraryItemAdapter(private val itineraryItemList: List<ItineraryItem>) : RecyclerView.Adapter<ItineraryItemAdapter.ItineraryItemViewHolder>() {

    class ItineraryItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itinerary_item_image)
        val titleTextView: TextView = view.findViewById(R.id.itinerary_item_title)
        val authorNicknameTextView: TextView = view.findViewById(R.id.itinerary_item_author_nickname)
        val priceTextView: TextView = view.findViewById(R.id.price_tag_price)
        val ratingBar: RatingBar = view.findViewById(R.id.itinerary_item_rating_bar)
    }

    companion object {
        fun fromJson(json: String): List<ItineraryItem> {
            val listType = object : TypeToken<List<ItineraryItem>>() {}.type
            return Gson().fromJson(json, listType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryItemAdapter.ItineraryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itinerary_item, parent, false)
        return ItineraryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItineraryItemViewHolder, position: Int) {
        val currentItem = itineraryItemList[position]
        holder.imageView.setImageURI(Uri.parse(currentItem.imageUri))
        holder.titleTextView.text = currentItem.title
        holder.authorNicknameTextView.text = currentItem.authorNickname
        holder.priceTextView.text = currentItem.price.toString()
        holder.ratingBar.rating = currentItem.rating
    }

    override fun getItemCount() = itineraryItemList.size
}