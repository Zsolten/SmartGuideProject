package edu.bbte.smartguide.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import edu.bbte.smartguide.model.Location
import edu.bbte.smartguide.permissions.locationTracking.DefaultLocationClient
import edu.bbte.smartguide.permissions.locationTracking.LocationClient
import edu.bbte.smartguide.retrofit.RetrofitInstance
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel() : ViewModel() {
    private val _locationData: MutableStateFlow<List<Location>> = MutableStateFlow(listOf())
    val locationsData: StateFlow<List<Location>> = _locationData
    var selectedLocation by mutableStateOf<Location?>(value = null)
        private set

    var tabIndex by mutableIntStateOf(0)
        private set

    fun tabIndex(index: Int){
        viewModelScope.launch {
            tabIndex = index
        }
    }

    init {
        retrieveLocationsData()
    }

    fun selectLocation(id: Long) {
        retrieveLocation(id)
    }

    fun update() {
        retrieveLocationsData()
    }

    private fun retrieveLocationsData() {
        viewModelScope.launch {
//            val location = getCurrentLocation()

//            val location = DefaultLocationClient

            val call: Call<List<Location>> = RetrofitInstance.apiService.getLocationsWithoutDistance()


            call.enqueue(object : Callback<List<Location>> {
                override fun onResponse(call: Call<List<Location>>, response: Response<List<Location>>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _locationData.value = it
                        }
                    } else {
                        Log.d("API_RESPONSE", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Location>>, t: Throwable) {
                    Log.d("Failed Retrieve", "Network Error", t)
                }
            })
        }
    }

    private fun retrieveLocation(id: Long) {
        viewModelScope.launch {
            val call: Call<Location> = RetrofitInstance.apiService.getLocationById(id)
            call.enqueue(object : Callback<Location> {
                override fun onResponse(call: Call<Location>, response: Response<Location>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            selectedLocation = it
                        }
                    } else {
                        Log.d("API_RESPONSE", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Location>, t: Throwable) {
                    Log.d("Failed Retrieve", "Network Error", t)
                }
            })
        }
    }
}
