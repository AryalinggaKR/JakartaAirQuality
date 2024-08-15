package com.aryalingga.jakartaairquality

data class AQIResponse(
    val status: String,
    val data: AQIData
)

data class AQIData(
    val aqi: Int,
    val idx: Int,
    val city: City
)

data class City(
    val name: String,
    val url: String
)
