package com.example.tmdb_challenge.api

import com.example.tmdb_challenge.domain.models.responses.MovieTrailersResponse
import com.example.tmdb_challenge.domain.models.responses.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("page") page: Int
    ): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getRecommended(
        @Query("page") page: Int,
        @Query("region") region: String
    ): MoviesResponse

    @GET("movie/{movieId}/videos")
    suspend fun getVideoByMovie(
        @Path("movieId") movieId: String
    ): MovieTrailersResponse

}