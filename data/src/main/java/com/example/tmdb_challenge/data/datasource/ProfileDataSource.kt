package com.example.tmdb_challenge.data.datasource

import com.example.tmdb_challenge.data.util.Result
import com.example.tmdb_challenge.domain.models.Profile
import com.example.tmdb_challenge.domain.models.responses.ProfileResponse

interface ProfileDataSource {
    suspend fun getProfile(): Result<Profile>
}