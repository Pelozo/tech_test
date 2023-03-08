package com.example.tmdb_challenge.usecases.location

import android.location.Location
import com.example.tmdb_challenge.data.datasource.FirebaseDataSource
import javax.inject.Inject

class UploadCurrentLocationUseCase @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun invoke(location: Location) =
        firebaseDataSource.uploadLocationAsync(location)


}