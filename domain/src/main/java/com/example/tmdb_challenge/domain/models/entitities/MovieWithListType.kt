package com.example.tmdb_challenge.domain.models.entitities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithListType(
    @Embedded val movieList: MovieListEntity,
    @Relation(
        parentColumn = "listId",
        entityColumn = "movieId",
        associateBy = Junction(MovieListWithMoviesCrossRef::class)
    )
    val movies: List<MovieEntity>
)