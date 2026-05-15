package com.stickerpal.app.ui.view.stickers

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stickerpal.app.data.entity.Sticker
import com.stickerpal.app.data.model.HabitType
import com.stickerpal.app.data.model.StickerRarity
import com.stickerpal.app.utils.AppColors
import com.stickerpal.app.utils.DateHelper
import com.stickerpal.app.viewmodel.StickerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerDetailScreen(
    stickerId: String,
    viewModel: StickerViewModel,
    onBack: () -> Unit
) {
    var sticker by remember { mutableStateOf<Sticker?>(null) }
    val context = LocalContext.current

    LaunchedEffect(stickerId) {
        withContext(Dispatchers.IO) {
            val allStickers = viewModel.getAllStickers()
            sticker = allStickers.find { it.id == stickerId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = sticker?.name ?: "贴纸详情",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        sticker?.let { s ->
                            val shareText = buildShareText(s)
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "分享贴纸"))
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "分享"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        sticker?.let { s ->
            StickerDetailContent(
                sticker = s,
                modifier = Modifier.padding(padding)
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "贴纸不存在",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun StickerDetailContent(
    sticker: Sticker,
    modifier: Modifier = Modifier
) {
    val isObtained = sticker.isObtained
    val rarity = try {
        StickerRarity.fromString(sticker.rarity)
    } catch (_: Exception) {
        StickerRarity.COMMON
    }
    val categoryDisplayName = try {
        HabitType.fromString(sticker.category).displayName
    } catch (_: Exception) {
        sticker.category
    }
    val categoryColor = Color(AppColors.habitTypeColor(sticker.category).toInt())

    val bitmap = remember(sticker.imageData) {
        BitmapFactory.decodeByteArray(sticker.imageData, 0, sticker.imageData.size)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(categoryColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = sticker.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentScale = ContentScale.Fit
                )
            }

            if (!isObtained) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Black.copy(alpha = 0.45f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "???",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isObtained) sticker.name else "???",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                DetailInfoRow(
                    label = "分类",
                    value = categoryDisplayName,
                    valueColor = categoryColor
                )

                DetailDivider()

                DetailInfoRow(label = "稀有度") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color(rarity.color.toInt()))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = rarity.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(rarity.color.toInt())
                        )
                    }
                }

                DetailDivider()

                DetailInfoRow(
                    label = "获得状态",
                    value = if (isObtained) "已获得" else "未获得",
                    valueColor = if (isObtained) {
                        Color(AppColors.PRIMARY.toInt())
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    }
                )

                if (isObtained && sticker.obtainedDate != null) {
                    DetailDivider()
                    DetailInfoRow(
                        label = "获得时间",
                        value = DateHelper.formatDate(sticker.obtainedDate),
                        valueColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DetailInfoRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Composable
private fun DetailInfoRow(
    label: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        content()
    }
}

@Composable
private fun DetailDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}

private fun buildShareText(sticker: Sticker): String {
    val rarity = try {
        StickerRarity.fromString(sticker.rarity).displayName
    } catch (_: Exception) {
        sticker.rarity
    }
    val category = try {
        HabitType.fromString(sticker.category).displayName
    } catch (_: Exception) {
        sticker.category
    }
    val status = if (sticker.isObtained) "已获得" else "未获得"
    val dateInfo = if (sticker.isObtained && sticker.obtainedDate != null) {
        "\n获得时间：${DateHelper.formatDate(sticker.obtainedDate)}"
    } else ""

    return """
我在「贴贴伙伴」发现了一张贴纸！

名称：${sticker.name}
分类：$category
稀有度：$rarity
状态：$status$dateInfo

快来贴贴伙伴一起收集贴纸吧！
    """.trimIndent()
}
