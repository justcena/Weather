package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.WeatherRepository
import com.example.weather.data.model.current.CurrentWeatherResponse
import com.example.weather.data.model.days.DaysWeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    private val TAG = "LOGS"
    val city: String = "Tehran"


    private val _currentWeatherByLocation = MutableLiveData<CurrentWeatherResponse>()
    val currenttWeatherByLocation: LiveData<CurrentWeatherResponse>
        get() = _currentWeatherByLocation

    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currenttWeather: LiveData<CurrentWeatherResponse>
        get() = _currentWeather

    private val _dailyWeather = MutableLiveData<DaysWeatherResponse>()
    val dailyWeather: LiveData<DaysWeatherResponse>
        get() = _dailyWeather

    init {
        getCurrentDefaultWeather(city)
    }

    fun getDaysWeather(lat: Double, lon: Double) = viewModelScope.launch {
        repository.getDailyWeather(lat, lon).let {
            if (it.isSuccessful) _dailyWeather.postValue(it.body())
            else Log.d(TAG, "getDailyWeatherInfo: ${it.code()}")
        }
    }

     fun getCurrentDefaultWeather(city: String) = viewModelScope.launch {
        repository.getCurrentWeather(city).let {
            if (it.isSuccessful) _currentWeather.postValue(it.body())
            else Log.d(TAG, "getCurrentDefaultWeather: ${it.code()}")
        }
    }

    fun getWeatherByLocation(userLatLocation: Double, userLonLocation: Double) = viewModelScope.launch {
        repository.getWeatherByLocation(userLatLocation, userLonLocation).let {
            if (it.isSuccessful) _currentWeatherByLocation.postValue(it.body())
            else Log.d(TAG, "getWeatherByLocation: ${it.code()}")
        }
    }

}