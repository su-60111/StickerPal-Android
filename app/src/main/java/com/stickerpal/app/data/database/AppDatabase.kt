package com.stickerpal.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stickerpal.app.data.dao.DailyGoalDao
import com.stickerpal.app.data.dao.HabitRecordDao
import com.stickerpal.app.data.dao.PetStateDao
import com.stickerpal.app.data.dao.StickerDao
import com.stickerpal.app.data.entity.DailyGoal
import com.stickerpal.app.data.entity.HabitRecord
import com.stickerpal.app.data.entity.PetState
import com.stickerpal.app.data.entity.Sticker

@Database(
    entities = [HabitRecord::class, Sticker::class, PetState::class, DailyGoal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitRecordDao(): HabitRecordDao
    abstract fun stickerDao(): StickerDao
    abstract fun petStateDao(): PetStateDao
    abstract fun dailyGoalDao(): DailyGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stickerpal-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
