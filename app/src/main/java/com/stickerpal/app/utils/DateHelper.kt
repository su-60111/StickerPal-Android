package com.stickerpal.app.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateHelper {
    private val calendar: Calendar get() = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("yyyy\u5E74M\u6708d\u65E5", Locale.CHINA)
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.CHINA)

    fun formatDate(date: Long): String = dateFormatter.format(Date(date))

    fun formatTime(date: Long): String = timeFormatter.format(Date(date))

    fun formatDuration(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return when {
            hours > 0 && mins > 0 -> "${hours}\u5C0F\u65F6${mins}\u5206\u949F"
            hours > 0 -> "${hours}\u5C0F\u65F6"
            else -> "${mins}\u5206\u949F"
        }
    }

    fun getGreeting(): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..8 -> "\u65E9\u4E0A\u597D \u2600\uFE0F"
            in 9..11 -> "\u4E0A\u5348\u597D \uD83C\uDF24\uFE0F"
            in 12..13 -> "\u4E2D\u5348\u597D \uD83C\uDF1E"
            in 14..17 -> "\u4E0B\u5348\u597D \uD83C\uDF25\uFE0F"
            in 18..21 -> "\u665A\u4E0A\u597D \uD83C\uDF19"
            else -> "\u591C\u6DF1\u4E86 \uD83D\uDCA4"
        }
    }

    fun getDayOfWeek(): String {
        val days = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    }

    fun isToday(date: Long): Boolean {
        val today = calendar.timeInMillis
        return date >= getStartOfDay(today) && date < getEndOfDay(today)
    }

    fun getStartOfDay(date: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = date }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getEndOfDay(date: Long): Long = getStartOfDay(date) + 24 * 60 * 60 * 1000
}
