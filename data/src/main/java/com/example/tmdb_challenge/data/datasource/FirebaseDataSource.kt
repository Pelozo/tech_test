package com.example.tmdb_challenge.data.datasource

import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import com.example.tmdb_challenge.domain.models.firestore.LocationFirestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface FirebaseDataSource {
    suspend fun uploadPhotoAsync(image: Bitmap): Deferred<Boolean>
    suspend fun uploadPhotoAsync(uri: Uri): Deferred<Boolean>
    suspend fun uploadLocationAsync(location: Location):Deferred<Boolean>
    suspend fun getPastLocations(): Flow<List<LocationFirestore>>
}