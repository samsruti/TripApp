/*
 * Copyright (c) 2019. All rights reserved.
 * Samsruti Dash
 * sam.sipun@gmail.com
 */

package com.frostinteractive.samsruti.tripapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Simple ViewModel factory that provides the MarsProperty and context to the ViewModel.
 */
class MapsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
