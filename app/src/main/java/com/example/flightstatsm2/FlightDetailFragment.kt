package com.example.flightstatsm2


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_flight_list.*
import kotlinx.android.synthetic.main.fragment_flight_detail.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FlightDetailMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlightDetailFragment : Fragment(), OnMapReadyCallback, RequestsManager.RequestListener, GoogleMap.OnMapLoadedCallback{

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var searchIcao: String? = null
    private var searchTime: Long? = null

    private lateinit var viewModel: FlightListViewModel
    private lateinit var mMapView: MapView
    private lateinit var myGoogleMap: GoogleMap

    private lateinit var coordinates: CoordinatesModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //On instancie la vue de la map et on récupère l'icao et le temps de départ/d'arrivée
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity()).get(FlightListViewModel::class.java)
        viewModel.getSelectedFlightNameLiveData().observe(this, androidx.lifecycle.Observer {
            flight_name.text = it
        })

        val rootView = inflater.inflate(R.layout.fragment_flight_detail, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(FlightListViewModel::class.java)
        searchIcao = viewModel.getSelectedIcao()
        searchTime = viewModel.getSelectedTime()

        mMapView = rootView.findViewById(R.id.mapView) as MapView
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()

        mMapView.getMapAsync(this)

        return rootView
    }

    //Permet d'accèder à l'activité PlaneInfoActivity pour réaliser l'écran 4 - Toujours non fonctionnel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnDetails.setOnClickListener {
            val intent = Intent(
                activity,
                PlaneInfoActivity::class.java
            )
            startActivity(intent) }
    }

    override fun onRequestSuccess(result: String?) {
        val flightTrackModel: FlightTrackModel = Utils.getTrackFromString(result!!)
        coordinates = CoordinatesModel(
            LatLng(
                flightTrackModel.path[0].lat.toDouble(),
                flightTrackModel.path[0].long.toDouble()
            ),
            LatLng(
                flightTrackModel.path.last().lat.toDouble(),
                flightTrackModel.path.last().long.toDouble()
            )
        )
        updateMap(coordinates)
        Log.i("RequestCoordinates", "Success, result : " + coordinates)
    }

    override fun onRequestFailed() {
        Log.e("Request", "problem")
    }

    fun searchFlightTrack(icao: String, time: Long) {
        val searchFlightTrackDataModel = SearchFlightTrackDataModel(icao, time)
        SearchFlightTrackAsyncTask(this).execute(searchFlightTrackDataModel)

        Log.i("AppelDone", "L'appel API a été lancé fréro")
    }

    fun updateMap(coordinates: CoordinatesModel){
        myGoogleMap.addMarker(
            MarkerOptions().position(coordinates.departureLatLong)
        )
        myGoogleMap.addMarker(
            MarkerOptions().position(coordinates.arrivalLatLong)
        )
        Log.i(
            "Coordinates",
            coordinates.departureLatLong.toString() + " - " + coordinates.arrivalLatLong.toString()
        )

        val poi = ArrayList<LatLng>()
        poi.add(coordinates.departureLatLong) //from
        poi.add(coordinates.arrivalLatLong) // to
        val polyline: Polyline = myGoogleMap.addPolyline(PolylineOptions().addAll(poi))
        this.zoomToFit(coordinates.departureLatLong, coordinates.arrivalLatLong)

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FlightDetailFragment().apply {

            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myGoogleMap = googleMap
        myGoogleMap.setOnMapLoadedCallback(this)
        searchFlightTrack(searchIcao!!, searchTime!!)
        Log.i("onMapReady", "The map is ready")
    }

    override fun onMapLoaded() {
        Log.i("onMapLoaded", "The map is loaded")
    }
    private fun zoomToFit(departure: LatLng, arrival: LatLng) {
        val coordinates = LatLngBounds.Builder()
            .include(departure)
            .include(arrival)
            .build()

        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(coordinates, 400))
    }
}