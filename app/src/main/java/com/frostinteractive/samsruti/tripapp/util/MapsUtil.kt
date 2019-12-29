/*
 * Copyright (c) 2019. All rights reserved.
 * Samsruti Dash
 * sam.sipun@gmail.com
 */

package com.frostinteractive.samsruti.tripapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.util.SparseArray
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.frostinteractive.samsruti.tripapp.network.TripProperty
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

val mapTripWithDistance: SparseArray<Float> = SparseArray()

fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

fun GoogleMap.addPolylinesToMap(
    currentLocation: LatLng,
    userTrips: LiveData<List<TripProperty>>
) {

    val list: ArrayList<LatLng> = ArrayList()

    var tempLocation: LatLng = currentLocation
    var distance = 0.0F

    for ((index, trip) in userTrips.value!!.withIndex()) {

        distance = distance.plus(
            calculateDistance(
                tempLocation.latitude, tempLocation.longitude,
                trip.lat, trip.lng
            )
        )
        mapTripWithDistance.put(index, distance)
        val currentTrip = LatLng(trip.lat, trip.lng)
        list.add(currentTrip)
        tempLocation = currentTrip
    }

    zoomRoute(list)


    val polyLineOptions = PolylineOptions().apply {
        addAll(list)
        width(10F)
        color(Color.DKGRAY)
        geodesic(true)
    }
    addPolyline(polyLineOptions)

}

fun calculateDistance(
    startLat: Double,
    startLng: Double,
    endLat: Double,
    endLng: Double
): Float {
    val startLocation = Location("START")
    startLocation.latitude = startLat
    startLocation.longitude = startLng

    val endLocation = Location("START")
    endLocation.latitude = endLat
    endLocation.longitude = endLng

    return startLocation.distanceTo(endLocation)
}

fun GoogleMap.zoomRoute(
    lstLatLngRoute: List<LatLng?>?
) {
    if (lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return
    val boundsBuilder = LatLngBounds.Builder()
    for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint)
    val routePadding = 100
    val latLngBounds = boundsBuilder.build()
    moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding))
}