package com.example.weatherapp.Views

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.weatherapp.Adapter.ForecastAdapter
import com.example.weatherapp.Model.CurrentResponseApi
import com.example.weatherapp.Model.ForecastResponceApi
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.WeatherViewModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.github.matteobattilana.weather.PrecipType
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private  val weatherViewModel : WeatherViewModel by viewModels()
    private val calendar by lazy {Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))}
    private  val forecastAdapter by lazy { ForecastAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        //current temp
        binding.apply {
            var lat = 51.50
            var lon = -0.12
            var name = "London"
            citytxt.text  = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat,lon,"metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    p0: Call<CurrentResponseApi>,
                    p1: Response<CurrentResponseApi>,
                ) {
                    if(p1.isSuccessful){
                        val data = p1.body()
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        data?.let {
                            statusTxt.text = it.weather?.get(0)?.main ?: "-"
                            windTxt.text = it.wind?.speed.let{Math.round(it!!).toString() } + "Km"
                            humidityTxt.text = it.main?.humidity.toString() + "%"
                            currentTempTxt.text = it.main?.temp.let{Math.round(it!!).toString()} + "°"
                            maxTempTxt.text = it.main?.tempMax.let{Math.round(it!!).toString()} + "°"
                            minTempTxt.text = it.main?.tempMin.let{Math.round(it!!).toString()} + "°"
                        val drawable = if(isNightNow()) R.drawable.night_bg else
                            setDynamicallyWallpaper(
                                it.weather?.get(0)?.icon?:"-"
                            )
//                        }
                            bgImg.setImageResource(drawable)
                            setEffectRainSnow(it.weather?.get(0)?.icon?:"-")
                        }
                    }
                }

                override fun onFailure(p0: Call<CurrentResponseApi>, p1: Throwable) {
                    Toast.makeText(this@MainActivity, p1.toString(), Toast.LENGTH_SHORT).show()
                }

            })
            //setting blur view
            var radius = 10f
            val decorView = window.decorView
            val rootView = (decorView.findViewById(android.R.id.content) as ViewGroup?)
            val windowBackground = decorView.background
            rootView?.let{
                blurView.setupWith(it,RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
                blurView.clipToOutline = true
            }
            //forecast temp
            weatherViewModel.loadCurrentForecast(lat,lon,"metric").enqueue(object : retrofit2.Callback<ForecastResponceApi>{
                override fun onResponse(
                    p0: Call<ForecastResponceApi>,
                    p1: Response<ForecastResponceApi>,
                ) {
                    if (p1.isSuccessful) {
                        val data = p1.body()
                        blurView.visibility = View.VISIBLE
                        data?.let {
                            forecastView.adapter = forecastAdapter.apply {
                                differ.submitList(it.list)
                            }
                            forecastView.layoutManager = LinearLayoutManager(this@MainActivity,
                                LinearLayoutManager.HORIZONTAL ,
                                false)
                        }
                    }
                }
                override fun onFailure(p0: Call<ForecastResponceApi>, p1: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

    }



    private  fun isNightNow():Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }
    private fun setDynamicallyWallpaper(icon:String):Int{
        return when(icon.dropLast(1)){
            "01"->{
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }
            "02","03","04"->{
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }
            "09","10","11"->{
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg
            }
            "13"->{
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg
            }
            "50"->{
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }
            else -> 0
        }
    }
    private fun setEffectRainSnow(icon:String){
         when(icon.dropLast(1)){
            "01"->{
                initWeatherView(PrecipType.CLEAR)
            }
            "02","03","04"->{
                initWeatherView(PrecipType.CLEAR)
            }
            "09","10","11"->{
                initWeatherView(PrecipType.RAIN)
            }
            "13"->{
                initWeatherView(PrecipType.SNOW)
            }
            "50"->{
                initWeatherView(PrecipType.CLEAR)
            }
        }
    }
    private fun initWeatherView(type:PrecipType){
    binding.weatherView.apply {
        setWeatherData(type)
        angle= -20
        emissionRate-100.0f
    }
    }
}