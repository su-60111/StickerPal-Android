package com.stickerpal.app.ui.view.pet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stickerpal.app.viewmodel.HomeViewModel
import com.stickerpal.app.viewmodel.PetViewModel
import kotlinx.coroutines.launch

@Composable
fun PetScreen(
    viewModel: PetViewModel,
    homeViewModel: HomeViewModel
) {
    val petIntimacy by homeViewModel.petIntimacy.collectAsState()
    val petEmoji by homeViewModel.petEmoji.collectAsState()
    val petMoodName by homeViewModel.petMoodName.collectAsState()
    val petMessage by homeViewModel.petMessage.collectAsState()

    var currentSkin by remember { mutableStateOf("cat") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFFF0E6))
                .clickable { homeViewModel.interactWithPet() },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawPetCharacter(currentSkin)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val intimacyLevel = (petIntimacy / 50) + 1
        val intimacyProgress = (petIntimacy % 50) / 50f

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "亲密度 Lv.$intimacyLevel",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(intimacyProgress)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$petIntimacy / ${intimacyLevel * 50}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = petEmoji,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = petMoodName,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = petMessage.ifEmpty { "来和我互动吧！" },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "选择皮肤",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SkinButton(
                label = "\uD83D\uDC31 小猫",
                isSelected = currentSkin == "cat",
                onClick = {
                    currentSkin = "cat"
                    scope.launch { viewModel.updateSkin("cat") }
                }
            )
            SkinButton(
                label = "\uD83D\uDC36 小狗",
                isSelected = currentSkin == "dog",
                onClick = {
                    currentSkin = "dog"
                    scope.launch { viewModel.updateSkin("dog") }
                }
            )
            SkinButton(
                label = "\uD83D\uDC30 小兔",
                isSelected = currentSkin == "rabbit",
                onClick = {
                    currentSkin = "rabbit"
                    scope.launch { viewModel.updateSkin("rabbit") }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SkinButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = label, fontSize = 14.sp)
    }
}

private fun DrawScope.drawPetCharacter(skin: String) {
    val centerX = size.width / 2
    val centerY = size.height / 2

    when (skin) {
        "cat" -> drawCat(centerX, centerY)
        "dog" -> drawDog(centerX, centerY)
        "rabbit" -> drawRabbit(centerX, centerY)
    }
}

private fun DrawScope.drawCat(cx: Float, cy: Float) {
    val bodyColor = Color(0xFFFFB347)
    val earInner = Color(0xFFFFDAB9)
    val eyeColor = Color(0xFF2D3436)
    val mouthColor = Color(0xFF2D3436)
    val whiskerColor = Color(0xFF636E72)

    val earOffsetX = 28f
    val earOffsetY = -52f
    val earSize = Size(22f, 30f)
    drawRoundRect(
        color = bodyColor,
        topLeft = Offset(cx - earOffsetX - earSize.width / 2, cy + earOffsetY - earSize.height / 2),
        size = earSize,
        cornerRadius = CornerRadius(8f, 8f)
    )
    drawRoundRect(
        color = earInner,
        topLeft = Offset(cx - earOffsetX - earSize.width / 2 + 4f, cy + earOffsetY - earSize.height / 2 + 4f),
        size = Size(14f, 22f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    drawRoundRect(
        color = bodyColor,
        topLeft = Offset(cx + earOffsetX - earSize.width / 2, cy + earOffsetY - earSize.height / 2),
        size = earSize,
        cornerRadius = CornerRadius(8f, 8f)
    )
    drawRoundRect(
        color = earInner,
        topLeft = Offset(cx + earOffsetX - earSize.width / 2 + 4f, cy + earOffsetY - earSize.height / 2 + 4f),
        size = Size(14f, 22f),
        cornerRadius = CornerRadius(6f, 6f)
    )

    val headRadius = 44f
    drawCircle(color = bodyColor, radius = headRadius, center = Offset(cx, cy - 6f))

    drawCircle(color = Color.White, radius = 10f, center = Offset(cx - 16f, cy - 16f))
    drawCircle(color = Color.White, radius = 10f, center = Offset(cx + 16f, cy - 16f))
    drawCircle(color = eyeColor, radius = 5f, center = Offset(cx - 16f, cy - 16f))
    drawCircle(color = eyeColor, radius = 5f, center = Offset(cx + 16f, cy - 16f))
    drawCircle(color = Color.White, radius = 2f, center = Offset(cx - 14f, cy - 18f))
    drawCircle(color = Color.White, radius = 2f, center = Offset(cx + 18f, cy - 18f))

    drawCircle(color = Color(0xFFFF8A80), radius = 5f, center = Offset(cx, cy + 4f))
    drawArc(
        color = mouthColor,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(cx - 8f, cy + 10f),
        size = Size(16f, 10f),
        style = Stroke(width = 2f)
    )

    drawLine(color = whiskerColor, start = Offset(cx - 36f, cy), end = Offset(cx - 56f, cy - 8f), strokeWidth = 1.5f)
    drawLine(color = whiskerColor, start = Offset(cx - 36f, cy + 6f), end = Offset(cx - 56f, cy + 10f), strokeWidth = 1.5f)
    drawLine(color = whiskerColor, start = Offset(cx + 36f, cy), end = Offset(cx + 56f, cy - 8f), strokeWidth = 1.5f)
    drawLine(color = whiskerColor, start = Offset(cx + 36f, cy + 6f), end = Offset(cx + 56f, cy + 10f), strokeWidth = 1.5f)
}

private fun DrawScope.drawDog(cx: Float, cy: Float) {
    val bodyColor = Color(0xFFD4A574)
    val earDark = Color(0xFF8B6914)
    val eyeColor = Color(0xFF2D3436)
    val noseColor = Color(0xFF2D3436)
    val tongueColor = Color(0xFFFF6B6B)

    val headRadius = 44f
    drawCircle(color = bodyColor, radius = headRadius, center = Offset(cx, cy - 4f))

    val earWidth = 18f
    val earHeight = 34f
    drawRoundRect(
        color = earDark,
        topLeft = Offset(cx - 36f, cy - 50f),
        size = Size(earWidth, earHeight),
        cornerRadius = CornerRadius(12f, 12f)
    )
    drawRoundRect(
        color = earDark,
        topLeft = Offset(cx + 18f, cy - 50f),
        size = Size(earWidth, earHeight),
        cornerRadius = CornerRadius(12f, 12f)
    )

    val snoutColor = Color(0xFFFFF5E6)
    drawRoundRect(
        color = snoutColor,
        topLeft = Offset(cx - 16f, cy + 2f),
        size = Size(32f, 20f),
        cornerRadius = CornerRadius(10f, 10f)
    )

    drawCircle(color = Color.White, radius = 10f, center = Offset(cx - 14f, cy - 14f))
    drawCircle(color = Color.White, radius = 10f, center = Offset(cx + 14f, cy - 14f))
    drawCircle(color = eyeColor, radius = 5f, center = Offset(cx - 14f, cy - 14f))
    drawCircle(color = eyeColor, radius = 5f, center = Offset(cx + 14f, cy - 14f))
    drawCircle(color = Color.White, radius = 2f, center = Offset(cx - 12f, cy - 16f))
    drawCircle(color = Color.White, radius = 2f, center = Offset(cx + 16f, cy - 16f))

    drawRoundRect(
        color = noseColor,
        topLeft = Offset(cx - 5f, cy - 2f),
        size = Size(10f, 8f),
        cornerRadius = CornerRadius(4f, 4f)
    )

    drawRoundRect(
        color = tongueColor,
        topLeft = Offset(cx - 4f, cy + 10f),
        size = Size(8f, 12f),
        cornerRadius = CornerRadius(4f, 4f)
    )
}

private fun DrawScope.drawRabbit(cx: Float, cy: Float) {
    val bodyColor = Color(0xFFF5F5F5)
    val innerEar = Color(0xFFFFB6C1)
    val eyeColor = Color(0xFF2D3436)
    val noseColor = Color(0xFFFF8A80)

    val earWidth = 16f
    val earHeight = 48f
    drawRoundRect(
        color = bodyColor,
        topLeft = Offset(cx - 20f, cy - 72f),
        size = Size(earWidth, earHeight),
        cornerRadius = CornerRadius(8f, 8f)
    )
    drawRoundRect(
        color = innerEar,
        topLeft = Offset(cx - 18f, cy - 64f),
        size = Size(12f, 34f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    drawRoundRect(
        color = bodyColor,
        topLeft = Offset(cx + 4f, cy - 72f),
        size = Size(earWidth, earHeight),
        cornerRadius = CornerRadius(8f, 8f)
    )
    drawRoundRect(
        color = innerEar,
        topLeft = Offset(cx + 6f, cy - 64f),
        size = Size(12f, 34f),
        cornerRadius = CornerRadius(6f, 6f)
    )

    val headRadius = 40f
    drawCircle(color = bodyColor, radius = headRadius, center = Offset(cx, cy - 2f))

    drawCircle(color = Color.White, radius = 10f, center = Offset(cx - 14f, cy - 12f))
    drawCircle(color = Color.White, radius = 10f, center = Offset(cx + 14f, cy - 12f))
    drawCircle(color = eyeColor, radius = 5f, center = Offset(cx - 14f, cy - 12f))
    drawCircle(color = eyeColor, radius = 5f, center = Offset(cx + 14f, cy - 12f))
    drawCircle(color = Color.White, radius = 2f, center = Offset(cx - 12f, cy - 14f))
    drawCircle(color = Color.White, radius = 2f, center = Offset(cx + 16f, cy - 14f))

    drawRoundRect(
        color = noseColor,
        topLeft = Offset(cx - 4f, cy - 2f),
        size = Size(8f, 6f),
        cornerRadius = CornerRadius(3f, 3f)
    )

    drawLine(
        color = Color(0xFF9E9E9E),
        start = Offset(cx, cy + 4f),
        end = Offset(cx - 6f, cy + 12f),
        strokeWidth = 1.5f
    )
    drawLine(
        color = Color(0xFF9E9E9E),
        start = Offset(cx, cy + 4f),
        end = Offset(cx + 6f, cy + 12f),
        strokeWidth = 1.5f
    )

    drawCircle(color = Color(0xFFFFB6C1), radius = 5f, center = Offset(cx - 12f, cy + 16f))
    drawCircle(color = Color(0xFFFFB6C1), radius = 5f, center = Offset(cx + 12f, cy + 16f))
}
