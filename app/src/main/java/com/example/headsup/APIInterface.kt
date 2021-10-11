package com.example.headsup

import retrofit2.Call
import retrofit2.http.*


interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET("/celebrities/")
    fun getCelebrities(): Call<List<Celebrities.Celebrity>>

    @Headers("Content-Type: application/json")
    @POST("/celebrities/")
    fun addCelebrity(@Body celebrityData: Celebrities.Celebrity): Call<Celebrities.Celebrity>

    @Headers("Content-Type: application/json")
    @PUT("/celebrities/{id}")
    fun updateCelebrity(@Path("id") id: Int, @Body celebrityData: Celebrities.Celebrity): Call<Celebrities.Celebrity>

    @Headers("Content-Type: application/json")
    @DELETE("/celebrities/{id}")
    fun deleteCelebrity(@Path("id") id: Int): Call<Void>
}