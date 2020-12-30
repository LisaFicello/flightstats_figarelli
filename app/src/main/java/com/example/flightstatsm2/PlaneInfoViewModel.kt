package com.example.flightstatsm2

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

//Classe encore en cours de développement, donc les variables + fonctions ne sont pas encore totalement
//codée et testées
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
        flightListLiveData.value = flightList
    }

    override fun onRequestFailed() {
        isLoadingLiveData.value = false
    }
}