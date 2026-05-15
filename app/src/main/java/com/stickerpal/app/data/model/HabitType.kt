package com.stickerpal.app.data.model

enum class HabitType(val displayName: String, val iconName: String, val defaultUnit: String, val defaultGoal: Double) {
    STUDY("学习", "book", "分钟", 120.0),
    WATER("饮水", "water_drop", "杯", 8.0),
    MEAL("三餐", "restaurant", "次", 1.0),
    EXERCISE("运动", "fitness_center", "分钟", 60.0),
    SLEEP("睡眠", "bedtime", "小时", 1.0),
    TASK("任务", "check_circle", "个", 1.0),
    JOURNAL("日记", "edit_note", "篇", 1.0),
    SCREENTIME("屏幕使用", "phone_android", "分钟", 120.0);

    companion object {
        fun fromString(value: String): HabitType =
            entries.first { it.name == value.uppercase() }
    }
}
