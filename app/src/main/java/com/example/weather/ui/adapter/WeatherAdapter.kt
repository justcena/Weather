package com.example.weather.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.model.days.Daily
import kotlinx.android.synthetic.main.sevendays_item.view.*
import java.text.SimpleDateFormat

class WeatherAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val TAG = "LOG-->"

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallBack = object : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean =
            oldItem.weather[0].id == newItem.weather[0].id


        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean =
            oldItem == newItem

    }
    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder =
        WeatherViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.sevendays_item, parent, false
            )
        )

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val daysWeather: Daily = differ.currentList[position]

        holder.itemView.apply {
            val description = daysWeather.weather[0]
            tv_temp.text = daysWeather.temp.max.toString()
            tv_day.text = getDate(daysWeather.dt.toLong())
            tv_description.text = description.description
            setOnClickListener {
                listener.onItemClick(daysWeather)
                Log.d(TAG, "onBindViewHolder: ------------> ${daysWeather}")
            }
            when (description.main) {
                "Rain" ->
                    Glide.with(this)
                        .load("https://openweathermap.org/img/wn/" + "${description.icon}" + ".png")
                        .into(img_icon)
                "Clouds" -> Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + "${description.icon}" + ".png")
                    .into(this.img_icon)
                "Clear" -> Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + "${description.icon}" + ".png")
                    .into(this.img_icon)
                else -> Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + "${description.icon}" + ".png")
                    .into(this.img_icon)
            }
        }
    }

    //    override fun getItemCount(): Int = dailyWeather.size
    override fun getItemCount(): Int = differ.currentList.size
    private val date = SimpleDateFormat("dd")
    private fun getDate(time: Long): String = date.format(time * 1000L)


//    private var onItemClicked: ((Daily) -> Unit)? = null
//
//    fun setOnItemClickListener(listener: (Daily) -> Unit) {
//        onItemClicked = listener
//    }

    interface OnItemClickListener {
        fun onItemClick(daily: Daily)
    }

}
