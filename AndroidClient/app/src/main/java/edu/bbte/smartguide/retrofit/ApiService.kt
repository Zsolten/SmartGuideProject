package edu.bbte.smartguide.retrofit

import edu.bbte.smartguide.model.Location
import edu.bbte.smartguide.model.SmallLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/location/{id}")
    suspend fun getLocation(@Path("id") id: Int): Call<Location>

    @GET("/location/list")
    fun getLocationsWithoutDistance(): Call<List<SmallLocation>>

    @GET("/location/list/{lat}/{lon}")
    fun getLocationsWithDistance(@Path("lat") lat: Double, @Path("lon") lon: Double): Call<List<SmallLocation>>
}