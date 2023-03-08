package com.example.tmdb_challenge.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tmdb_challenge.domain.models.entitities.MovieEntity
import com.example.tmdb_challenge.domain.models.entitities.MovieListEntity
import com.example.tmdb_challenge.domain.models.entitities.MovieListWithMoviesCrossRef

@Database(
    entities = [
        MovieEntity::class,
        MovieListEntity::class,
        MovieListWithMoviesCrossRef::class
    ],
    version = 1
)

abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}