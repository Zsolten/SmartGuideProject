package edu.bbte.smartguide.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bbte.smartguide.model.Locations
import edu.bbte.smartguide.model.Regions
import edu.bbte.smartguide.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel() : ViewModel() {
    private val _locationData: MutableStateFlow<List<Locations>> = MutableStateFlow(listOf())
    val locationsData: StateFlow<List<Locations>> = _locationData
    var selectedLocation by mutableStateOf<Locations?>(value = null)
        private set

    var tabIndex by mutableIntStateOf(0)
        private set

    fun tabIndex(index: Int) {
        viewModelScope.launch {
            tabIndex = index
        }
    }

    init {
        retrieveLocationsData()
    }

    fun selectLocation(id: Long) {
        retrieveLocationById(id)
    }

    fun updateWithDistance(latitude: Double, longitude: Double) {
        retrieveLocationsDataWithDistance(latitude, longitude)
    }

    private fun retrieveLocationsData() {
        viewModelScope.launch {
            val call: Call<List<Locations>> =
                RetrofitInstance.apiService.getLocationsWithoutDistance()

            call.enqueue(object : Callback<List<Locations>> {
                override fun onResponse(
                    call: Call<List<Locations>>,
                    response: Response<List<Locations>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _locationData.value = it
                        }
                    } else {
                        Log.d("API_RESPONSE", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Locations>>, t: Throwable) {
                    Log.d("Failed Retrieve", "Network Error", t)
                }
            })
        }
    }

    private fun retrieveLocationsDataWithDistance(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val call: Call<List<Locations>> =
                RetrofitInstance.apiService.getLocationsWithDistance(latitude, longitude)

            call.enqueue(object : Callback<List<Locations>> {
                override fun onResponse(
                    call: Call<List<Locations>>,
                    response: Response<List<Locations>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _locationData.value = it.sortedBy { it.distance }
                        }
                    } else {
                        Log.d("API_RESPONSE", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Locations>>, t: Throwable) {
                    Log.d("Failed Retrieve", "Network Error", t)
                }
            })
        }
    }

    private fun retrieveLocationById(id: Long) {
        viewModelScope.launch {
            val call: Call<Locations> = RetrofitInstance.apiService.getLocationById(id)
            call.enqueue(object : Callback<Locations> {
                override fun onResponse(call: Call<Locations>, response: Response<Locations>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            selectedLocation = it
                        }
                    } else {
                        Log.d("API_RESPONSE", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Locations>, t: Throwable) {
                    Log.d("Failed Retrieve", "Network Error", t)
                }
            })
        }
    }
}


