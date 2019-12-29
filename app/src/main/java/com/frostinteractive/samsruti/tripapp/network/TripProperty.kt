/*
 * Copyright (c) 2019. All rights reserved.
 * Samsruti Dash
 * sam.sipun@gmail.com
 */

package com.frostinteractive.samsruti.tripapp.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TripProperty(
    @Json(name = "tripId")
    val tripId: String,

    @Json(name = "lat")
    val lat: Double,

    @Json(name = "lng")
    val lng: Double,

    @Json(name = "bearing")
    val bearing: Double,

    @Json(name = "speed")
    val speed: Double,

    @Json(name = "rpm")
    val rpm: Double,

    @Json(name = "isSaved")
    val isSaved: Boolean
) : Parcelable

