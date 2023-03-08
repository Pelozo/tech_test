package com.example.tmdb_challenge.usecases.location

import com.example.tmdb_challenge.data.datasource.FirebaseDataSource
import javax.inject.Inject

class GetPastLocationsUseCase @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun invoke() =
        firebaseDataSource.getPastLocations()


}