package com.example.tmdb_challenge.data.datasource

import com.example.tmdb_challenge.data.db.MoviesDao
import com.example.tmdb_challenge.data.mappers.mapToEntity
import com.example.tmdb_challenge.data.mappers.mapToModel
import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.domain.models.entitities.MovieListWithMoviesCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesLocalDataSourceImpl @Inject constructor(
    private val moviesDao: MoviesDao
) : MoviesLocalDataSource {
    override fun getMovies(): Flow<List<Movie>> {
        return moviesDao.getAll().map {
            it.flatMap { list ->
                list.movies.map { movie ->
                    val movieModel = movie.mapToModel()
                    movieModel.list = list.movieList.listName
                    movieModel
                }
            }
        }
    }

    override fun getTopRatedMovies(): Flow<List<Movie>> {
        return moviesDao.getTopRated().map {
            it.flatMap { list ->
                list.movies.map { movie ->
                    val movieModel = movie.mapToModel()
                    movieModel.list = list.movieList.listName
                    movieModel
                }
            }
        }
    }

    override fun getPopularMovies(): Flow<List<Movie>> {
        return moviesDao.getPopular().map {
            it.flatMap { list ->
                list.movies.map { movie ->
                    val movieModel = movie.mapToModel()
                    movieModel.list = list.movieList.listName
                    movieModel
                }
            }
        }
    }

    override fun getRecommended(): Flow<List<Movie>> {
        return moviesDao.getRecommended().map {
            it.flatMap { list ->
                list.movies.map { movie ->
                    val movieModel = movie.mapToModel()
                    movieModel.list = list.movieList.listName
                    movieModel
                }
            }
        }
    }


    override suspend fun saveMovies(movies: List<Movie>) =
        moviesDao.insertMovies(movies.map { it.mapToEntity() })

    override suspend fun saveMovieToList(movie: MovieListWithMoviesCrossRef) {
        moviesDao.insertMovieIntoList(movie)
    }

}