package com.stickerpal.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_goals")
data class DailyGoal(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val habitType: String,
    val targetValue: Double,
    val unit: String,
    val createdAt: Long = System.currentTimeMillis()
)
