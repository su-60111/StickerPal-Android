package com.stickerpal.app.ui.view.habits

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stickerpal.app.utils.AppColors
import com.stickerpal.app.viewmodel.HabitViewModel

private const val TIMER_MAX_SECONDS = 3600f

@Composable
fun TimerScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit
) {
    val timerSeconds by viewModel.timerSeconds.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()

    val timeText = viewModel.formatTime()
    val progress = (timerSeconds.toFloat() / TIMER_MAX_SECONDS).coerceIn(0f, 1f)
    val primaryColor = Color(AppColors.PRIMARY)
    val backgroundColor = Color(0xFFFFF8F0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.15f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val circleSize = 240.dp
            val strokeWidth = 12.dp

            Canvas(modifier = Modifier.size(circleSize)) {
                val canvasSize = size.minDimension
                val topLeft = Offset(
                    (size.width - canvasSize) / 2f,
                    (size.height - canvasSize) / 2f
                )
                val arcSize = Size(canvasSize, canvasSize)

                drawArc(
                    color = Color(0xFFE8E0D8),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )

                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = progress * 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeText,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isTimerRunning) "学习进行中..." else if (timerSeconds > 0) "已暂停" else "准备开始",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.3f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.stopTimer()
                    onBack()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primaryColor
                )
            ) {
                Text(
                    text = "停止",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = {
                    if (isTimerRunning) {
                        viewModel.pauseTimer()
                    } else {
                        viewModel.startTimer()
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTimerRunning) Color(0xFFF5A623) else primaryColor
                )
            ) {
                Text(
                    text = when {
                        isTimerRunning -> "暂停"
                        timerSeconds > 0 -> "继续"
                        else -> "开始学习"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
