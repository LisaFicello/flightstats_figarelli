package com.example.flightstatsm2

data class FlightInfosModel (val icao24: String,
                             val callsign: String,
                             val startTime: Long,
                             val endTime: Long,
                             val path: Array<WaypointModel>)