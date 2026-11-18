package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.components.BottomNav
import com.example.ui.components.MainSection
import com.example.ui.components.TopStatusBar
import com.example.ui.screens.BathroomScreen
import com.example.ui.screens.BedroomScreen
import com.example.ui.screens.HabitatScreen
import com.example.ui.screens.KitchenScreen
import com.example.ui.screens.LivingRoomScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.LottieViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: LottieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LottieMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LottieMainApp(viewModel: LottieViewModel) {
    val petState by viewModel.petState.collectAsState()
    val currentMood by viewModel.currentMood.collectAsState()
    val currentSection by viewModel.currentSection.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val speechText by viewModel.speechText.collectAsState()
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsState()

    // Back button handling: returns to SALA if on any other screen
    BackHandler(enabled = currentSection != MainSection.SALA && petState.hasCompletedOnboarding) {
        viewModel.setSection(MainSection.SALA)
    }

    if (!petState.hasCompletedOnboarding) {
        WelcomeScreen(
            onConfirmName = { name ->
                viewModel.confirmNameAndStart(name)
            }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                TopStatusBar(
                    state = petState,
                    isSoundEnabled = isSoundEnabled,
                    onToggleSound = { viewModel.toggleSound() }
                )
            },
            bottomBar = {
                BottomNav(
                    currentSection = currentSection,
                    onSectionSelected = { section ->
                        viewModel.setSection(section)
                    }
                )
            }
        ) { innerPadding ->
            // Section Content: background fills edge-to-edge while interactive UI stays strictly within innerPadding
            Crossfade(
                targetState = currentSection,
                label = "section_crossfade",
                modifier = Modifier.fillMaxSize()
            ) { section ->
                when (section) {
                    MainSection.SALA -> {
                        LivingRoomScreen(
                            state = petState,
                            mood = currentMood,
                            isListening = isListening,
                            isSpeaking = isSpeaking,
                            speechText = speechText,
                            onPetHead = { viewModel.petHead() },
                            onTickleBelly = { viewModel.tickleBelly() },
                            onTapNose = { viewModel.tapNose() },
                            onTapFeet = { viewModel.tapFeet() },
                            onStartListening = { viewModel.startListeningVoice() },
                            onSpeakPhrase = { phrase -> viewModel.speakPhrase(phrase) },
                            contentPadding = innerPadding
                        )
                    }
                    MainSection.COMER -> {
                        KitchenScreen(
                            state = petState,
                            mood = currentMood,
                            onFeedFood = { food -> viewModel.feedFood(food) },
                            onPetHead = { viewModel.petHead() },
                            contentPadding = innerPadding
                        )
                    }
                    MainSection.BANO -> {
                        BathroomScreen(
                            state = petState,
                            mood = currentMood,
                            onBrushTeeth = { progress -> viewModel.brushTeethProgress(progress) },
                            onApplySoap = { progress -> viewModel.bathSoapProgress(progress) },
                            onRinseWater = { viewModel.bathRinseWater() },
                            onPetHead = { viewModel.petHead() },
                            contentPadding = innerPadding
                        )
                    }
                    MainSection.DORMIR -> {
                        BedroomScreen(
                            state = petState,
                            mood = currentMood,
                            onToggleSleep = { viewModel.toggleSleep() },
                            onPetHead = { viewModel.petHead() },
                            onBuyOrEquipWardrobeItem = { item -> viewModel.buyOrEquipWardrobeItem(item) },
                            contentPadding = innerPadding
                        )
                    }
                    MainSection.HABITAT -> {
                        HabitatScreen(
                            state = petState,
                            mood = currentMood,
                            onRewardMinigame = { coins, praise ->
                                viewModel.rewardMinigame(coins, praise)
                            },
                            onPetHead = { viewModel.petHead() },
                            onTickleBelly = { viewModel.tickleBelly() },
                            contentPadding = innerPadding
                        )
                    }
                }
            }
        }
    }
}
