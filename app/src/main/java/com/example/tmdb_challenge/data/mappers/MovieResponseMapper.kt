package com.example.tmdb_challenge.data.mappers

import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.domain.models.responses.MovieResponse

fun MovieResponse.mapToModel() =
    Movie(
        id,
        backdropPath,
        originalLanguage,
        title,
        overview,
        posterPath,
        releaseDate,
        voteAverage
    )
