package com.example.tmdb_challenge.di

import com.example.tmdb_challenge.api.MoviesService
import com.example.tmdb_challenge.api.ProfileService
import com.example.tmdb_challenge.di.NetworkModule.PROFILE_RETROFIT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {
    @Provides
    fun provideMoviesService(retrofit: Retrofit): MoviesService =
        retrofit.create(MoviesService::class.java)

    @Provides
    fun provideProfileService(@Named(PROFILE_RETROFIT) retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)
}