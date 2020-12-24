package com.example.flightstatsm2

data class WaypointModel (val timePoint: Long,
                             val latitude: Float,
                             val longitude: Float,
                             val altitude: Float,
                             val truetracks : Float,
                             val onGround: Boolean)