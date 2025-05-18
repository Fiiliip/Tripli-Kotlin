package com.example.tripli.data.repository

import com.example.tripli.R
import com.example.tripli.data.model.FilterData
import com.example.tripli.data.model.itinerary.Itinerary
import com.example.tripli.data.model.routeOptimalization.OptimalRouteResponse
import com.example.tripli.data.model.routeOptimalization.RouteMatrixRequest
import com.example.tripli.data.network.api.ItineraryApiService
import com.example.tripli.di.StringProvider
import com.google.gson.Gson
import com.google.gson.JsonParser

class ItineraryRepository(
    private val itineraryApiService: ItineraryApiService
) {
    private val gson = Gson()

    suspend fun getItineraries(filters: FilterData?): List<Itinerary> {
        val queryParams = filters?.toQueryMap() ?: emptyMap()
        val response = itineraryApiService.getItineraries(queryParams)

        if (response.isSuccessful) {
            val dataArray = response.body()?.getAsJsonArray("data") ?: throw Exception(StringProvider.get(R.string.error_malformed_response))
            return dataArray.map { gson.fromJson(it, Itinerary::class.java) }
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = errorJson?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString} ?: StringProvider.get(R.string.error_failed_get_itineraries)

        throw Exception(errorMessage)
    }

    suspend fun getItineraryById(id: Int): Itinerary? {
        val response = itineraryApiService.getItineraryById(id)

        if (response.isSuccessful) {
            val data = response.body()?.getAsJsonObject("itinerary")
            return data?.let { gson.fromJson(it, Itinerary::class.java) }
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = errorJson?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString} ?: StringProvider.get(R.string.error_failed_get_itinerary)

        throw Exception(errorMessage)
    }

    suspend fun createItinerary(itinerary: Itinerary): Itinerary? {
        val response = itineraryApiService.createItinerary(itinerary)

        if (response.isSuccessful) {
            val data = response.body()?.getAsJsonObject("data")
            return data?.let { gson.fromJson(it, Itinerary::class.java) }
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = errorJson?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString} ?: StringProvider.get(R.string.error_failed_create_itinerary)

        throw Exception(errorMessage)
    }

    suspend fun updateItinerary(itinerary: Itinerary): Boolean {
        val response = itineraryApiService.updateItinerary(itinerary.id, itinerary)

        if (response.isSuccessful) {
            val data = response.body()?.getAsJsonObject("itinerary")?.get("id")?.asInt ?: throw Exception(StringProvider.get(R.string.error_malformed_response))
            return itinerary.id == data
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = errorJson?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString} ?: StringProvider.get(R.string.error_failed_update_itinerary)

        throw Exception(errorMessage)
    }

    suspend fun deleteItinerary(id: Int) {
        val response = itineraryApiService.deleteItinerary(id)
        if (!response.isSuccessful) {
            val errorJson = response.errorBody()?.string()
            val errorMessage = errorJson?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString} ?: StringProvider.get(R.string.error_failed_delete_itinerary)

            throw Exception(errorMessage)
        }
    }

    suspend fun optimalizeRoute(routeMatrixRequest: RouteMatrixRequest): OptimalRouteResponse? {
        val response = itineraryApiService.getOptimalRoute(routeMatrixRequest)

        if (response.isSuccessful) {
            return response.body()?.let { jsonObject -> gson.fromJson(jsonObject, OptimalRouteResponse::class.java) }
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = errorJson?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString} ?: StringProvider.get(R.string.error_failed_get_optimal_route)

        throw Exception(errorMessage)
    }
}