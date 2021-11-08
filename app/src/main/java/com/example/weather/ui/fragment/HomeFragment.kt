package com.example.weather.ui.fragment

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.model.days.Daily
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.ui.adapter.WeatherAdapter
import com.example.weather.ui.WeatherViewModel
import com.example.weather.utils.afterTextChanged
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

private const val TAG = "----->"

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), WeatherAdapter.OnItemClickListener {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var weatherAdapter: WeatherAdapter
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var currentItemDaily: Daily? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()
        setUpRecyclerView()

//        weatherAdapter.setOnItemClickListener {
//            Log.e(TAG, "onViewCreated: $it")
//            val bundle = Bundle().apply {
//                putSerializable("daily", it)
//            }
//            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
//        }
        lifecycleScope.launchWhenCreated {
            searchView.afterTextChanged {
                if (it.isNullOrEmpty()) {
                } else {
                    viewModel.getCurrentDefaultWeather(it)
                    viewModel.currenttWeather.observe(requireActivity(), {
                        tv_city.text = it.name
                        tv_temp.text = it.main.temp.toString()

                        viewModel.getWeatherByLocation(it.coord.lat, it.coord.lon)
                        weatherAdapter.notifyDataSetChanged()
                    })
                }
            }

        }
    }


    private fun setUpCurrentWeatherByUserLocation(
        userLonLocation: Double,
        userLatLocation: Double
    ) {
        viewModel.getWeatherByLocation(userLatLocation, userLonLocation)
        viewModel.currenttWeatherByLocation.observe(requireActivity(), { response ->
            tv_city.text = response.name
            tv_temp.text = response.main.temp.toString()
            val des = response.weather[0]
            if (des.main == "Clear sky" || des.main == "mist") {
                Glide.with(requireView())
                    .load(R.drawable.ic_header_mist)
                    .into(img_icon)
            } else if (des.main == "haze" || des.main == "overcast") {
                Glide.with(requireView())
                    .asBitmap().override(1080, 600)
                    .load(R.drawable.ic_header_haze)
                    .into(img_icon)
            } else if (des.main == "snow" || des.main == "snow") {
                Glide.with(requireView())
                    .asBitmap().override(1080, 600)
                    .load(R.drawable.ic_header_snow)
                    .into(img_icon)
            } else if (des.main == "Rain") {
                Glide.with(requireView())
                    .asBitmap().override(1080, 600)
                    .load(R.drawable.ic_header_rain)
                    .into(img_icon)
            } else if (des.main == "Clouds") {
                Glide.with(requireView())
                    .asBitmap().override(1080, 600)
                    .load(R.drawable.ic_header_clouds)
                    .into(img_icon)
            }
            viewModel.getDaysWeather(response.coord.lat, response.coord.lon)
        })

        setUpRecyclerView()
    }

    private fun setUpCurrentView(city: String?) {
        viewModel.currenttWeather.observe(requireActivity(), { response ->
            tv_city.text = response.name
            tv_temp.text = response.main.temp.toString()
            val des = response.weather[0]
            if (des.main == "Clear sky" || des.main == "mist") {
                Glide.with(requireView())
                    .load(R.drawable.ic_header_mist)
                    .into(img_icon)
            } else if (des.main == "haze" || des.main == "overcast") {
                Glide.with(requireView())
                    .asBitmap().override(1080, 600)
                    .load(R.drawable.ic_header_haze)
                    .into(img_icon)
            } else if (des.main == "snow" || des.main == "snow") {
                Glide.with(requireView())
                    .asBitmap().override(1080, 600)
                    .load(R.drawable.ic_header_snow)
                    .into(img_icon)
            } else if (des.main == "Rain") {
                Glide.with(requireView())
                    .asBitmap().override(1080, 600)
                    .load(R.drawable.ic_header_rain)
                    .into(img_icon)
            } else if (des.main == "Clouds") {
                Glide.with(requireView())
                    .asBitmap().override(1080, 600)
                    .load(R.drawable.ic_header_clouds)
                    .into(img_icon)
            }

            viewModel.getDaysWeather(response.coord.lat, response.coord.lon)
        })
    }

    private fun setUpRecyclerView() {
        weatherAdapter = WeatherAdapter(this)
        binding.rvSevendays.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = weatherAdapter
            setOnClickListener {

            }
        }
        viewModel.dailyWeather.observe(requireActivity(), {
            weatherAdapter.differ.submitList(it.daily)
        })

    }

    private fun checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it != null) {
                lat = it.latitude
                lon = it.longitude
                Log.d(TAG, "checkLocationPermission: $lat $lon")
                setUpCurrentWeatherByUserLocation(lon, lat)
            }
        }
    }

    override fun onItemClick(daily: Daily) {
        currentItemDaily = daily
        val bundle = Bundle().apply {
            putSerializable("daily", currentItemDaily)
        }
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)

    }

}