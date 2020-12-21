package com.example.jcdecaux

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jcdecaux.Constants.Constants
import com.example.jcdecaux.Models.StaionModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap
    val constants = Constants()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchJSON(constants.API_URL)
    }

    fun fetchJSON(withURL: String){
        val request = Request.Builder().url(withURL).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("failed to get server response")
            }

            override fun onResponse(call: Call, response: Response) {
                println("got server response")
                val body = response?.body?.string()
                val gson = GsonBuilder().create()
                val stationsList = gson.fromJson(body , Array<StaionModel>::class.java).toList()
                runOnUiThread {
                    implementGoogleMaps(stationsList)
                }
            }
        })
    }

    fun implementGoogleMaps(annotations : List<StaionModel>){
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            val firstPosition = annotations.first()
            val firstLocation = LatLng(firstPosition.position.lat, firstPosition.position.lng)
            for (annotation in annotations){
                val location = LatLng(annotation.position.lat,annotation.position.lng)
                googleMap.addMarker(MarkerOptions().position(location).title(annotation.name))
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 14f))
        })
    }

}