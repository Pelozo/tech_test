package com.example.tmdb_challenge.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.tmdb_challenge.domain.models.entitities.MovieEntity
import com.example.tmdb_challenge.domain.models.entitities.MovieListWithMoviesCrossRef
import com.example.tmdb_challenge.domain.models.entitities.MovieWithListType
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = REPLACE)
    fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>): List<Long>

    @Transaction
    @Query("SELECT * FROM MovieListEntity")
    fun getAll(): Flow<List<MovieWithListType>>

    @Query("SELECT * FROM movies WHERE movieId = :movieId")
    fun getById(movieId: Long): Flow<List<MovieEntity>>

    @Insert(onConflict = REPLACE)
    fun insertMovieIntoList(movie: MovieListWithMoviesCrossRef)

    @Transaction
    @Query("SELECT * FROM MovieListEntity WHERE listName == 'TOP_RATED'")
    fun getTopRated(): Flow<List<MovieWithListType>>

    @Transaction
    @Query("SELECT * FROM MovieListEntity WHERE listName == 'POPULAR'")
    fun getPopular(): Flow<List<MovieWithListType>>

    @Transaction
    @Query("SELECT * FROM MovieListEntity WHERE listName == 'RECOMMENDED'")
    fun getRecommended(): Flow<List<MovieWithListType>>

}