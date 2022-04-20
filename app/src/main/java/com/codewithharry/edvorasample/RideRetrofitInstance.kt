package com.codewithharry.edvorasample

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RideRetrofitInstance {

    val retrofit:RideAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://assessment.api.vweb.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RideAPI::class.java)
    }
}