package com.aryalingga.jakartaairquality

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AQIService {
    @GET("/feed/{city}/")
    fun getCityAQI(@Path("city") city: String, @Query("token") token: String): Call<AQIResponse>
}