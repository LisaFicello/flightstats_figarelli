package com.example.flightstatsm2


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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FlightDetailMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlightDetailFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: FlightListViewModel
    private lateinit var mMapView: MapView
    private lateinit var myGoogleMap: GoogleMap

    private lateinit var depCoordinates: LatLng
    private lateinit var arrCoordinates: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_flight_detail, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(FlightListViewModel::class.java)
        viewModel.getSelectedFlightNameLiveData().observe(this, {
            //flight_name.text = it
        })

        depCoordinates = viewModel.getDepartureAirportCoordinates()
        arrCoordinates = viewModel.getArrivalAirportCoordinates()

        mMapView = rootView.findViewById(R.id.mapView) as MapView
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()

        mMapView.getMapAsync(this)

        // Inflate the layout for this fragment
        return rootView
        //return inflater.inflate(R.layout.fragment_flight_detail_map, container, false)
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
        // Add a marker in Sydney and move the camera
        //val departureAirportLocation = LatLng()
        //val arrivalAirportLocation = LatLng()
        Log.e("Mapfragment", "Dep airport" + viewModel.getDepartureAirportCoordinates())
        Log.e("Mapfragment", "Arrival airport" + viewModel.getArrivalAirportCoordinates())


        myGoogleMap.addMarker(
            MarkerOptions()
                .position(depCoordinates)
                .title("Departure airport")
        )

        myGoogleMap.addMarker(
            MarkerOptions()
                .position(arrCoordinates)
                .title("Arrival airport")
        )

        val poi = ArrayList<LatLng>()
        val polyLineOptions = PolylineOptions()
        poi.add(depCoordinates) //from
        poi.add(arrCoordinates) // to
        polyLineOptions.width(7f)
        polyLineOptions.geodesic(true)
        //polyLineOptions.color(resources.getColor())
        polyLineOptions.addAll(poi)
        val polyline: Polyline = myGoogleMap.addPolyline(polyLineOptions)
        polyline.isGeodesic = true

    }

    override fun onMapLoaded() {
        this.zoomToFit(depCoordinates, arrCoordinates)
    }

    private fun zoomToFit(poi1: LatLng, poi2: LatLng) {
        val group = LatLngBounds.Builder()
            .include(poi1) // LatLgn object1
            .include(poi2) // LatLgn object2
            .build()

        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(group, 400))
    }
}