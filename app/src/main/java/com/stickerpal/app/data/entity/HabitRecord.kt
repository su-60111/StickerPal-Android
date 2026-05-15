package com.stickerpal.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "habit_records")
data class HabitRecord(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val type: String,
    val date: Long = Date().time,
    val value: Double,
    val unit: String,
    val note: String? = null,
    val stickerId: String? = null
)
