package edu.bbte.smartguide.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bbte.smartguide.model.SmallLocation
import edu.bbte.smartguide.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class HomeViewModel : ViewModel() {
    private val _locationData: MutableStateFlow<List<SmallLocation>> = MutableStateFlow(listOf())
    val locationsData: StateFlow<List<SmallLocation>> = _locationData
    var selectedLocations by mutableStateOf<List<SmallLocation>?>(value = null)
        private set

    init {
        retrieveLocationsData()
    }

    fun selectedLocations(location: List<SmallLocation>){
        viewModelScope.launch {
            selectedLocations = location
        }
    }


    private fun retrieveLocationsData() {
        viewModelScope.launch {
            val call: Call<List<SmallLocation>>

//            if () {
//                call = RetrofitInstance.apiService.getLocationsWithDistance()
//            } else {
                call = RetrofitInstance.apiService.getLocationsWithoutDistance()
//            }

            call.enqueue(object : Callback<List<SmallLocation>> {
                override fun onResponse(
                    call: Call<List<SmallLocation>>,
                    response: Response<List<SmallLocation>>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()

                        if (responseData != null) {
                            _locationData.value = responseData
                        }
                    }
                }

                override fun onFailure(call: Call<List<SmallLocation>>, t: Throwable) {
                    Log.d("Failed Retrieve", "Network Error")
                }
            })
        }
    }
}