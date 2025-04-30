package edu.msoe.grocerease.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Ingredient (
    @PrimaryKey val id: UUID,
    val name: String,
    val amount: Double,
    val unit: String,
    val displayed: Boolean
)