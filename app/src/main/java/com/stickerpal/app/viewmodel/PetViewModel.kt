package com.stickerpal.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.stickerpal.app.data.database.AppDatabase
import com.stickerpal.app.data.repository.PetRepository

class PetViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val petRepository = PetRepository(database.petStateDao())

    suspend fun getPetState() = petRepository.getPetState()
    suspend fun interact() = petRepository.incrementIntimacy()
    suspend fun updateSkin(skin: String) = petRepository.updateSkin(skin)
    suspend fun updateMood(mood: com.stickerpal.app.data.model.PetMood) = petRepository.updatePetMood(mood)
}
