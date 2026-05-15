package com.stickerpal.app.data.repository

import com.stickerpal.app.data.dao.StickerDao
import com.stickerpal.app.data.entity.Sticker
import com.stickerpal.app.data.model.StickerRarity

class StickerRepository(private val stickerDao: StickerDao) {
    suspend fun insertSticker(name: String, category: String, imageData: ByteArray, isCustom: Boolean, rarity: StickerRarity) {
        val sticker = Sticker(name = name, category = category, imageData = imageData, isCustom = isCustom, rarity = rarity.name)
        stickerDao.insert(sticker)
    }

    suspend fun obtainSticker(sticker: Sticker) {
        stickerDao.update(sticker.copy(obtainedDate = System.currentTimeMillis()))
    }

    suspend fun getAllStickers(): List<Sticker> = stickerDao.getAllStickers()
    suspend fun getObtainedStickers(): List<Sticker> = stickerDao.getObtainedStickers()

    suspend fun getCollectionStats(): Pair<Int, Int> {
        val obtained = stickerDao.getObtainedCount()
        val total = stickerDao.getStickerCount()
        return Pair(obtained, total)
    }
}
