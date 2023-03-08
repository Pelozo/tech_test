package com.example.tmdb_challenge.domain.models.responses

data class RatedMovieResponse(
    val movieId: Int,
    val title: String,
    val posterPath: String,
    val rate: Float
)