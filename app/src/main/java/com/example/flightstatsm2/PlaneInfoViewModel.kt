package com.example.flightstatsm2

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class PlaneInfoViewModel : ViewModel(), RequestsManager.RequestListener {
    private val airportListLiveData : MutableLiveData<List<Airport>> = MutableLiveData()
    val flightListLiveData: MutableLiveData<List<FlightModel>> = MutableLiveData()
    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val selectedFlightLiveData: MutableLiveData<FlightModel> = MutableLiveData()

    init {
        airportListLiveData.value = Utils.generateAirportList()
    }

    fun getSelectedFlightNameLiveData(): LiveData<FlightModel> {
        return selectedFlightLiveData
    }

    fun getDepartureAirportCoordinates(): LatLng {
        var coordinates = LatLng(0.0, 0.0)
        val dep = selectedFlightLiveData.value!!.estDepartureAirport
        airportListLiveData.value!!.forEach {
            if (it.icao == dep) {
                coordinates = LatLng(it.lat.toDouble(), it.lon.toDouble())
                return coordinates
            }
        }
        return coordinates
    }

    fun getArrivalAirportCoordinates(): LatLng {
        var coordinates = LatLng(0.0, 0.0)
        val arr = selectedFlightLiveData.value!!.estArrivalAirport
        airportListLiveData.value!!.forEach {
            if (it.icao == arr) {
                coordinates = LatLng(it.lat.toDouble(), it.lon.toDouble())
                return coordinates
            }
        }
        return coordinates
    }

    fun search(icao: String, isArrival: Boolean, begin: Long, end: Long) {

        val searchDataModel = SearchDataModel(
            isArrival,
            icao,
            begin,
            end
        )
        SearchFlightsAsyncTask(this).execute(searchDataModel)
    }

    override fun onRequestSuccess(result: String?) {
        isLoadingLiveData.value = false
        val flightList = Utils.getFlightListFromString(result!!)
        Log.d("models list", flightList.toString())
        flightListLiveData.value = flightList
    }

    override fun onRequestFailed() {
        isLoadingLiveData.value = false
        Log.e("Request", "problem")
    }

    fun updateSelectedFlight(flight: FlightModel) {
        selectedFlightLiveData.value = flight
    }
}