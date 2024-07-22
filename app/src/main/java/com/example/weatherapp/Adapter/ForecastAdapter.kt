package com.example.weatherapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.Model.ForecastResponceApi
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForecastViewholderBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    private lateinit var binding: ForecastViewholderBinding
    inner class ForecastViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.forecast_viewholder,parent,false)
        return ForecastViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        binding = ForecastViewholderBinding.bind(holder.itemView)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(differ.currentList[position].dtTxt.toString())
        val calendar = Calendar.getInstance()
        calendar.time= date
        val dayOfWeekName = when(calendar.get(Calendar.DAY_OF_WEEK)){
            1->"Sun"
            2->"Mon"
            3->"Tue"
            4->"Wed"
            5->"Thu"
            6->"Fri"
            7->"Sat"
            else -> "-"
        }
        binding.nameDayTxt.text = dayOfWeekName
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val amPm = if(hour<12) "am" else "pm"
        val hour12 = calendar.get(Calendar.HOUR)
        binding.hourTxt.text= hour12.toString()+ amPm
        binding.tempTxt.text= differ.currentList[position].main?.temp?.let {
            Math.round(it).toString()+"°"
        }
        val icon = when(differ.currentList[position].weather?.get(0)?.icon.toString()){
            "0d","0n"-> "sunny"
            "02d","02n"-> "cloudy_sunny"
            "03d","03n"-> "cloudy_sunny"
            "04d","04n"-> "cloudy"
            "09d","09n"-> "rainy"
            "10d","10n"-> "rainy"
            "11d","11n"-> "storm"
            "13d","13n"-> "snowy"
            "50d","50n"-> "windy"
            else ->"sunny"
        }
        val drawableResourceId = binding.root.resources.getIdentifier(icon,"drawable",binding.root.context.packageName)
        Glide.with(binding.root.context)
            .load(drawableResourceId)
            .into(binding.pic)
    }
    private val differCallback = object : DiffUtil.ItemCallback<ForecastResponceApi.data>(){
        override fun areItemsTheSame(
            oldItem: ForecastResponceApi.data,
            newItem: ForecastResponceApi.data,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastResponceApi.data,
            newItem: ForecastResponceApi.data,
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}