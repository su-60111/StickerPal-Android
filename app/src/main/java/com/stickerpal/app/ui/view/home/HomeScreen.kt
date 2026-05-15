package com.stickerpal.app.ui.view.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stickerpal.app.data.model.HabitType
import com.stickerpal.app.utils.DateHelper
import com.stickerpal.app.viewmodel.HabitViewModel
import com.stickerpal.app.viewmodel.HomeViewModel

private val ProgressRingColor = Color(0xFFFF6B6B)
private val ProgressTrackColor = Color(0xFFFFE0E0)
private val StreakColor = Color(0xFFFFA500)
private val PetCardStartColor = Color(0xFFA8E6CF)
private val PetCardEndColor = Color(0xFFDCEDC1)
private val QuickActionStudyColor = Color(0xFFFF6B6B)
private val QuickActionWaterColor = Color(0xFF45B7D1)
private val QuickActionMealColor = Color(0xFFFFB347)
private val QuickActionJournalColor = Color(0xFF85C1E9)

private fun habitIcon(type: String): ImageVector = when (type) {
    "STUDY" -> Icons.Default.MenuBook
    "WATER" -> Icons.Default.WaterDrop
    "MEAL" -> Icons.Default.Restaurant
    "JOURNAL" -> Icons.Default.EditNote
    else -> Icons.Default.KeyboardArrowRight
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    habitViewModel: HabitViewModel,
    onNavigateToPet: () -> Unit = {},
    onOpenTimer: () -> Unit = {}
) {
    val greeting by viewModel.greeting.collectAsState()
    val todayProgress by viewModel.todayProgress.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val petEmoji by viewModel.petEmoji.collectAsState()
    val petMoodName by viewModel.petMoodName.collectAsState()
    val petIntimacy by viewModel.petIntimacy.collectAsState()
    val petMessage by viewModel.petMessage.collectAsState()
    val todayRecords by habitViewModel.todayRecords.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshData()
        habitViewModel.loadTodayRecords()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            GreetingHeader(greeting = greeting)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ProgressRing(progress = todayProgress)
                }
                Box(modifier = Modifier.weight(1f)) {
                    StreakCard(streakDays = streakDays)
                }
            }
        }

        item {
            PetCard(
                emoji = petEmoji,
                moodName = petMoodName,
                intimacy = petIntimacy,
                message = petMessage,
                onInteract = { viewModel.interactWithPet() },
                onNavigateToPet = onNavigateToPet
            )
        }

        item {
            QuickActionsRow(
                onStudyClick = {
                    habitViewModel.setShowTimer(true)
                    onOpenTimer()
                },
                onWaterClick = { habitViewModel.addWater() },
                onMealClick = { habitViewModel.addMeal() },
                onJournalClick = { habitViewModel.addJournal() }
            )
        }

        item {
            Text(
                text = "今日记录",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (todayRecords.isEmpty()) {
            item {
                Text(
                    text = "今天还没有记录哦，快去完成一个习惯吧~",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(todayRecords, key = { it.id }) { record ->
                val habitType = try {
                    HabitType.fromString(record.type)
                } catch (_: Exception) {
                    null
                }
                TodayRecordItem(
                    icon = habitIcon(record.type),
                    name = habitType?.displayName ?: record.type,
                    value = record.value,
                    unit = record.unit,
                    time = DateHelper.formatTime(record.date),
                    color = com.stickerpal.app.utils.AppColors.habitTypeColor(record.type).let { Color(it.toInt()) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun GreetingHeader(greeting: String) {
    Text(
        text = greeting,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun ProgressRing(progress: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "今日进度",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 10.dp.toPx()
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                    val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                    drawArc(
                        color = ProgressTrackColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    drawArc(
                        color = ProgressRingColor,
                        startAngle = -90f,
                        sweepAngle = (360f * progress).toFloat(),
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ProgressRingColor
                )
            }
        }
    }
}

@Composable
private fun StreakCard(streakDays: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StreakColor.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "连续打卡",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = StreakColor,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$streakDays",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = StreakColor
                )
            }
            Text(
                text = "天",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PetCard(
    emoji: String,
    moodName: String,
    intimacy: Int,
    message: String,
    onInteract: () -> Unit,
    onNavigateToPet: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onInteract,
                onLongClick = onNavigateToPet
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PetCardStartColor, PetCardEndColor)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.6f))
                ) {
                    Text(text = emoji, fontSize = 36.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = moodName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D5A27)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "❤ $intimacy",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2D5A27).copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5A7D4A)
                    )
                }
                IconButton(onClick = onNavigateToPet) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "查看宠物",
                        tint = Color(0xFF5A7D4A)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsRow(
    onStudyClick: () -> Unit,
    onWaterClick: () -> Unit,
    onMealClick: () -> Unit,
    onJournalClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickActionButton(
            icon = Icons.Default.Timer,
            label = "开始学习",
            color = QuickActionStudyColor,
            onClick = onStudyClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = Icons.Default.WaterDrop,
            label = "喝一杯水",
            color = QuickActionWaterColor,
            onClick = onWaterClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = Icons.Default.Restaurant,
            label = "记录用餐",
            color = QuickActionMealColor,
            onClick = onMealClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = Icons.Default.EditNote,
            label = "写日记",
            color = QuickActionJournalColor,
            onClick = onJournalClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = color,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TodayRecordItem(
    icon: ImageVector,
    name: String,
    value: Double,
    unit: String,
    time: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Text(
                text = formatRecordValue(value, unit),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

private fun formatRecordValue(value: Double, unit: String): String {
    return if (value == value.toLong().toDouble()) {
        "${value.toLong()}$unit"
    } else {
        "${value}$unit"
    }
}
