package com.submission.aplikasidicodingevent.retrofit

import com.submission.aplikasidicodingevent.response.DetailResponse
import com.submission.aplikasidicodingevent.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active")
        active: Int = 1
    ): Call<EventResponse>

    @GET("events")
    fun getFinishEvents(
        @Query("active")
        active: Int = 0
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<DetailResponse>
}