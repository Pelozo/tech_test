package com.example.tmdb_challenge.domain.models.entitities

import androidx.room.Entity

@Entity(
    primaryKeys = ["listId", "movieId"]
)
data class MovieListWithMoviesCrossRef(
    val listId: Long,
    val movieId: Long
)