package com.example.weatherapp.repository

import com.example.weatherapp.service.ApiServices

class WeatherRepository(val api:ApiServices) {
    fun getCurrentWeather(lat :Double, long:Double,unit:String) =
        api.getCurrentWeather(lat,long,unit,"0dd1850aedd50b903225e54f4fba1284")

    fun getCurrentForecast(lat :Double, long:Double,unit:String) =
        api.getCurrentForecast(lat,long,unit,"0dd1850aedd50b903225e54f4fba1284")
 }