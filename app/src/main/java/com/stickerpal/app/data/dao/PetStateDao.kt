package com.stickerpal.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.stickerpal.app.data.entity.PetState

@Dao
interface PetStateDao {
    @Insert
    suspend fun insert(petState: PetState)

    @Update
    suspend fun update(petState: PetState)

    @Query("SELECT * FROM pet_state WHERE id = :id")
    suspend fun getPetState(id: String): PetState?

    @Query("SELECT COUNT(*) FROM pet_state")
    suspend fun getPetStateCount(): Int
}
