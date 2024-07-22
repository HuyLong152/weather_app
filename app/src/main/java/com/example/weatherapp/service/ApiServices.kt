package com.example.weatherapp.service

import com.example.weatherapp.Model.CurrentResponseApi
import com.example.weatherapp.Model.ForecastResponceApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units:String,
        @Query("appid") ApiKey:String
    ): Call<CurrentResponseApi>

    @GET("data/2.5/forecast")
    fun getCurrentForecast(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units:String,
        @Query("appid") ApiKey:String
    ): Call<ForecastResponceApi>
}