package com.example.tmdb_challenge.data.repository

import com.example.tmdb_challenge.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieTrailer(movieId: String): String?
    fun getMovies(): Flow<List<Movie>>
    suspend fun getTopRatedMovies(page: Int): Flow<List<Movie>>
    suspend fun getPopularMovies(page: Int): Flow<List<Movie>>
    suspend fun getRecommended(page: Int, region: String): Flow<List<Movie>>
}