package com.example.tmdb_challenge.usecases.image

import android.graphics.Bitmap
import android.net.Uri
import com.example.tmdb_challenge.data.datasource.FirebaseDataSource
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun invoke(bitmap: Bitmap) =
        firebaseDataSource.uploadPhotoAsync(bitmap)

    suspend fun invoke(uri: Uri) =
        firebaseDataSource.uploadPhotoAsync(uri)

}