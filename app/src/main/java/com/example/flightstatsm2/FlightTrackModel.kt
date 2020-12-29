package com.example.flightstatsm2

data class FlightTrackModel (
    val icao24: String,
    val callsign: String,
    val startTime: Int,
    val endTime: Int,
    val path: ArrayList<WaypointModel>
)