package com.stickerpal.app

import com.stickerpal.app.data.model.HabitType
import com.stickerpal.app.data.model.PetMood
import com.stickerpal.app.data.model.StickerRarity
import com.stickerpal.app.utils.DateHelper
import com.stickerpal.app.utils.StickerGenerator
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class StickerPalUnitTest {

    @Test
    fun testHabitTypeDisplayName() {
        assertEquals("学习", HabitType.STUDY.displayName)
        assertEquals("饮水", HabitType.WATER.displayName)
        assertEquals("三餐", HabitType.MEAL.displayName)
        assertEquals("运动", HabitType.EXERCISE.displayName)
        assertEquals("睡眠", HabitType.SLEEP.displayName)
        assertEquals("任务", HabitType.TASK.displayName)
        assertEquals("日记", HabitType.JOURNAL.displayName)
        assertEquals("屏幕使用", HabitType.SCREENTIME.displayName)
    }

    @Test
    fun testStickerRarityDisplayName() {
        assertEquals("普通", StickerRarity.COMMON.displayName)
        assertEquals("稀有", StickerRarity.UNCOMMON.displayName)
        assertEquals("珍贵", StickerRarity.RARE.displayName)
        assertEquals("传说", StickerRarity.LEGENDARY.displayName)
    }

    @Test
    fun testPetMoodDisplayName() {
        assertEquals("开心", PetMood.HAPPY.displayName)
        assertEquals("一般", PetMood.NORMAL.displayName)
        assertEquals("低落", PetMood.SAD.displayName)
    }

    @Test
    fun testPetMoodEmoji() {
        assertEquals("\uD83D\uDE04", PetMood.HAPPY.emoji)
        assertEquals("\uD83D\uDE0A", PetMood.NORMAL.emoji)
        assertEquals("\uD83D\uDE22", PetMood.SAD.emoji)
    }

    @Test
    fun testDetermineRarity() {
        assertEquals(StickerRarity.COMMON, StickerGenerator.determineRarity(0))
        assertEquals(StickerRarity.COMMON, StickerGenerator.determineRarity(5))
        assertEquals(StickerRarity.COMMON, StickerGenerator.determineRarity(6))
        assertEquals(StickerRarity.UNCOMMON, StickerGenerator.determineRarity(7))
        assertEquals(StickerRarity.UNCOMMON, StickerGenerator.determineRarity(10))
        assertEquals(StickerRarity.UNCOMMON, StickerGenerator.determineRarity(13))
        assertEquals(StickerRarity.RARE, StickerGenerator.determineRarity(14))
        assertEquals(StickerRarity.RARE, StickerGenerator.determineRarity(20))
        assertEquals(StickerRarity.RARE, StickerGenerator.determineRarity(29))
        assertEquals(StickerRarity.LEGENDARY, StickerGenerator.determineRarity(30))
        assertEquals(StickerRarity.LEGENDARY, StickerGenerator.determineRarity(50))
    }

    @Test
    fun testFormatDuration() {
        assertEquals("0分钟", DateHelper.formatDuration(0))
        assertEquals("30分钟", DateHelper.formatDuration(30))
        assertEquals("1小时", DateHelper.formatDuration(60))
        assertEquals("1小时30分钟", DateHelper.formatDuration(90))
        assertEquals("2小时", DateHelper.formatDuration(120))
        assertEquals("2小时30分钟", DateHelper.formatDuration(150))
    }

    @Test
    fun testIsToday() {
        val now = System.currentTimeMillis()
        val yesterday = now - 24 * 60 * 60 * 1000
        assertTrue(DateHelper.isToday(now))
        assertFalse(DateHelper.isToday(yesterday))
    }

    @Test
    fun testGetStartOfDay() {
        val now = System.currentTimeMillis()
        val start = DateHelper.getStartOfDay(now)
        val cal = Calendar.getInstance().apply { timeInMillis = start }
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, cal.get(Calendar.MINUTE))
        assertEquals(0, cal.get(Calendar.SECOND))
    }

    @Test
    fun testGetEndOfDay() {
        val now = System.currentTimeMillis()
        val start = DateHelper.getStartOfDay(now)
        val end = DateHelper.getEndOfDay(now)
        assertEquals(start + 24 * 60 * 60 * 1000, end)
    }

    @Test
    fun testGetGreeting() {
        val greeting = DateHelper.getGreeting()
        assertTrue(greeting.isNotEmpty())
        assertTrue(greeting.contains("好") || greeting.contains("夜深了"))
    }

    @Test
    fun testHabitTypeDefaultGoals() {
        assertEquals(120.0, HabitType.STUDY.defaultGoal, 0.01)
        assertEquals(8.0, HabitType.WATER.defaultGoal, 0.01)
        assertEquals(1.0, HabitType.MEAL.defaultGoal, 0.01)
        assertEquals(60.0, HabitType.EXERCISE.defaultGoal, 0.01)
        assertEquals(1.0, HabitType.SLEEP.defaultGoal, 0.01)
        assertEquals(1.0, HabitType.TASK.defaultGoal, 0.01)
        assertEquals(1.0, HabitType.JOURNAL.defaultGoal, 0.01)
        assertEquals(120.0, HabitType.SCREENTIME.defaultGoal, 0.01)
    }

    @Test
    fun testHabitTypeFromString() {
        assertEquals(HabitType.STUDY, HabitType.fromString("STUDY"))
        assertEquals(HabitType.WATER, HabitType.fromString("water"))
        assertEquals(HabitType.MEAL, HabitType.fromString("Meal"))
    }

    @Test
    fun testStickerRarityFromString() {
        assertEquals(StickerRarity.COMMON, StickerRarity.fromString("COMMON"))
        assertEquals(StickerRarity.UNCOMMON, StickerRarity.fromString("uncommon"))
        assertEquals(StickerRarity.RARE, StickerRarity.fromString("Rare"))
        assertEquals(StickerRarity.LEGENDARY, StickerRarity.fromString("legendary"))
    }

    @Test
    fun testPetMoodFromString() {
        assertEquals(PetMood.HAPPY, PetMood.fromString("HAPPY"))
        assertEquals(PetMood.NORMAL, PetMood.fromString("normal"))
        assertEquals(PetMood.SAD, PetMood.fromString("Sad"))
    }

    @Test
    fun testStickerRarityColors() {
        assertEquals(0xFF9CA3AF, StickerRarity.COMMON.color)
        assertEquals(0xFF10B981, StickerRarity.UNCOMMON.color)
        assertEquals(0xFF3B82F6, StickerRarity.RARE.color)
        assertEquals(0xFFF59E0B, StickerRarity.LEGENDARY.color)
    }
}
