package com.levanda.jokeapp.service

import com.levanda.jokeapp.model.JokeModel
import retrofit2.Call
import retrofit2.http.GET

interface JokeAPI {
    @GET("/")
    fun fetch(): Call<JokeModel>
}