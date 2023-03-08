package com.example.tmdb_challenge.di

import com.example.tmdb_challenge.data.datasource.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {
    @Provides
    fun providesMoviesRemoteDataSource(moviesRemoteDataSourceImpl: MoviesRemoteDataSourceImpl): MoviesRemoteDataSource = moviesRemoteDataSourceImpl

    @Provides
    fun providesMoviesLocalDataSource(moviesLocalDataSourceImpl: MoviesLocalDataSourceImpl): MoviesLocalDataSource = moviesLocalDataSourceImpl

    @Provides
    fun providesFirebaseDataSource(firebaseDataSourceImpl: FirebaseDataSourceImpl): FirebaseDataSource = firebaseDataSourceImpl

    @Provides
    fun providesProfileDataSource(profileDataSourceImpl: ProfileDataSourceImpl): ProfileDataSource = profileDataSourceImpl
}