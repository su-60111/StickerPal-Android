package com.stickerpal.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stickerpal.app.data.entity.HabitRecord

@Dao
interface HabitRecordDao {
    @Insert
    suspend fun insert(record: HabitRecord)

    @Query("SELECT * FROM habit_records ORDER BY date DESC")
    suspend fun getAllRecords(): List<HabitRecord>

    @Query("SELECT * FROM habit_records WHERE type = :type ORDER BY date DESC")
    suspend fun getRecordsByType(type: String): List<HabitRecord>

    @Query("SELECT * FROM habit_records WHERE date >= :startOfDay AND date < :endOfDay ORDER BY date DESC")
    suspend fun getTodayRecords(startOfDay: Long, endOfDay: Long): List<HabitRecord>

    @Query("SELECT COALESCE(SUM(value), 0) FROM habit_records WHERE type = :type AND date >= :startOfDay AND date < :endOfDay")
    suspend fun getTodayProgress(type: String, startOfDay: Long, endOfDay: Long): Double

    @Query("SELECT COUNT(*) FROM habit_records WHERE date >= :startOfDay AND date < :endOfDay")
    suspend fun getTodayRecordCount(startOfDay: Long, endOfDay: Long): Int
}
