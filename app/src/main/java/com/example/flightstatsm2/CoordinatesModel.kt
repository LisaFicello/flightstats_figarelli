package com.example.flightstatsm2

import com.google.android.gms.maps.model.LatLng

data class CoordinatesModel (
    val departureLatLong: LatLng,
    val arrivalLatLong: LatLng
)