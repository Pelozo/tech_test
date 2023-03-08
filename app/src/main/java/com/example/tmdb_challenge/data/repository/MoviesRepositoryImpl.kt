package com.example.tmdb_challenge.data.repository

import com.example.tmdb_challenge.data.datasource.MoviesLocalDataSource
import com.example.tmdb_challenge.data.datasource.MoviesRemoteDataSource
import com.example.tmdb_challenge.data.util.Result
import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.domain.models.entitities.MovieListWithMoviesCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val moviesLocalDataSource: MoviesLocalDataSource
) : MovieRepository {

    override fun getMovies(): Flow<List<Movie>> {
        return moviesLocalDataSource.getMovies()
    }

    override suspend fun getMovieTrailer(movieId: String): String? {
        return when (val trailers = withTimeout(5000) {
            moviesRemoteDataSource.getMovieTrailer(movieId)
        }) {
            is Result.Success -> {
                if (trailers.data.isEmpty()) {
                    null
                } else {
                    trailers.data[0]
                }
            }
            is Result.Error -> null
        }
    }

    override suspend fun getTopRatedMovies(page: Int): Flow<List<Movie>> {
        return when (val remoteMovies = withTimeout(5000) {
            moviesRemoteDataSource.getTopRatedMovies(page)
        }) {
            is Result.Success -> {
                CoroutineScope(IO).launch {
                    moviesLocalDataSource.saveMovies(remoteMovies.data)
                        .onEach { movieId ->
                            moviesLocalDataSource.saveMovieToList(
                                MovieListWithMoviesCrossRef(movieId = movieId, listId = 2)
                            )
                        }
                }
                flow{
                    emit(remoteMovies.data)
                }
            }
            is Result.Error -> {
                remoteMovies.exception.printStackTrace()
                if(page == 1) moviesLocalDataSource.getTopRatedMovies() else emptyFlow()
            }
        }
    }


    override suspend fun getPopularMovies(page: Int): Flow<List<Movie>> {
        return when (val remoteMovies = withTimeout(5000) {
            moviesRemoteDataSource.getPopularMovies(page)
        }) {
            is Result.Success -> {
                CoroutineScope(IO).launch {
                    moviesLocalDataSource.saveMovies(remoteMovies.data)
                        .onEach { movieId ->
                            moviesLocalDataSource.saveMovieToList(
                                MovieListWithMoviesCrossRef(movieId = movieId, listId = 1)
                            )
                        }
                }
                flow{
                    emit(remoteMovies.data)
                }
            }
            is Result.Error -> {
                remoteMovies.exception.printStackTrace()
                if(page == 1) moviesLocalDataSource.getPopularMovies() else emptyFlow()
            }
        }
    }

    override suspend fun getRecommended(page: Int, region: String): Flow<List<Movie>> {
        return when (val remoteMovies = withTimeout(5000) {
            moviesRemoteDataSource.getRecommendedMovies(page, region)
        }) {
            is Result.Success -> {
                CoroutineScope(IO).launch {
                    moviesLocalDataSource.saveMovies(remoteMovies.data)
                        .onEach { movieId ->
                            moviesLocalDataSource.saveMovieToList(
                                MovieListWithMoviesCrossRef(movieId = movieId, listId = 3)
                            )
                        }
                }
                flow{
                    emit(remoteMovies.data)
                }
            }
            is Result.Error -> {
                remoteMovies.exception.printStackTrace()
                if(page == 1) moviesLocalDataSource.getRecommended() else emptyFlow()
            }
        }
    }
}