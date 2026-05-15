package com.stickerpal.app.ui.view.stickers

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stickerpal.app.data.entity.Sticker
import com.stickerpal.app.data.model.HabitType
import com.stickerpal.app.data.model.StickerRarity
import com.stickerpal.app.utils.AppColors
import com.stickerpal.app.viewmodel.StickerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun StickerScreen(
    viewModel: StickerViewModel,
    onNavigateToDetail: (String) -> Unit = {}
) {
    val stickers = remember { mutableStateListOf<Sticker>() }
    val categories = remember { mutableStateListOf<String>() }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var obtainedCount by remember { mutableIntStateOf(0) }
    var totalCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val allStickers = viewModel.getAllStickers()
            stickers.clear()
            stickers.addAll(allStickers)

            val cats = mutableSetOf<String>()
            allStickers.forEach { cats.add(it.category) }
            categories.clear()
            categories.add("全部")
            categories.addAll(cats.sorted())

            val stats = viewModel.getCollectionStats()
            obtainedCount = stats.first
            totalCount = stats.second
        }
    }

    val displayCategories = listOf("全部") + stickers.map { it.category }.distinct().sorted()
    val filteredStickers = if (selectedCategoryIndex == 0) {
        stickers.toList()
    } else {
        val selectedCategory = displayCategories.getOrElse(selectedCategoryIndex) { "" }
        stickers.filter { it.category == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CategoryTabBar(
            categories = displayCategories,
            selectedIndex = selectedCategoryIndex,
            onCategorySelected = { selectedCategoryIndex = it }
        )

        CollectionProgressBar(
            obtainedCount = obtainedCount,
            totalCount = totalCount
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredStickers, key = { it.id }) { sticker ->
                StickerCard(
                    sticker = sticker,
                    onClick = { onNavigateToDetail(sticker.id) }
                )
            }
        }
    }
}

@Composable
private fun CategoryTabBar(
    categories: List<String>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEachIndexed { index, category ->
            val isSelected = index == selectedIndex
            val displayName = if (category == "全部") {
                "全部"
            } else {
                try {
                    HabitType.fromString(category).displayName
                } catch (_: Exception) {
                    category
                }
            }
            val categoryColor = if (category == "全部") {
                Color(AppColors.PRIMARY.toInt())
            } else {
                Color(AppColors.habitTypeColor(category).toInt())
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) categoryColor
                        else categoryColor.copy(alpha = 0.12f)
                    )
                    .clickable { onCategorySelected(index) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else categoryColor
                )
            }
        }
    }
}

@Composable
private fun CollectionProgressBar(obtainedCount: Int, totalCount: Int) {
    val progress = if (totalCount > 0) obtainedCount.toFloat() / totalCount.toFloat() else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(AppColors.PRIMARY.toInt()).copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "收集进度",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$obtainedCount / $totalCount",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(AppColors.PRIMARY.toInt())
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(AppColors.PRIMARY.toInt()),
                trackColor = Color(AppColors.PRIMARY.toInt()).copy(alpha = 0.15f)
            )
        }
    }
}

@Composable
private fun StickerCard(
    sticker: Sticker,
    onClick: () -> Unit
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

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bitmap = remember(sticker.imageData) {
                    BitmapFactory.decodeByteArray(sticker.imageData, 0, sticker.imageData.size)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(AppColors.habitTypeColor(sticker.category).toInt()).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = sticker.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = sticker.name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = categoryDisplayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(AppColors.habitTypeColor(sticker.category).toInt()),
                        fontWeight = FontWeight.Medium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(rarity.color.toInt()))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = rarity.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(rarity.color.toInt()),
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            if (!isObtained) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.45f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "???",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
