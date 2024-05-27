package edu.bbte.smartguide.ui.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import edu.bbte.smartguide.model.Regions
import edu.bbte.smartguide.retrofit.RetrofitInstance
import edu.bbte.smartguide.ui.geofence.GeofenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "location",
            "Background Location",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        //Adding geofence locations

        val geofenceManager = GeofenceManager(this)
        val call: Call<List<Regions>> = RetrofitInstance.apiService.getRegions()
        Log.d("LocationApp: ", "Initalizing Geofence")

        call.enqueue(object : Callback<List<Regions>> {
            override fun onResponse(
                call: Call<List<Regions>>,
                response: Response<List<Regions>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        geofenceManager.addGeofencesFromRegions(it)
                        geofenceManager.registerGeofence()
                    }
                } else {
                    Log.d("API_RESPONSE", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Regions>>, t: Throwable) {
                Log.d("API_RESPONSE", "Network Error", t)
            }
        })
    }
}