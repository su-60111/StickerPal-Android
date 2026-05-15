package com.stickerpal.app.data.model

enum class PetMood(val displayName: String, val emoji: String) {
    HAPPY("开心", "\uD83D\uDE04"),
    NORMAL("一般", "\uD83D\uDE0A"),
    SAD("低落", "\uD83D\uDE22");

    companion object {
        fun fromString(value: String): PetMood =
            entries.first { it.name == value.uppercase() }
    }
}
