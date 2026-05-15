package com.stickerpal.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stickers")
data class Sticker(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val category: String,
    val imageData: ByteArray,
    val isCustom: Boolean = false,
    val obtainedDate: Long? = null,
    val rarity: String
) {
    val isObtained: Boolean get() = obtainedDate != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Sticker
        return id == other.id && imageData.contentEquals(other.imageData)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}
