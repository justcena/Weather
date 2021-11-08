package com.example.weather.data.services

import com.example.weather.data.model.current.CurrentWeatherResponse
import com.example.weather.data.model.days.DaysWeatherResponse
import com.example.weather.utils.Constants.API_KEY
import com.example.weather.utils.Constants.END_POINT
import com.example.weather.utils.Constants.END_POINT_DAYS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(END_POINT)
    suspend fun getWeatherByCityName(
        @Query("q") city: String = "Tehran",
        @Query("lang") lang: String = "fa",
        @Query("units") unit: String = "metric",
        @Query("appid") apiKey: String = API_KEY
    ): Response<CurrentWeatherResponse>

    @GET(END_POINT)
    suspend fun getWeatherByLocation(
        @Query("lat") userLatLocation: Double = 35.6944,
        @Query("lon") userLonLocation: Double = 51.4215,
        @Query("lang") lang: String = "fa",
        @Query("units") unit: String = "metric",
        @Query("appid") apiKey: String = API_KEY
    ): Response<CurrentWeatherResponse>

    @GET(END_POINT_DAYS)
    suspend fun getDaysWeather(
        @Query("lat") lat: Double = 35.6944,
        @Query("lon") lon: Double = 51.4215,
        @Query("lang") lang: String = "fa",
        @Query("units") unit: String = "metric",
        @Query("appid") apiKey: String = API_KEY
    ): Response<DaysWeatherResponse>
}