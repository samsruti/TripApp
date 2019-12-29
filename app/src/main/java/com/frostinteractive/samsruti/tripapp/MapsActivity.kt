/*
 * Copyright (c) 2019. All rights reserved.
 * Samsruti Dash
 * sam.sipun@gmail.com
 */

package com.frostinteractive.samsruti.tripapp

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frostinteractive.samsruti.tripapp.databinding.ActivityMainBinding
import com.frostinteractive.samsruti.tripapp.util.addPolylinesToMap
import com.frostinteractive.samsruti.tripapp.util.bitmapDescriptorFromVector
import com.frostinteractive.samsruti.tripapp.util.calculateDistance
import com.frostinteractive.samsruti.tripapp.util.mapTripWithDistance
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MapsViewModel

    private lateinit var markerSuv: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = requireNotNull(this).application
        val modelFactory = MapsViewModelFactory(application)
        viewModel =
            ViewModelProviders.of(this, modelFactory).get(MapsViewModel::class.java)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.setLifecycleOwner(this)

        binding.userTripViewModel = viewModel
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        viewModel.userTrips.observe(this, Observer {

            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        })


        viewModel.selectedTrip.observe(this, Observer { resultTrip ->
            binding.selectedTrip = resultTrip


            binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, p1: Int, p2: Boolean) {

                    viewModel.updateSelectedProperty(p1)
                    viewModel.updateDistance(mapTripWithDistance[p1])

                    val currentLocation: LatLng = viewModel.selectedTrip.value!!.let {
                        LatLng(
                            it.lat,
                            it.lng
                        )
                    }


                    markerSuv.position = currentLocation
                    val distance = calculateDistance(
                        currentLocation.latitude, currentLocation.longitude,
                        viewModel.userTrips.value!![0].lat,
                        viewModel.userTrips.value!![0].lng
                    )

                    binding.totalMilesTv.setText(distance.toString())

                }

                override fun onStartTrackingTouch(p0: SeekBar) {

                }

                override fun onStopTrackingTouch(p0: SeekBar) {

                }

            })


        })


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val currentLocation: LatLng = viewModel.selectedTrip.value!!.let {
            LatLng(
                it.lat,
                it.lng
            )
        }

        val finalLocation: LatLng = viewModel.userTrips.value!!.get(viewModel.size.value!!).let {
            LatLng(
                it.lat,
                it.lng
            )
        }



        mMap.addPolylinesToMap(currentLocation, viewModel.userTrips)


        mMap.addMarker(
            MarkerOptions()
                .position(currentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_point))
                .title("Starting Point")
        )

        mMap.addMarker(
            MarkerOptions()
                .position(finalLocation)
                .title("Destination Point")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point))
        )

        val markerOptions = MarkerOptions()
            .position(currentLocation)
            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_car_suv))

        markerSuv = mMap.addMarker(markerOptions)


//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))

//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds())
    }


}
