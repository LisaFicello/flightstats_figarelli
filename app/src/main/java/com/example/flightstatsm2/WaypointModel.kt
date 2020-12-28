package com.example.flightstatsm2

data class WaypointModel(
    val time: Long,
    val lat: Long,
    val long: Long,
    val altitude: Long,
    val rotation: Long,
    val isOnGround: Boolean
)
