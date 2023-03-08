package com.example.tmdb_challenge.usecases.movies

import com.example.tmdb_challenge.data.repository.MovieRepository
import com.example.tmdb_challenge.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun invoke(page: Int): Flow<List<Movie>> =
        movieRepository.getTopRatedMovies(page)
}