package com.example.weatherpro.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherpro.utils.Constants

class Pref(context:Context) {
    private val sharedPreferences:SharedPreferences =
        context.getSharedPreferences(Constants.PREF_KEY_MAIN,Context.MODE_PRIVATE)

    fun setUserCityName(cityName:String){
        val editor = sharedPreferences.edit()
        editor.putString(Constants.PREF_KEY_USER_CITY_NAME,cityName)
        editor.apply()
    }

    fun getUserCityName():String?{
        return sharedPreferences.getString(Constants.PREF_KEY_USER_CITY_NAME,null)
    }
}