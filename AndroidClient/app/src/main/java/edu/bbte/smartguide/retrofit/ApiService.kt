package edu.bbte.smartguide.retrofit

import edu.bbte.smartguide.model.Locations
import edu.bbte.smartguide.model.Regions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/location/{id}")
    fun getLocationById(@Path("id") id: Long): Call<Locations>

    @GET("/location/list")
    fun getLocationsWithoutDistance(): Call<List<Locations>>

    @GET("/location/list/{lat}/{lon}")
    fun getLocationsWithDistance(@Path("lat") lat: Double, @Path("lon") lon: Double): Call<List<Locations>>


    @GET("/region/{id}")
    fun getRegionById(@Path("id") id: Long): Call<Regions>

    @GET("/region")
    fun getRegions(): Call<List<Regions>>
}