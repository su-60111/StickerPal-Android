package com.stickerpal.app.ui.view.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items as listItems
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stickerpal.app.data.entity.HabitRecord
import com.stickerpal.app.data.model.HabitType
import com.stickerpal.app.utils.AppColors
import com.stickerpal.app.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private data class HabitEntry(
    val type: HabitType,
    val icon: ImageVector
)

private val habitEntries = listOf(
    HabitEntry(HabitType.STUDY, Icons.Default.Book),
    HabitEntry(HabitType.WATER, Icons.Default.WaterDrop),
    HabitEntry(HabitType.MEAL, Icons.Default.Restaurant),
    HabitEntry(HabitType.EXERCISE, Icons.Default.FitnessCenter),
    HabitEntry(HabitType.SLEEP, Icons.Default.Bedtime),
    HabitEntry(HabitType.TASK, Icons.Default.CheckCircle),
    HabitEntry(HabitType.JOURNAL, Icons.Default.EditNote),
    HabitEntry(HabitType.SCREENTIME, Icons.Default.PhoneAndroid),
)

@Composable
fun HabitScreen(
    viewModel: HabitViewModel,
    onStartTimer: () -> Unit
) {
    val todayRecords by viewModel.todayRecords.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTodayRecords()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "今日习惯",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                userScrollEnabled = false
            ) {
                gridItems(habitEntries, key = { it.type.name }) { entry ->
                    HabitCard(
                        entry = entry,
                        onClick = {
                            when (entry.type) {
                                HabitType.STUDY -> onStartTimer()
                                HabitType.WATER -> viewModel.addWater()
                                HabitType.MEAL -> viewModel.addMeal()
                                HabitType.JOURNAL -> viewModel.addJournal()
                                HabitType.TASK -> viewModel.addTask()
                                else -> {}
                            }
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "今日记录",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        if (todayRecords.isEmpty()) {
            item {
                Text(
                    text = "今天还没有记录，快来完成一个习惯吧 ✨",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                )
            }
        } else {
            listItems(todayRecords, key = { it.id }) { record ->
                TodayRecordItem(record = record)
            }
        }
    }
}

@Composable
private fun HabitCard(
    entry: HabitEntry,
    onClick: () -> Unit
) {
    val accentColor = Color(AppColors.habitTypeColor(entry.type.name))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = entry.icon,
                    contentDescription = entry.type.displayName,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.type.displayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(accentColor)
            )
        }
    }
}

@Composable
private fun TodayRecordItem(record: HabitRecord) {
    val habitType = try {
        HabitType.fromString(record.type)
    } catch (_: Exception) {
        null
    }

    val accentColor = if (habitType != null) {
        Color(AppColors.habitTypeColor(habitType.name))
    } else {
        Color(AppColors.habitTypeColor(""))
    }

    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeText = dateFormat.format(java.util.Date(record.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = habitEntries.find { it.type == habitType }?.icon
                        ?: Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habitType?.displayName ?: record.type,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${record.value.toInt()} ${record.unit}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Text(
                text = timeText,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}
