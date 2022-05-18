package com.levanda.jokeapp.model

import com.google.gson.annotations.SerializedName

data class JokeModel(
    @SerializedName("setup")
    var joke: String,
    @SerializedName("punchline")
    var answer: String,
)
