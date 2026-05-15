package com.stickerpal.app.data.model

enum class StickerRarity(val displayName: String, val color: Long) {
    COMMON("普通", 0xFF9CA3AF),
    UNCOMMON("稀有", 0xFF10B981),
    RARE("珍贵", 0xFF3B82F6),
    LEGENDARY("传说", 0xFFF59E0B);

    companion object {
        fun fromString(value: String): StickerRarity =
            entries.first { it.name == value.uppercase() }
    }
}
