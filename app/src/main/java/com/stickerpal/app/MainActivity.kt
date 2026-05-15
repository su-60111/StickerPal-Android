package com.stickerpal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.stickerpal.app.ui.theme.StickerPalTheme
import com.stickerpal.app.ui.view.MainView
import com.stickerpal.app.viewmodel.HabitViewModel
import com.stickerpal.app.viewmodel.HomeViewModel
import com.stickerpal.app.viewmodel.PetViewModel
import com.stickerpal.app.viewmodel.StickerViewModel

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val habitViewModel: HabitViewModel by viewModels()
    private val stickerViewModel: StickerViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StickerPalTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainView(
                        homeViewModel = homeViewModel,
                        habitViewModel = habitViewModel,
                        stickerViewModel = stickerViewModel,
                        petViewModel = petViewModel
                    )
                }
            }
        }
    }
}
