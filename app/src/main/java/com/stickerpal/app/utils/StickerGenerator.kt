package com.stickerpal.app.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.stickerpal.app.data.model.HabitType
import com.stickerpal.app.data.model.StickerRarity
import java.io.ByteArrayOutputStream

object StickerGenerator {
    private val stickerTemplates = mapOf(
        HabitType.STUDY to listOf("\uD83D\uDCDA", "\u270F\uFE0F", "\uD83D\uDCD6", "\uD83C\uDF92", "\uD83D\uDCDD", "\uD83D\uDD8A\uFE0F"),
        HabitType.WATER to listOf("\uD83D\uDCA7", "\uD83E\uDD64", "\uD83C\uDF75", "\uD83E\uDDC3", "\uD83D\uDCA6", "\uD83E\uDDCA"),
        HabitType.MEAL to listOf("\uD83C\uDF4E", "\uD83C\uDF55", "\uD83C\uDF54", "\uD83C\uDF63", "\uD83E\uDD57", "\uD83C\uDF71"),
        HabitType.EXERCISE to listOf("\uD83C\uDFC3", "\uD83E\uDDD8", "\u26BD", "\uD83C\uDFCB\uFE0F", "\uD83D\uDEB4", "\uD83C\uDFCA"),
        HabitType.SLEEP to listOf("\uD83C\uDF19", "\uD83D\uDE34", "\uD83D\uDECF\uFE0F", "\uD83C\uDF1A", "\u2728", "\uD83C\uDF1B"),
        HabitType.TASK to listOf("\u2705", "\uD83D\uDCCB", "\uD83D\uDCCC", "\uD83C\uDFAF", "\uD83C\uDF96\uFE0F", "\uD83C\uDFC6"),
        HabitType.JOURNAL to listOf("\uD83D\uDCD4", "\u2712\uFE0F", "\uD83D\uDCAD", "\uD83D\uDC9D", "\uD83C\uDFA8", "\uD83C\uDF08"),
        HabitType.SCREENTIME to listOf("\uD83D\uDCF1", "\uD83D\uDCBB", "\uD83D\uDDA5\uFE0F", "\uD83C\uDFAE", "\uD83D\uDCFA", "\uD83C\uDFA7")
    )

    private val colors = listOf(
        0xFFFF6B6B.toInt(), 0xFF4ECDC4.toInt(), 0xFF45B7D1.toInt(), 0xFF96CEB4.toInt(),
        0xFFFFEAA7.toInt(), 0xFFDDA0DD.toInt(), 0xFF98D8C8.toInt(), 0xFFF7DC6F.toInt(),
        0xFFBB8FCE.toInt(), 0xFF85C1E9.toInt(), 0xFFF8B500.toInt(), 0xFFFF8C00.toInt()
    )

    fun generateSticker(type: HabitType): Pair<Bitmap, String> {
        val templateIndex = (stickerTemplates[type]?.indices ?: 0..5).random()
        val emoji = stickerTemplates[type]?.get(templateIndex) ?: "\u2B50"
        val bgColor = colors.random()
        val image = createStickerImage(emoji, bgColor)
        val name = generateStickerName(type, emoji)
        return Pair(image, name)
    }

    private fun createStickerImage(emoji: String, backgroundColor: Int): Bitmap {
        val size = 128
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundColor }
        val cornerRadius = (size * 0.2f)
        canvas.drawRoundRect(0f, 0f, size.toFloat(), size.toFloat(), cornerRadius, cornerRadius, paint)
        paint.color = Color.WHITE
        paint.alpha = 76
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRoundRect(0f, 0f, size.toFloat(), size.toFloat(), cornerRadius, cornerRadius, paint)
        paint.style = Paint.Style.FILL
        paint.alpha = 255
        paint.textSize = 56f
        paint.textAlign = Paint.Align.CENTER
        val textBounds = Rect()
        paint.getTextBounds(emoji, 0, emoji.length, textBounds)
        val textX = size / 2f
        val textY = (size - textBounds.height()) / 2f + textBounds.height()
        canvas.drawText(emoji, textX, textY, paint)
        return bitmap
    }

    private fun generateStickerName(type: HabitType, emoji: String): String {
        val adjectives = listOf("可爱的", "萌萌的", "闪亮的", "温暖的", "甜蜜的", "快乐的", "元气的", "梦幻的")
        return "${adjectives.random()}$emoji${type.displayName}"
    }

    fun determineRarity(streak: Int): StickerRarity = when {
        streak >= 30 -> StickerRarity.LEGENDARY
        streak >= 14 -> StickerRarity.RARE
        streak >= 7 -> StickerRarity.UNCOMMON
        else -> StickerRarity.COMMON
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
