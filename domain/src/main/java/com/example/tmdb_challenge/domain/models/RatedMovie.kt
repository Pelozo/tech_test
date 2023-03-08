package com.example.tmdb_challenge.domain.models

data class RatedMovie(
    val movieId: Int,
    val title: String,
    val posterPath: String,
    val rate: Float
)