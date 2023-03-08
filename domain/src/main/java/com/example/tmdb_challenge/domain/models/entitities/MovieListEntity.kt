package com.example.tmdb_challenge.domain.models.entitities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieListEntity(
    @PrimaryKey
    val listId: Long,
    val listName: String
)