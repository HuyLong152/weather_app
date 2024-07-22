package com.example.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.service.ApiClient
import com.example.weatherapp.service.ApiServices

class WeatherViewModel(val repository:WeatherRepository):ViewModel() {
    constructor() : this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))
    fun loadCurrentWeather(lat:Double,long:Double,unit:String) =
        repository.getCurrentWeather(lat,long,unit)
    fun loadCurrentForecast(lat:Double,long:Double,unit:String) =
        repository.getCurrentForecast(lat,long,unit)
}