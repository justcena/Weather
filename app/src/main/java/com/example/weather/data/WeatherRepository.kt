package com.example.weather.data

import com.example.weather.data.services.ApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCurrentWeather(city: String) = apiService.getWeatherByCityName(city)

    suspend fun getDailyWeather(lat: Double, lon: Double) =
        apiService.getDaysWeather(lat = lat, lon = lon)

    suspend fun getWeatherByLocation(userLatLocation: Double, userLonLocation: Double) =
        apiService.getWeatherByLocation(userLatLocation, userLonLocation)
}