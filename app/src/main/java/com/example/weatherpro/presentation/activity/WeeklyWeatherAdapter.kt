package com.example.weatherpro.presentation.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpro.R
import com.example.weatherpro.data.models.HourlyWeatherModel
import com.example.weatherpro.databinding.ItemWeeklyWeatherBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeeklyWeatherAdapter: RecyclerView.Adapter<WeeklyWeatherAdapter.WeeklyWeatherViewHolder>() {

    private var list = mutableListOf<HourlyWeatherModel.Date>()

    @SuppressLint("NotifyDataSetChanged")
    fun addData(weatherData: List<HourlyWeatherModel.Date>){
        list.clear()
        list.addAll(weatherData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyWeatherViewHolder {
        return WeeklyWeatherViewHolder(
            ItemWeeklyWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: WeeklyWeatherViewHolder, position: Int) {
        //holder.toBind(list[position])
        val currentItem = list[position]
        if (currentItem.dt_txt.endsWith("12:00:00")) holder.toBind(currentItem)
        else {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0,0)
        }
    }

    inner class WeeklyWeatherViewHolder(private val binding: ItemWeeklyWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun toBind(weeklyWeatherData: HourlyWeatherModel.Date) {
            val weatherType = weeklyWeatherData.weather.first().main
            val iconResId = when (weatherType) {
                "Clouds" -> R.drawable.ic_cloudy_rv
                "Clear" -> R.drawable.ic_sunny
                "Rain" -> R.drawable.ic_rainy
                "Snow" -> R.drawable.ic_snow
                "Thunderstorm" -> R.drawable.ic_rainy_thundershtorm
                else -> R.drawable.ic_rainy_with_sun
            }
            binding.ivWeather.setImageResource(iconResId)
            binding.tvDayTemperature.text = weeklyWeatherData.main.temp.toInt().toString() + "°C"

            val dtTxt = weeklyWeatherData.dt_txt
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = sdf.parse(dtTxt)
            val calendar = Calendar.getInstance().apply {
                time = date
            }
            val dayOfWeek = SimpleDateFormat("EEE", Locale.ENGLISH).format(calendar.time).uppercase()
            //val dayOfWeek = dateFormat.format(calendar.time).uppercase(Locale.getDefault())
            binding.tvDayOfWeek.text = dayOfWeek
        }
    }
}