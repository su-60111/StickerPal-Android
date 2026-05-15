package com.stickerpal.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.stickerpal.app.data.entity.Sticker

@Dao
interface StickerDao {
    @Insert
    suspend fun insert(sticker: Sticker)

    @Update
    suspend fun update(sticker: Sticker)

    @Query("SELECT * FROM stickers ORDER BY obtainedDate DESC")
    suspend fun getAllStickers(): List<Sticker>

    @Query("SELECT * FROM stickers WHERE category = :category ORDER BY obtainedDate DESC")
    suspend fun getStickersByCategory(category: String): List<Sticker>

    @Query("SELECT * FROM stickers WHERE obtainedDate IS NOT NULL ORDER BY obtainedDate DESC")
    suspend fun getObtainedStickers(): List<Sticker>

    @Query("SELECT COUNT(*) FROM stickers")
    suspend fun getStickerCount(): Int

    @Query("SELECT COUNT(*) FROM stickers WHERE obtainedDate IS NOT NULL")
    suspend fun getObtainedCount(): Int
}
