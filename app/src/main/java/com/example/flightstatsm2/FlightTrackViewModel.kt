package com.example.flightstatsm2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by sergio on 19/11/2020
 * All rights reserved GoodBarber
 */
class FlightTrackViewModel : ViewModel(), RequestsManager.RequestListener {


    val flightTrackListLiveData: MutableLiveData<List<FlightInfosModel>> = MutableLiveData()
    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val selectedFlightTrackLiveData: MutableLiveData<String> = MutableLiveData()

    fun getSelectedFlightTrackLiveData(): LiveData<String> {
        return selectedFlightTrackLiveData
    }


    fun search(icao: String, time: Long) {

        val searchDataModel = SearchFlightTrackDataModel(
            icao,
            time
        )
        /*val baseUrl: String = "https://opensky-network.org/api/tracks/all"

        viewModelScope.launch {
            //start loading
            isLoadingLiveData.value = true
            val result = withContext(Dispatchers.IO) {
                RequestsManager.getSuspended(baseUrl, getRequestParams(searchDataModel))
            }
            //end loading
            isLoadingLiveData.value = false
            if (result == null) {
                Log.e("Request", "problem")

            } else {
                val flightList = Utils.getFlightTrackFromArray(result)
                Log.d("models list", flightList.toString())
                flightListLiveData.value = flightList
            }

        }*/
        SearchFlightTrackAsyncTask(this).execute(searchDataModel)
    }

    private fun getRequestParams(searchModel: SearchFlightTrackDataModel?): Map<String, String>? {
        val params = HashMap<String, String>()
        if (searchModel != null) {
            params["icao24"] = searchModel.icao
            params["time"] = searchModel.time.toString()
        }
        return params
    }

    override fun onRequestSuccess(result: String?) {
        val flightList = Utils.getFlightTrackFromArray(result!!)
        Log.d("models list", flightList.toString())
        flightTrackListLiveData.value = flightList
    }

    override fun onRequestFailed() {
        isLoadingLiveData.value = false
        Log.e("Request", "problem")
    }

    fun updateSelectedFlightTrack(flightTrack: String) {
        selectedFlightTrackLiveData.value = flightTrack
    }
}