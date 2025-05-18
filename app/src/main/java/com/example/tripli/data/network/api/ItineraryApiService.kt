package com.example.tripli.data.network.api

import com.example.tripli.data.model.itinerary.Itinerary
import com.example.tripli.data.model.routeOptimalization.RouteMatrixRequest
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ItineraryApiService {
    @GET("v1/tripli/itineraries")
    suspend fun getItineraries(@QueryMap filters: Map<String, String>? = null): Response<JsonObject>

    @GET("v1/tripli/itineraries/{id}")
    suspend fun getItineraryById(@Path("id") id: Int): Response<JsonObject>

    @POST("v1/tripli/itineraries/create")
    suspend fun createItinerary(@Body itinerary: Itinerary): Response<JsonObject>

    @PUT("v1/tripli/itineraries/{id}")
    suspend fun updateItinerary(@Path("id") id: Int, @Body itinerary: Itinerary): Response<JsonObject>

    @DELETE("v1/tripli/itineraries/{id}")
    suspend fun deleteItinerary(@Path("id") id: Int): Response<JsonObject>

    @POST("v1/tripli/routes/optimal-route")
    suspend fun getOptimalRoute(@Body routeMatrixRequest: RouteMatrixRequest): Response<JsonObject>
}