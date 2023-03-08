package com.example.tmdb_challenge.data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


sealed class Result<out T : Any?> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

suspend inline fun <T> safeApiCall(
    crossinline body: suspend () -> T
): Result<T> {
    return try {
        // blocking block
        val users = withContext(Dispatchers.IO) {
            body()
        }
        Result.Success(users)
    } catch (e: Exception) {
        Result.Error(e)
    }
}