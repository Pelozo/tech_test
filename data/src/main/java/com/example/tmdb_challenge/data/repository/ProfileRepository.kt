package com.example.tmdb_challenge.data.repository

import com.example.tmdb_challenge.domain.models.Profile

interface ProfileRepository {
    suspend fun getProfile(): Profile?
}