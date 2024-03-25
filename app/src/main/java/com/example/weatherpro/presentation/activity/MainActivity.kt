package com.example.weatherpro.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherpro.R
import com.example.weatherpro.data.local.prefs.Pref
import com.example.weatherpro.data.models.HourlyWeatherModel
import com.example.weatherpro.data.models.WeatherModel
import com.example.weatherpro.databinding.ActivityMainBinding
import com.example.weatherpro.utils.UiState
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel
    private lateinit var hourlyWeatherViewModel: HourlyWeatherViewModel
    private lateinit var weeklyWeatherViewModel: WeeklyWeatherViewModel
    private val adapter = WeeklyWeatherAdapter()
    private var entries = ArrayList<Entry>()
    private val pref:Pref by lazy { Pref(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initUserCity()
        initListeners()
        initRecyclerView()
        initRequest(binding.etCity.text.toString())
        initStateFlow()

    }

    private fun initUserCity() {
        val defaultCity = getString(R.string.bishkek)
        val userCity = pref.getUserCityName()?: defaultCity
        binding.etCity.setText(userCity)
    }

    private fun initRecyclerView() {
        binding.rvWeeklyWeather.adapter = adapter
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            initRequest(binding.etCity.text.toString())
            initStateFlow()
            binding.swipeRefresh.isRefreshing = false
        }
        binding.etCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                pref.setUserCityName(binding.etCity.text.toString())
                initRequest(binding.etCity.text.toString())
                initStateFlow()
                true
            } else {
                false
            }
        }
        binding.btnSearch.setOnClickListener {
            binding.etCity.requestFocus()
            binding.etCity.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etCity,InputMethodManager.SHOW_IMPLICIT)

        }
    }

    private fun initViewModel() {
        currentWeatherViewModel = ViewModelProvider(this)[CurrentWeatherViewModel::class.java]
        hourlyWeatherViewModel = ViewModelProvider(this)[HourlyWeatherViewModel::class.java]
        weeklyWeatherViewModel = ViewModelProvider(this)[WeeklyWeatherViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun initStateFlow() {
        lifecycleScope.launch {
            currentWeatherViewModel.viewState.collect{
                when(it){
                    is UiState.Empty ->
                        Toast.makeText(this@MainActivity, "Empty", Toast.LENGTH_SHORT).show()
                    is UiState.Error ->{
                        Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@MainActivity, "Неверный город,введите корректное название", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading ->
                        binding.pbLoading.visibility = View.VISIBLE
                    is UiState.Success -> {
                        binding.pbLoading.visibility = View.GONE
                        binding.tvTypeOfWeather.text = it.data?.weather?.firstOrNull()?.main

                        setWeatherIcon(binding.ivTypeOfWeather, it.data!!)
                        binding.tvTemperature.text = it.data.main.temp.toInt().toString() + "°C"
                        setDateAndDayOfWeek(binding.tvDayWeekAndDate)
                        checkRain(it.data)

                    }
                }
            }
        }
        lifecycleScope.launch {
            hourlyWeatherViewModel.viewState.collect{
                when(it){
                    is UiState.Empty ->
                        Toast.makeText(this@MainActivity, "Empty", Toast.LENGTH_SHORT).show()
                    is UiState.Error ->
                        Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    is UiState.Loading ->
                        binding.pbLoading.visibility = View.VISIBLE
                    is UiState.Success -> {
                        binding.pbLoading.visibility = View.GONE
                        //it.data?.list?.let { it1 -> adapter.addData(it1) }

                        entries = convertHourlyWeatherToEntries(it.data!!)
                        val dataSet = LineDataSet(entries,"")
                        binding.linechartGraphicWeather.data = LineData(dataSet)
                        binding.linechartGraphicWeather.invalidate()

                    }
                }
            }
        }
        lifecycleScope.launch {
            weeklyWeatherViewModel.viewState.collect{
                when(it){
                    is UiState.Empty ->
                        Toast.makeText(this@MainActivity, "Empty", Toast.LENGTH_SHORT).show()
                    is UiState.Error ->
                        Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    is UiState.Loading ->
                        binding.pbLoading.visibility = View.VISIBLE
                    is UiState.Success -> {
                        binding.pbLoading.visibility = View.GONE
                        it.data?.list?.let { it1 -> adapter.addData(it1) }

                    }
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun checkRain(model: WeatherModel) {
        if (model.weather.firstOrNull()?.main == "Rain" || model.weather.firstOrNull()?.main == "Thunderstorm") {
            binding.mainContainer.setBackgroundResource(R.drawable.img_thunderstorm)
            binding.containerPeriodOfDay.setBackgroundColor(R.color.grey_transparent)
        } else {
            binding.mainContainer.setBackgroundResource(R.color.yellow)
            binding.containerPeriodOfDay.setBackgroundResource(R.color.yellow_transparent)
        }
    }

    private fun initRequest(cityName:String) {
        lifecycleScope.launch { currentWeatherViewModel.getCurrentWeather(cityName = cityName) }
        lifecycleScope.launch { hourlyWeatherViewModel.getHourlyWeather(cityName = cityName) }
        lifecycleScope.launch { weeklyWeatherViewModel.getWeeklyWeather(cityName = cityName) }
    }

    private fun setWeatherIcon(imageView: ImageView, weatherModel: WeatherModel) {
        val weatherType = weatherModel.weather.firstOrNull()?.main ?: return
        val iconResId = when (weatherType) {
            "Clouds" -> R.drawable.ic_cloudy_with_sun
            "Mist" -> R.drawable.ic_mist
            "Clear" -> R.drawable.ic_sunny
            "Rain" -> R.drawable.ic_rainy
            "Snow" -> R.drawable.ic_snow
            "Thunderstorm" -> R.drawable.ic_rainy_thundershtorm
            else -> R.drawable.ic_rainy_with_sun
        }
        imageView.setImageResource(iconResId)
    }

    private fun setDateAndDayOfWeek(textView:TextView){
        val currentDate = Calendar.getInstance().time
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault()) //Формат для дня недели (например, "Пн", "Вт")
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Формат для даты (например, "12 Dec 2023")

        val dayOfWeek = dayOfWeekFormat.format(currentDate)
        val date = dateFormat.format(currentDate)

        val formattedDate = "$dayOfWeek | $date"
        textView.text = formattedDate
    }

    private fun convertHourlyWeatherToEntries(hourlyWeatherModel: HourlyWeatherModel):ArrayList<Entry>{
        val entries = arrayListOf<Entry>()
        hourlyWeatherModel.list.forEachIndexed { _, data ->
            val temperature = data.main.temp.toInt().toFloat()
            val timeString = convertDtTxtToTime(data.dt_txt)
            val timestamp = convertTimeToFloat(timeString)
            val entry = Entry(timestamp,temperature)
            entries.add(entry)
        }
        return entries
    }

    private fun convertDtTxtToTime(dtTxt: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(dtTxt, formatter)
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    private fun convertTimeToFloat(time: String): Float {
        val parts = time.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()

        return hours.toFloat() + minutes.toFloat() / 60
    }

}