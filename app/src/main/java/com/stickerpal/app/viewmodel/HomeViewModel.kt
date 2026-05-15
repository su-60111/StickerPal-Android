package com.stickerpal.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.stickerpal.app.data.database.AppDatabase
import com.stickerpal.app.data.model.HabitType
import com.stickerpal.app.data.repository.HabitRepository
import com.stickerpal.app.data.repository.PetRepository
import com.stickerpal.app.data.repository.StickerRepository
import com.stickerpal.app.utils.DateHelper
import com.stickerpal.app.utils.StickerGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val habitRepository = HabitRepository(database.habitRecordDao(), database.dailyGoalDao())
    private val stickerRepository = StickerRepository(database.stickerDao())
    private val petRepository = PetRepository(database.petStateDao())

    private val _todayProgress = MutableStateFlow(0.0)
    val todayProgress: StateFlow<Double> = _todayProgress.asStateFlow()

    private val _streakDays = MutableStateFlow(0)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _greeting = MutableStateFlow("")
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    private val _petEmoji = MutableStateFlow("\uD83D\uDE0A")
    val petEmoji: StateFlow<String> = _petEmoji.asStateFlow()

    private val _petMoodName = MutableStateFlow("一般")
    val petMoodName: StateFlow<String> = _petMoodName.asStateFlow()

    private val _petIntimacy = MutableStateFlow(0)
    val petIntimacy: StateFlow<Int> = _petIntimacy.asStateFlow()

    private val _petMessage = MutableStateFlow("")
    val petMessage: StateFlow<String> = _petMessage.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        _greeting.value = DateHelper.getGreeting()
        kotlinx.coroutines.MainScope().launch {
            loadProgress()
            loadStreak()
            loadPetState()
        }
    }

    private suspend fun loadProgress() {
        val startOfDay = DateHelper.getStartOfDay(System.currentTimeMillis())
        val endOfDay = DateHelper.getEndOfDay(System.currentTimeMillis())
        var total = 0.0
        for (type in HabitType.entries) {
            val progress = habitRepository.getTodayProgress(type, startOfDay, endOfDay)
            val goal = habitRepository.getOrCreateDailyGoal(type)
            total += (progress / goal.targetValue).coerceAtMost(1.0) / HabitType.entries.size
        }
        _todayProgress.value = total
    }

    private suspend fun loadStreak() {
        var streak = 0
        var currentDate = System.currentTimeMillis()
        while (true) {
            val startOfDay = DateHelper.getStartOfDay(currentDate)
            val endOfDay = DateHelper.getEndOfDay(currentDate)
            val records = habitRepository.getTodayRecords(startOfDay, endOfDay)
            if (records.isEmpty()) break
            streak++
            val previousDate = currentDate - 24 * 60 * 60 * 1000
            if (previousDate < 0) break
            currentDate = previousDate
        }
        _streakDays.value = streak
    }

    private suspend fun loadPetState() {
        val state = petRepository.getPetState()
        val mood = try { com.stickerpal.app.data.model.PetMood.fromString(state.mood) } catch (_: Exception) { com.stickerpal.app.data.model.PetMood.NORMAL }
        _petEmoji.value = mood.emoji
        _petMoodName.value = mood.displayName
        _petIntimacy.value = state.intimacyLevel
        _petMessage.value = when (mood) {
            com.stickerpal.app.data.model.PetMood.HAPPY -> listOf("太棒了！继续加油！", "今天表现超棒！", "我为你感到骄傲！").random()
            com.stickerpal.app.data.model.PetMood.NORMAL -> listOf("今天也要好好努力哦！", "加油，你可以的！", "一起坚持下去吧！").random()
            com.stickerpal.app.data.model.PetMood.SAD -> listOf("今天也要打起精神呀~", "别灰心，明天会更好！", "我相信你！").random()
        }
    }

    fun addHabitRecord(type: HabitType, value: Double = 1.0, unit: String? = null, note: String? = null) {
        kotlinx.coroutines.MainScope().launch {
            val u = unit ?: type.defaultUnit
            habitRepository.insertHabitRecord(type, value, u, note)
            val (image, name) = StickerGenerator.generateSticker(type)
            val rarity = StickerGenerator.determineRarity(_streakDays.value)
            val imageData = StickerGenerator.bitmapToByteArray(image)
            stickerRepository.insertSticker(name, type.name, imageData, false, rarity)
            petRepository.incrementIntimacy()
            refreshData()
        }
    }

    fun interactWithPet() {
        kotlinx.coroutines.MainScope().launch {
            petRepository.incrementIntimacy()
            _petMessage.value = listOf("摸摸头~", "你好呀！", "今天过得开心吗？", "一起加油吧！", "谢谢你陪我~", "我好喜欢你！").random()
            loadPetState()
        }
    }
}
