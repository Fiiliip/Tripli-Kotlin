package com.fiiliip.tripli

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiiliip.tripli.adapter.ItineraryItemAdapter
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var itineraryListRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        itineraryListRecyclerView = view.findViewById(R.id.home_itinerary_list) // Find the RecyclerView with ID "home_itinerary_list" and assign it to itineraryList
        itineraryListRecyclerView.layoutManager = LinearLayoutManager(view.context) // Set the layout manager for itineraryList to a LinearLayoutManager
        val itineraryItems = loadItineraryItemsFromJson(requireContext()) // Load itinerary items from the JSON file located in the assets folder
        val adapter = ItineraryItemAdapter(itineraryItems, requireContext()) // Load itinerary items from the JSON file located in the assets folder
        itineraryListRecyclerView.adapter = adapter // Set the adapter for itineraryList to the new adapter

        // Set margin bottom to items in recycler view
        val marginBottomPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            ItineraryItemAdapter.DEFAULT_MARGIN_BOTTOM_DP.toFloat(),
            resources.displayMetrics
        ).toInt()

        val bottomMarginItemDecoration = ItineraryItemAdapter.BottomMarginItemDecoration(marginBottomPx)
        itineraryListRecyclerView.addItemDecoration(bottomMarginItemDecoration)

        return view
    }

    private fun loadItineraryItemsFromJson(context: Context): List<ItineraryItem> {
        val json = try {
            context.assets.open("mock/itineraries.json").bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }

        return Gson().fromJson(json, Array<ItineraryItem>::class.java).toList()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}