package com.stickerpal.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stickerpal.app.data.entity.DailyGoal

@Dao
interface DailyGoalDao {
    @Insert
    suspend fun insert(goal: DailyGoal)

    @Query("SELECT * FROM daily_goals WHERE habitType = :habitType")
    suspend fun getGoalByHabitType(habitType: String): DailyGoal?

    @Query("SELECT * FROM daily_goals")
    suspend fun getAllGoals(): List<DailyGoal>
}
