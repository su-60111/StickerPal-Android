package com.stickerpal.app.data.repository

import com.stickerpal.app.data.dao.DailyGoalDao
import com.stickerpal.app.data.dao.HabitRecordDao
import com.stickerpal.app.data.entity.DailyGoal
import com.stickerpal.app.data.entity.HabitRecord
import com.stickerpal.app.data.model.HabitType

class HabitRepository(
    private val habitRecordDao: HabitRecordDao,
    private val dailyGoalDao: DailyGoalDao
) {
    suspend fun insertHabitRecord(type: HabitType, value: Double, unit: String, note: String? = null) {
        val record = HabitRecord(type = type.name, value = value, unit = unit, note = note)
        habitRecordDao.insert(record)
    }

    suspend fun getTodayRecords(startOfDay: Long, endOfDay: Long): List<HabitRecord> =
        habitRecordDao.getTodayRecords(startOfDay, endOfDay)

    suspend fun getTodayProgress(type: HabitType, startOfDay: Long, endOfDay: Long): Double =
        habitRecordDao.getTodayProgress(type.name, startOfDay, endOfDay)

    suspend fun getOrCreateDailyGoal(type: HabitType): DailyGoal {
        val existing = dailyGoalDao.getGoalByHabitType(type.name)
        if (existing != null) return existing
        val newGoal = DailyGoal(habitType = type.name, targetValue = type.defaultGoal, unit = type.defaultUnit)
        dailyGoalDao.insert(newGoal)
        return newGoal
    }

    suspend fun getAllDailyGoals(): List<DailyGoal> = dailyGoalDao.getAllGoals()
}
