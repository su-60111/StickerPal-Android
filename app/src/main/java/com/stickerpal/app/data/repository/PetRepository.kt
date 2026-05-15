package com.stickerpal.app.data.repository

import com.stickerpal.app.data.dao.PetStateDao
import com.stickerpal.app.data.entity.PetState
import com.stickerpal.app.data.model.PetMood

class PetRepository(private val petStateDao: PetStateDao) {
    suspend fun getPetState(): PetState {
        val existing = petStateDao.getPetState("default")
        if (existing != null) return existing
        val newState = PetState(mood = PetMood.NORMAL.name)
        petStateDao.insert(newState)
        return newState
    }

    suspend fun updatePetMood(mood: PetMood) {
        val state = getPetState()
        petStateDao.update(state.copy(mood = mood.name, lastInteraction = System.currentTimeMillis()))
    }

    suspend fun incrementIntimacy() {
        val state = getPetState()
        petStateDao.update(state.copy(intimacyLevel = state.intimacyLevel + 1, lastInteraction = System.currentTimeMillis()))
    }

    suspend fun updateSkin(skin: String) {
        val state = getPetState()
        petStateDao.update(state.copy(currentSkin = skin))
    }
}
