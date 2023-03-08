package com.example.tmdb_challenge.data.repository

import com.example.tmdb_challenge.data.datasource.ProfileDataSource
import com.example.tmdb_challenge.data.util.Result
import com.example.tmdb_challenge.domain.models.Profile
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource
): ProfileRepository {

    override suspend fun getProfile(): Profile? {
        return when (val profile = withTimeout(5000) {
            profileDataSource.getProfile()
        }) {
            is Result.Success -> {
                profile.data
            }
            is Result.Error -> {
                null
            }
        }
    }
}