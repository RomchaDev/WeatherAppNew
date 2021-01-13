package com.romeo.weatherappnew.JSON

import kotlin.math.roundToLong

class Day {
    private val temp: Temp? = null
    private val weather: Array<Weather>? = null
    fun getTemp(): Long {
        return (temp!!.eve - 273).roundToLong()
    }

    fun getWeather(): Weather {
        return weather!![0]
    }
}