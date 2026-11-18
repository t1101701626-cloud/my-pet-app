package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.audio.VoiceEchoManager
import com.example.data.LottiePreferences
import com.example.model.FoodItem
import com.example.model.GameCatalogs
import com.example.model.LottieMood
import com.example.model.LottiePetState
import com.example.model.WardrobeItem
import com.example.ui.components.MainSection
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LottieViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = LottiePreferences(application)
    val soundManager = SoundManager(application)

    private val _petState = MutableStateFlow(prefs.loadPetState())
    val petState: StateFlow<LottiePetState> = _petState.asStateFlow()

    private val _currentMood = MutableStateFlow(LottieMood.HAPPY)
    val currentMood: StateFlow<LottieMood> = _currentMood.asStateFlow()

    private val _currentSection = MutableStateFlow(MainSection.SALA)
    val currentSection: StateFlow<MainSection> = _currentSection.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _speechText = MutableStateFlow<String?>(null)
    val speechText: StateFlow<String?> = _speechText.asStateFlow()

    private val _isSoundEnabled = MutableStateFlow(prefs.isSoundEnabled())
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    private var moodResetJob: Job? = null
    private var sleepEnergyJob: Job? = null

    val voiceEchoManager = VoiceEchoManager(
        context = application,
        onListeningStateChanged = { listening ->
            _isListening.value = listening
            if (listening) {
                setMood(LottieMood.LISTENING, 10000)
            }
        },
        onSpeakingStateChanged = { speaking ->
            _isSpeaking.value = speaking
            if (speaking) {
                setMood(LottieMood.TALKING, 5000)
            } else {
                setMood(LottieMood.HAPPY, 2000)
            }
        },
        onEchoTextReceived = { text ->
            _speechText.value = text
        }
    )

    init {
        soundManager.isSoundEnabled = _isSoundEnabled.value
        // Periodic gentle stat decay simulation
        viewModelScope.launch {
            while (true) {
                delay(30000)
                if (!_petState.value.isSleeping) {
                    updatePet { current ->
                        current.copy(
                            hunger = (current.hunger - 1.5f).coerceAtLeast(10f),
                            energy = (current.energy - 1.0f).coerceAtLeast(10f),
                            hygiene = (current.hygiene - 0.8f).coerceAtLeast(10f)
                        )
                    }
                }
            }
        }
    }

    fun setSection(section: MainSection) {
        _currentSection.value = section
        if (_petState.value.isSleeping && section != MainSection.DORMIR) {
            wakeUp()
        }
    }

    fun confirmNameAndStart(name: String) {
        val trimmed = name.trim().ifBlank { "Lottie" }
        updatePet {
            it.copy(
                name = trimmed,
                hasCompletedOnboarding = true
            )
        }
        setMood(LottieMood.HAPPY, 3000)
        soundManager.playReward()
        voiceEchoManager.speakText("¡Hola! ¡Me llamo $trimmed! ¡Gracias por ser mi mejor amigo!")
    }

    fun petHead() {
        if (_petState.value.isSleeping) {
            wakeUp()
            return
        }
        soundManager.playPurr()
        setMood(LottieMood.LOVED, 2500)
        updatePet {
            it.copy(
                happiness = (it.happiness + 6f).coerceAtMost(100f)
            )
        }
    }

    fun tickleBelly() {
        if (_petState.value.isSleeping) {
            wakeUp()
            return
        }
        soundManager.playGiggle()
        setMood(LottieMood.TICKLED, 2000)
        updatePet {
            it.copy(
                happiness = (it.happiness + 8f).coerceAtMost(100f)
            )
        }
    }

    fun tapNose() {
        if (_petState.value.isSleeping) return
        soundManager.playSneeze()
        setMood(LottieMood.SNEEZING, 2200)
        updatePet {
            it.copy(
                happiness = (it.happiness + 4f).coerceAtMost(100f)
            )
        }
    }

    fun tapFeet() {
        if (_petState.value.isSleeping) return
        soundManager.playGiggle()
        setMood(LottieMood.SURPRISED, 1800)
    }

    fun feedFood(food: FoodItem): Boolean {
        val current = _petState.value
        if (food.price > 0 && current.bambooCoins < food.price) {
            return false
        }

        soundManager.playMunch()
        setMood(LottieMood.CHEWING, 2500)

        updatePet {
            it.copy(
                hunger = (it.hunger + food.hungerRestore).coerceAtMost(100f),
                happiness = (it.happiness + food.happinessBonus).coerceAtMost(100f),
                bambooCoins = it.bambooCoins - food.price
            )
        }
        return true
    }

    fun brushTeethProgress(step: Float) {
        soundManager.playBrush()
        setMood(LottieMood.BRUSHING, 1500)
        updatePet {
            it.copy(
                hygiene = (it.hygiene + step).coerceAtMost(100f),
                happiness = (it.happiness + 1f).coerceAtMost(100f)
            )
        }
    }

    fun bathSoapProgress(step: Float) {
        soundManager.playBubblePop()
        setMood(LottieMood.BATHING, 1500)
        updatePet {
            it.copy(
                hygiene = (it.hygiene + step).coerceAtMost(100f),
                happiness = (it.happiness + 1f).coerceAtMost(100f)
            )
        }
    }

    fun bathRinseWater() {
        soundManager.playSplash()
        setMood(LottieMood.HAPPY, 2500)
        updatePet {
            it.copy(
                hygiene = 100f,
                happiness = (it.happiness + 10f).coerceAtMost(100f),
                bambooCoins = it.bambooCoins + 5
            )
        }
    }

    fun toggleSleep() {
        val sleeping = !_petState.value.isSleeping
        updatePet { it.copy(isSleeping = sleeping) }

        if (sleeping) {
            setMood(LottieMood.SLEEPING, 0)
            soundManager.playSnore()
            sleepEnergyJob?.cancel()
            sleepEnergyJob = viewModelScope.launch {
                while (_petState.value.isSleeping) {
                    delay(3000)
                    soundManager.playSnore()
                    updatePet {
                        it.copy(
                            energy = (it.energy + 8f).coerceAtMost(100f)
                        )
                    }
                }
            }
        } else {
            wakeUp()
        }
    }

    fun wakeUp() {
        sleepEnergyJob?.cancel()
        sleepEnergyJob = null
        updatePet { it.copy(isSleeping = false) }
        setMood(LottieMood.HAPPY, 2500)
        soundManager.playReward()
    }

    fun buyOrEquipWardrobeItem(item: WardrobeItem): Boolean {
        val current = _petState.value
        val isUnlocked = current.unlockedItemIds.contains(item.id)

        if (!isUnlocked) {
            if (current.bambooCoins < item.price) {
                return false
            }
            // Buy item
            soundManager.playReward()
            updatePet {
                it.copy(
                    bambooCoins = it.bambooCoins - item.price,
                    unlockedItemIds = it.unlockedItemIds + item.id
                )
            }
        }

        // Toggle equip
        soundManager.playCoin()
        updatePet { state ->
            when (item.category) {
                com.example.model.WardrobeCategory.HATS -> {
                    state.copy(equippedHatId = if (state.equippedHatId == item.id) null else item.id)
                }
                com.example.model.WardrobeCategory.OUTFITS -> {
                    state.copy(equippedOutfitId = if (state.equippedOutfitId == item.id) null else item.id)
                }
                com.example.model.WardrobeCategory.ACCESSORIES -> {
                    state.copy(equippedAccessoryId = if (state.equippedAccessoryId == item.id) null else item.id)
                }
            }
        }
        return true
    }

    fun rewardMinigame(coins: Int, praise: String = "¡Excelente!") {
        soundManager.playReward()
        updatePet {
            it.copy(
                bambooCoins = it.bambooCoins + coins,
                happiness = 100f
            )
        }
        setMood(LottieMood.HAPPY, 3000)
        voiceEchoManager.speakText(praise)
    }

    fun speakPhrase(phrase: String) {
        _speechText.value = phrase
        voiceEchoManager.speakText(phrase)
    }

    fun startListeningVoice() {
        voiceEchoManager.startListening()
    }

    fun toggleSound() {
        val enabled = !_isSoundEnabled.value
        _isSoundEnabled.value = enabled
        soundManager.isSoundEnabled = enabled
        prefs.setSoundEnabled(enabled)
    }

    private fun setMood(mood: LottieMood, durationMs: Long) {
        _currentMood.value = mood
        moodResetJob?.cancel()
        if (durationMs > 0) {
            moodResetJob = viewModelScope.launch {
                delay(durationMs)
                if (_petState.value.isSleeping) {
                    _currentMood.value = LottieMood.SLEEPING
                } else {
                    _currentMood.value = LottieMood.HAPPY
                }
            }
        }
    }

    private fun updatePet(transform: (LottiePetState) -> LottiePetState) {
        val newState = transform(_petState.value)
        _petState.value = newState
        prefs.savePetState(newState)
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
        voiceEchoManager.release()
    }
}
