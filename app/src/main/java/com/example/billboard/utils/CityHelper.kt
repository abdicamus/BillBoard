package com.example.billboard.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object CityHelper {

    fun getAllCountries(context: Context): ArrayList<String> {
        val countriesArray = ArrayList<String>()

        try {
            val inputStream: InputStream = context.assets.open("countriesToCities.json")
            val size: Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val countries = jsonObject.names()

            if (countries != null) {
                for (s in 0 until countries.length()) {
                    countriesArray.add(countries.getString(s))
                }
            }
        } catch (e: IOException) {
        }

        return countriesArray
    }

    fun getAllCities(country: String, context: Context): ArrayList<String> {
        val citiesArray = ArrayList<String>()

        try {
            val inputStream: InputStream = context.assets.open("countriesToCities.json")
            val size: Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val cities = jsonObject.getJSONArray(country)

            for (city in 0 until cities.length()) {
                citiesArray.add(cities.getString(city))
            }

        } catch (e: IOException) {

        }

        return citiesArray
    }

    fun filterListData(countriesList: ArrayList<String>, searchText: String?): ArrayList<String> {
        val tempList = ArrayList<String>()
        tempList.clear()

        if (searchText.isNullOrEmpty()) {
            tempList.add("No result")
            return tempList
        }
        for (country in countriesList) {
            if (country.lowercase().startsWith(searchText.lowercase())) {
                tempList.add(country)
            }
        }

        if (tempList.isEmpty()) tempList.add("No result")

        return tempList
    }
}