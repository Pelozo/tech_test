package com.example.tmdb_challenge.di

import com.example.tmdb_challenge.BuildConfig
import com.example.tmdb_challenge.interceptors.AuthInterceptor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient = OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()

    @Provides
    fun provideAuthInterceptor() = AuthInterceptor()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.TMDB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage =
        FirebaseStorage.getInstance()

    @Provides
    fun provideFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Named(PROFILE_RETROFIT)
    fun provideProfileRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.PROFILE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    const val PROFILE_RETROFIT = "profile_retrofit"
}