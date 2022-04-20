package com.codewithharry.edvorasample

data class Ride(
    val id: Int,
    val origin_station_code: Int,
    val station_path: ArrayList<Int>,
    val destination_station_code: Int,
    val date: String,
    val map_url: String,
    val state: String,
    val city: String,

    var distanceFromUser: Int,
    var upcoming: Boolean
)
