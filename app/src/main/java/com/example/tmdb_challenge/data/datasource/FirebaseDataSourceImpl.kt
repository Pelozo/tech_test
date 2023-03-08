package com.example.tmdb_challenge.data.datasource

import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import com.example.tmdb_challenge.domain.models.firestore.LocationFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject


class FirebaseDataSourceImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore
) : FirebaseDataSource {
    override suspend fun uploadPhotoAsync(image: Bitmap): CompletableDeferred<Boolean> {
        return CompletableDeferred<Boolean>().apply {
            val imageRef: StorageReference =
                storage.reference.child("$DEFAULT_PATH_STORAGE/${getRandomName()}")
            imageRef.putBytes(bitmapToByte(image))
                .addOnSuccessListener { _ ->
                    this.complete(true)

                }.addOnFailureListener {
                    it.printStackTrace()
                    this.complete(false)
                }
        }
    }

    override suspend fun uploadPhotoAsync(uri: Uri): CompletableDeferred<Boolean> {
        return CompletableDeferred<Boolean>().apply {
            val imageRef: StorageReference =
                storage.reference.child("$DEFAULT_PATH_STORAGE/${getRandomName()}")
            imageRef.putFile(uri)
                .addOnSuccessListener { _ ->
                    this.complete(true)

                }.addOnFailureListener {
                    it.printStackTrace()
                    this.complete(false)
                }
        }
    }

    override suspend fun uploadLocationAsync(location: Location): Deferred<Boolean> {
        return CompletableDeferred<Boolean>().apply {
            FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener {
                firestore
                    .collection("${FirebaseAuth.getInstance().currentUser?.uid}")
                    .document(getRandomName())
                    .set(location).addOnSuccessListener {
                        this.complete(true)
                    }.addOnFailureListener {
                        this.complete(false)
                        it.printStackTrace()
                    }
            }.addOnFailureListener {
                it.printStackTrace()
                this.complete(false)
            }
        }
    }

    override suspend fun getPastLocations(): Flow<List<LocationFirestore>> =
        firestore
            .collection("${FirebaseAuth.getInstance().currentUser?.uid}")
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(LocationFirestore::class.java)
            }

    private fun bitmapToByte(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        return data
    }

    private fun getRandomName(): String {
        return UUID.randomUUID().toString()
    }

    companion object {
        private const val DEFAULT_PATH_STORAGE = "media"
    }

}