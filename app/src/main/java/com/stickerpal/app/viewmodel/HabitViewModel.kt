package com.stickerpal.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.stickerpal.app.data.database.AppDatabase
import com.stickerpal.app.data.entity.HabitRecord
import com.stickerpal.app.data.model.HabitType
import com.stickerpal.app.data.repository.HabitRepository
import com.stickerpal.app.data.repository.StickerRepository
import com.stickerpal.app.utils.DateHelper
import com.stickerpal.app.utils.StickerGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val habitRepository = HabitRepository(database.habitRecordDao(), database.dailyGoalDao())
    private val stickerRepository = StickerRepository(database.stickerDao())

    private val _timerSeconds = MutableStateFlow(0)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _showTimer = MutableStateFlow(false)
    val showTimer: StateFlow<Boolean> = _showTimer.asStateFlow()

    private val _todayRecords = MutableStateFlow<List<HabitRecord>>(emptyList())
    val todayRecords: StateFlow<List<HabitRecord>> = _todayRecords.asStateFlow()

    private var timer: Timer? = null

    fun loadTodayRecords() {
        MainScope().launch {
            val startOfDay = DateHelper.getStartOfDay(System.currentTimeMillis())
            val endOfDay = DateHelper.getEndOfDay(System.currentTimeMillis())
            _todayRecords.value = habitRepository.getTodayRecords(startOfDay, endOfDay)
        }
    }

    fun startTimer() {
        _isTimerRunning.value = true
        timer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    _timerSeconds.value = _timerSeconds.value + 1
                }
            }, 0, 1000)
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timer?.cancel()
        timer = null
    }

    fun stopTimer() {
        _isTimerRunning.value = false
        timer?.cancel()
        timer = null
        val minutes = _timerSeconds.value / 60
        if (minutes > 0) addHabitRecord(HabitType.STUDY, minutes.toDouble(), "分钟")
        _timerSeconds.value = 0
        _showTimer.value = false
    }

    fun addWater() = addHabitRecord(HabitType.WATER)
    fun addMeal() = addHabitRecord(HabitType.MEAL)
    fun addJournal() = addHabitRecord(HabitType.JOURNAL)
    fun addTask() = addHabitRecord(HabitType.TASK)

    private fun addHabitRecord(type: HabitType, value: Double = 1.0, unit: String? = null) {
        MainScope().launch {
            val u = unit ?: type.defaultUnit
            habitRepository.insertHabitRecord(type, value, u)
            val (image, name) = StickerGenerator.generateSticker(type)
            val streak = calculateStreak()
            val rarity = StickerGenerator.determineRarity(streak)
            val imageData = StickerGenerator.bitmapToByteArray(image)
            stickerRepository.insertSticker(name, type.name, imageData, false, rarity)
            loadTodayRecords()
        }
    }

    private suspend fun calculateStreak(): Int {
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
        return streak
    }

    fun formatTime(): String {
        val seconds = _timerSeconds.value
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, secs)
        else String.format("%02d:%02d", minutes, secs)
    }

    fun setShowTimer(value: Boolean) { _showTimer.value = value }
}
