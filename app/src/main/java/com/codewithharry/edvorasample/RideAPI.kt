package com.codewithharry.edvorasample

import retrofit2.Response
import retrofit2.http.GET

interface RideAPI {

    @GET("/rides")
    suspend fun getRides(): Response<List<Ride>>
}