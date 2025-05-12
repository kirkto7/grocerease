package edu.msoe.grocerease.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Recipe(
    @PrimaryKey val id: UUID,
    val title: String,
    val imageResURL: String,
    val steps: String
)