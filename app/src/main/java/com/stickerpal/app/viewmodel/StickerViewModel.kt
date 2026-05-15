package com.stickerpal.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.stickerpal.app.data.database.AppDatabase
import com.stickerpal.app.data.repository.StickerRepository
import com.stickerpal.app.data.entity.Sticker

class StickerViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val stickerRepository = StickerRepository(database.stickerDao())

    suspend fun getAllStickers(): List<Sticker> = stickerRepository.getAllStickers()
    suspend fun getObtainedStickers(): List<Sticker> = stickerRepository.getObtainedStickers()
    suspend fun getCollectionStats(): Pair<Int, Int> = stickerRepository.getCollectionStats()
}
