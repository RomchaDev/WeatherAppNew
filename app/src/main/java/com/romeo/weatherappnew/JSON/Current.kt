package com.romeo.weatherappnew.JSON

class Current {
    val windSpeed = 0f
    val temp = 0f
    private val weather: Array<Weather>? = null
    fun getWeather(): Weather {
        return weather!![0]
    }
}