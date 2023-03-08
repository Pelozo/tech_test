package com.example.tmdb_challenge.data.datasource

import com.example.tmdb_challenge.api.MoviesService
import com.example.tmdb_challenge.data.mappers.mapToModel
import com.example.tmdb_challenge.data.util.Result
import com.example.tmdb_challenge.data.util.safeApiCall
import com.example.tmdb_challenge.domain.models.Movie
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val moviesService: MoviesService
) : MoviesRemoteDataSource {
    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        return safeApiCall {
            moviesService.getPopularMovies(page)
                .results
                .map {
                    it.mapToModel()
                }
        }
    }
    override suspend fun getMovieTrailer(movieId: String): Result<List<String>> {
        return safeApiCall {
            moviesService.getVideoByMovie(movieId).results.map {
                it.key
            }
        }
    }
    override suspend fun getTopRatedMovies(page: Int): Result<List<Movie>> {
        return safeApiCall {
            moviesService.getTopRated(page)
                .results
                .map {
                    it.mapToModel()
                }
        }
    }
    override suspend fun getRecommendedMovies(page: Int, region: String): Result<List<Movie>> {
        return safeApiCall {
            moviesService.getRecommended(page, region)
                .results
                .map {
                    it.mapToModel()
                }
        }
    }
}
