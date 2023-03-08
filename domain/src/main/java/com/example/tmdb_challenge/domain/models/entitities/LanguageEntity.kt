package com.example.tmdb_challenge.domain.models.entitities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
data class LanguageEntity (
    @PrimaryKey
    val iso: String,
    val englishName: String,
    val name: String,
)
