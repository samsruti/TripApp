/*
 * Copyright (c) 2019. All rights reserved.
 * Samsruti Dash
 * sam.sipun@gmail.com
 */

package com.frostinteractive.samsruti.tripapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://classicludo.com/apis/islot/v14/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface TripApiService {

    @GET("userTrip")
    fun getUserTrip():
            Deferred<List<TripProperty>>
}

object TripApi {
    val retrofitService: TripApiService by lazy {
        retrofit.create(TripApiService::class.java)
    }
}