package com.fkammediacenter.psak.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latitude :Double = 0.0
    private var longitude:Double = 0.0
    private var Outlet =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.fkammediacenter.psak.R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(com.fkammediacenter.psak.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }


    override fun onMapReady(googleMap: GoogleMap) {
        val zoomLevel = 16.0f
        val extras = intent.extras
        latitude = extras.getString("Latitude").toDouble()
        longitude = extras.getString("Longitude").toDouble()
        Outlet = extras.getString("Outlet")
        val position = latitude.let { longitude.let { it1 -> LatLng(it, it1) } }


        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(position).title(Outlet))

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position,zoomLevel)

        mMap.animateCamera(cameraUpdate)

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
