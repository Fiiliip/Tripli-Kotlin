package com.fiiliip.tripli.adapter

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
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
import java.io.InputStream
import java.text.DecimalFormat

class ItineraryItemAdapter(private val itineraryItemList: List<ItineraryItem>, private val context: Context) : RecyclerView.Adapter<ItineraryItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itinerary_item_image)
        val titleTextView: TextView = itemView.findViewById(R.id.itinerary_item_title)
        val authorNicknameTextView: TextView = itemView.findViewById(R.id.itinerary_item_author_nickname)
        val priceTextView: TextView = itemView.findViewById(R.id.price_tag_price)
        val ratingBar: RatingBar = itemView.findViewById(R.id.itinerary_item_rating_bar)
    }

    class BottomMarginItemDecoration(private val marginBottom: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = marginBottom
        }
    }

    companion object {
        fun fromJson(json: String): List<ItineraryItem> {
            val listType = object : TypeToken<List<ItineraryItem>>() {}.type
            return Gson().fromJson(json, listType)
        }

        const val DEFAULT_MARGIN_BOTTOM_DP = 8
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryItemAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itinerary_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ItineraryItemAdapter.ViewHolder, position: Int) {
        val currentItem = itineraryItemList[position]

        val inputStream: InputStream = context.assets.open(currentItem.image)
        viewHolder.imageView.setImageDrawable(Drawable.createFromStream(inputStream, null))

        viewHolder.titleTextView.text = currentItem.title
        viewHolder.authorNicknameTextView.text = currentItem.authorNickname
        viewHolder.priceTextView.text = DecimalFormat("###.#").format(currentItem.price) + "€"
        viewHolder.ratingBar.rating = currentItem.rating
    }

    override fun getItemCount() = itineraryItemList.size
}