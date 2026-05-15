package com.stickerpal.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_state")
data class PetState(
    @PrimaryKey val id: String = "default",
    val mood: String,
    val energy: Int = 100,
    val intimacyLevel: Int = 0,
    val currentSkin: String = "cat",
    val lastInteraction: Long = System.currentTimeMillis()
)
