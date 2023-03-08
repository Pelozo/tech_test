package com.example.tmdb_challenge.data.datasource

import com.example.tmdb_challenge.api.ProfileService
import com.example.tmdb_challenge.data.mappers.toModel
import com.example.tmdb_challenge.data.util.Result
import com.example.tmdb_challenge.data.util.safeApiCall
import com.example.tmdb_challenge.domain.models.Profile
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val profileService: ProfileService
) : ProfileDataSource {

    override suspend fun getProfile(): Result<Profile> {
        return safeApiCall {
            profileService.getProfile().toModel()
        }
    }
}