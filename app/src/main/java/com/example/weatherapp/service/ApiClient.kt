package com.example.weatherapp.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    // mặc định retrofit có sử dụng okhttp
    private lateinit var retrofit : Retrofit
    private val client = OkHttpClient.Builder() // tạo okhttp
        .connectTimeout(60,TimeUnit.SECONDS) // thời gian kết nối tối đa đến server là 60 giây
        .readTimeout(60,TimeUnit.SECONDS)   // thời gian đọc dữ liệu tối đa là 60 giây
        .writeTimeout(60,TimeUnit.SECONDS)  // thời gian ghi dữ liệu lên server tối đa là 60 giây
        .build()

    fun getClient() :Retrofit{
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}