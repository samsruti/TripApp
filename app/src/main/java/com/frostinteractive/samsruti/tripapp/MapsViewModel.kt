/*
 * Copyright (c) 2019. All rights reserved.
 * Samsruti Dash
 * sam.sipun@gmail.com
 */

package com.frostinteractive.samsruti.tripapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.frostinteractive.samsruti.tripapp.network.TripApi
import com.frostinteractive.samsruti.tripapp.network.TripProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class TripApiStatus { LOADING, ERROR, DONE }

class MapsViewModel(private val app: Application) : AndroidViewModel(app) {


    //
    private val _selectedTrip = MutableLiveData<TripProperty>()
    val selectedTrip: LiveData<TripProperty>
        get() = _selectedTrip


    val displaySpeed = Transformations.map(selectedTrip) {
        app.applicationContext.getString(R.string.speed_value_mph, it.speed)
    }

    val displayRPM = Transformations.map(selectedTrip) {
        app.applicationContext.getString(R.string.rpm_value, it.rpm)
    }

    private val _distance = MutableLiveData<Float>()
    val distance = Transformations.map(_distance) {
        app.applicationContext.getString(R.string.miles_value, it)
    }


    private val _status = MutableLiveData<TripApiStatus>()
    val status: LiveData<TripApiStatus>
        get() = _status

    private val _size = MutableLiveData<Int>()
    val size: LiveData<Int>
        get() = _size


    private val _userTrips = MutableLiveData<List<TripProperty>>()
    val userTrips: LiveData<List<TripProperty>>
        get() = _userTrips


    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        getUserTrips()
        _distance.value = 0.0F
    }

    private fun getUserTrips() {
        coroutineScope.launch {
            val deferredTripResult = TripApi.retrofitService.getUserTrip()
            try {

                _status.value = TripApiStatus.LOADING
                val result = deferredTripResult.await()
                _userTrips.value = result
                _size.value = result.size - 1

                updateSelectedProperty(index = 0)

                _status.value = TripApiStatus.DONE
            } catch (e: Exception) {
                _userTrips.value = ArrayList()
                _status.value = TripApiStatus.ERROR
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun updateSelectedProperty(index: Int) {
        _selectedTrip.value = _userTrips.value!![index]
    }

    fun updateDistance(distanceValue: Float) {
        _distance.value = distanceValue
    }


}