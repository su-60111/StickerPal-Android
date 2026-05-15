package com.stickerpal.app.utils

object Constants {
    const val APP_NAME = "贴贴伙伴"
}

object AppColors {
    const val PRIMARY = 0xFFFF6B6B
    const val SECONDARY = 0xFF4ECDC4
    const val BACKGROUND = 0xFFFFF8F0
    const val CARD_BACKGROUND = 0xFFFFFFFF
    const val TEXT_PRIMARY = 0xFF1A1A1A
    const val TEXT_SECONDARY = 0xFF6B7280

    fun habitTypeColor(type: String): Long = when (type.uppercase()) {
        "STUDY" -> 0xFF4ECDC4
        "WATER" -> 0xFF45B7D1
        "MEAL" -> 0xFFFF6B6B
        "EXERCISE" -> 0xFF96CEB4
        "SLEEP" -> 0xFFBB8FCE
        "TASK" -> 0xFFF7DC6F
        "JOURNAL" -> 0xFF85C1E9
        "SCREENTIME" -> 0xFFDDA0DD
        else -> 0xFF6B7280
    }
}
