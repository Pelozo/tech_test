package com.example.tmdb_challenge.domain.models.responses

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val user: String,
    val creation: Long,
    @SerializedName("rated_movies")
    val ratedMovies: List<RatedMovieResponse>
)