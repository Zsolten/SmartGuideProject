package edu.bbte.smartguide.retrofit

import edu.bbte.smartguide.model.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/location/{id}")
    fun getLocationById(@Path("id") id: Long): Call<Location>

    @GET("/location/list")
    fun getLocationsWithoutDistance(): Call<List<Location>>

    @GET("/location/list/{lat}/{lon}")
    fun getLocationsWithDistance(@Path("lat") lat: Double, @Path("lon") lon: Double): Call<List<Location>>
}