package com.example.tmdb_challenge.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tmdb_challenge.data.db.MoviesDao
import com.example.tmdb_challenge.data.db.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun providesDataBase(@ApplicationContext appContext: Context): MoviesDatabase {

        val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT OR REPLACE INTO MovieListEntity VALUES(1,'POPULAR');")
                db.execSQL("INSERT OR REPLACE INTO MovieListEntity VALUES(2,'TOP_RATED');")
                db.execSQL("INSERT OR REPLACE INTO MovieListEntity VALUES(3,'RECOMMENDED');")
            }
        }

        return Room.databaseBuilder(
            appContext,
            MoviesDatabase::class.java,
            "moviesDatabase"
        )
            .addCallback(roomCallback)
            .build()

    }

    @Provides
    fun provideMoviesDaos(moviesDatabase: MoviesDatabase): MoviesDao =
        moviesDatabase.moviesDao()
}

