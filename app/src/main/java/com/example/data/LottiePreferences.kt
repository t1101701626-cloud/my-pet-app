package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.LottiePetState

class LottiePreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("lottie_panda_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_NAME = "panda_name"
        private const val KEY_HUNGER = "panda_hunger"
        private const val KEY_HYGIENE = "panda_hygiene"
        private const val KEY_ENERGY = "panda_energy"
        private const val KEY_HAPPINESS = "panda_happiness"
        private const val KEY_COINS = "panda_coins"
        private const val KEY_IS_SLEEPING = "panda_is_sleeping"
        private const val KEY_EQUIPPED_HAT = "panda_equipped_hat"
        private const val KEY_EQUIPPED_OUTFIT = "panda_equipped_outfit"
        private const val KEY_EQUIPPED_ACC = "panda_equipped_acc"
        private const val KEY_UNLOCKED_ITEMS = "panda_unlocked_items"
        private const val KEY_ONBOARDING_DONE = "panda_onboarding_done"
        private const val KEY_SOUND_ENABLED = "panda_sound_enabled"
        private const val KEY_MUSIC_ENABLED = "panda_music_enabled"
    }

    fun loadPetState(): LottiePetState {
        val name = prefs.getString(KEY_NAME, "Lottie") ?: "Lottie"
        val hunger = prefs.getFloat(KEY_HUNGER, 85f)
        val hygiene = prefs.getFloat(KEY_HYGIENE, 90f)
        val energy = prefs.getFloat(KEY_ENERGY, 80f)
        val happiness = prefs.getFloat(KEY_HAPPINESS, 95f)
        val coins = prefs.getInt(KEY_COINS, 120)
        val isSleeping = prefs.getBoolean(KEY_IS_SLEEPING, false)
        val equippedHat = prefs.getString(KEY_EQUIPPED_HAT, "flower_clip")
        val equippedOutfit = prefs.getString(KEY_EQUIPPED_OUTFIT, "mint_romper")
        val equippedAcc = prefs.getString(KEY_EQUIPPED_ACC, null)
        val unlockedSet = prefs.getStringSet(
            KEY_UNLOCKED_ITEMS,
            setOf("flower_clip", "mint_romper", "heart_glasses")
        ) ?: setOf("flower_clip", "mint_romper", "heart_glasses")
        val onboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false)

        return LottiePetState(
            name = name,
            hunger = hunger,
            hygiene = hygiene,
            energy = energy,
            happiness = happiness,
            bambooCoins = coins,
            isSleeping = isSleeping,
            equippedHatId = equippedHat,
            equippedOutfitId = equippedOutfit,
            equippedAccessoryId = equippedAcc,
            unlockedItemIds = unlockedSet,
            hasCompletedOnboarding = onboardingDone
        )
    }

    fun savePetState(state: LottiePetState) {
        prefs.edit()
            .putString(KEY_NAME, state.name)
            .putFloat(KEY_HUNGER, state.hunger)
            .putFloat(KEY_HYGIENE, state.hygiene)
            .putFloat(KEY_ENERGY, state.energy)
            .putFloat(KEY_HAPPINESS, state.happiness)
            .putInt(KEY_COINS, state.bambooCoins)
            .putBoolean(KEY_IS_SLEEPING, state.isSleeping)
            .putString(KEY_EQUIPPED_HAT, state.equippedHatId)
            .putString(KEY_EQUIPPED_OUTFIT, state.equippedOutfitId)
            .putString(KEY_EQUIPPED_ACC, state.equippedAccessoryId)
            .putStringSet(KEY_UNLOCKED_ITEMS, state.unlockedItemIds)
            .putBoolean(KEY_ONBOARDING_DONE, state.hasCompletedOnboarding)
            .apply()
    }

    fun isSoundEnabled(): Boolean = prefs.getBoolean(KEY_SOUND_ENABLED, true)
    fun setSoundEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()

    fun isMusicEnabled(): Boolean = prefs.getBoolean(KEY_MUSIC_ENABLED, true)
    fun setMusicEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_MUSIC_ENABLED, enabled).apply()
}
