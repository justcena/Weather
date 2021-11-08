package com.example.weather.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weather.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val daily = args.daily
        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/" + "${daily.weather[0].icon}" + ".png")
            .into(iv_daily_details)
        tv_daily_humidity.text = daily.humidity.toString()
        tv_daily_wind_speed.text = daily.windSpeed.toString()
        tv_daily_des.text = daily.weather[0].description
    }
}