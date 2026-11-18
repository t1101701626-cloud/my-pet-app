package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.LottieMood
import com.example.model.LottiePetState
import com.example.ui.components.LottiePandaView
import com.example.ui.minigames.MathGame
import com.example.ui.minigames.MemoryGame
import com.example.ui.minigames.SequenceStoryGame
import com.example.ui.minigames.SortGame
import com.example.ui.theme.BambooGreenDark
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.GoldenCoin
import com.example.ui.theme.PastelPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark

enum class ActiveMinigame {
    NONE,
    MEMORY,
    MATH,
    SEQUENCE,
    SORT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitatScreen(
    state: LottiePetState,
    mood: LottieMood,
    onRewardMinigame: (coinsWon: Int, praise: String) -> Unit,
    onPetHead: () -> Unit,
    onTickleBelly: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeGame by remember { mutableStateOf(ActiveMinigame.NONE) }
    var showGameMenuSheet by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "gamepad_pulse")
    val gamepadScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gamepad_scale"
    )

    // If a minigame is actively running, render it full screen
    when (activeGame) {
        ActiveMinigame.MEMORY -> {
            MemoryGame(
                onGameComplete = { coins ->
                    onRewardMinigame(coins, "¡Increíble memoria! ¡Encontraste todas las parejas!")
                },
                onBack = { activeGame = ActiveMinigame.NONE }
            )
            return
        }
        ActiveMinigame.MATH -> {
            MathGame(
                onGameComplete = { coins ->
                    onRewardMinigame(coins, "¡Qué bien cuentas! ¡Eres muy inteligente!")
                },
                onBack = { activeGame = ActiveMinigame.NONE }
            )
            return
        }
        ActiveMinigame.SEQUENCE -> {
            SequenceStoryGame(
                onGameComplete = { coins ->
                    onRewardMinigame(coins, "¡Completaste la historia del bosque a la perfección!")
                },
                onBack = { activeGame = ActiveMinigame.NONE }
            )
            return
        }
        ActiveMinigame.SORT -> {
            SortGame(
                onGameComplete = { coins ->
                    onRewardMinigame(coins, "¡Excelente clasificación de colores y objetos!")
                },
                onBack = { activeGame = ActiveMinigame.NONE }
            )
            return
        }
        ActiveMinigame.NONE -> {}
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Enchanted bamboo forest background
        Image(
            painter = painterResource(id = R.drawable.img_forest_habitat),
            contentDescription = "Bosque encantado",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Soft nature light tint
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x10000000))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 85.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Cheerful Banner
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "🎋 Bosque Mágico de Bambú 🦋",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BambooGreenDark,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Central Panda enjoying the nature
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LottiePandaView(
                    state = state,
                    mood = mood,
                    modifier = Modifier.size(310.dp),
                    onPetHead = onPetHead,
                    onTickleBelly = onTickleBelly
                )
            }

            // Side Game Controller Floating Button & Bottom Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(26.dp)),
                color = Color(0xF5FFFFFF),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Juegos Educativos 🎮",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SoftTextDark
                        )
                        Text(
                            text = "¡Juega y gana monedas para Lottie!",
                            fontSize = 12.sp,
                            color = Color(0xFF66BB6A),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Game Controller Action Button
                    Button(
                        onClick = { showGameMenuSheet = true },
                        modifier = Modifier
                            .scale(gamepadScale)
                            .testTag("open_minigames_button"),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🕹️", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "JUGAR",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = SoftTextDark
                            )
                        }
                    }
                }
            }
        }

        // Minigames Selection Modal Sheet
        if (showGameMenuSheet) {
            ModalBottomSheet(
                onDismissRequest = { showGameMenuSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color(0xFFFFFDF7)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "🌟 Minijuegos para Aprender y Divertirse",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SoftTextDark
                    )
                    Text(
                        text = "Sin límite de tiempo y con recompensas felices ✨",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. Memory Game
                    MinigameItemCard(
                        emoji = "🧠",
                        title = "1. Memoria de Parejas",
                        description = "Encuentra cartas iguales de animalitos y frutas",
                        color = Color(0xFFA5D6A7),
                        onClick = {
                            showGameMenuSheet = false
                            activeGame = ActiveMinigame.MEMORY
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Math Game
                    MinigameItemCard(
                        emoji = "🔢",
                        title = "2. Matemática Mágica",
                        description = "Cuenta bambú y ricas frutas con números",
                        color = Color(0xFFFFD54F),
                        onClick = {
                            showGameMenuSheet = false
                            activeGame = ActiveMinigame.MATH
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. Sequence Story Game
                    MinigameItemCard(
                        emoji = "📖",
                        title = "3. Historias y Secuencias",
                        description = "Ordena cómo crece el bambú y la naturaleza",
                        color = Color(0xFFCE93D8),
                        onClick = {
                            showGameMenuSheet = false
                            activeGame = ActiveMinigame.SEQUENCE
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 4. Sort & Colors Game
                    MinigameItemCard(
                        emoji = "🎨",
                        title = "4. Asociación y Clasificación",
                        description = "Guarda cada objeto en su cesta del mismo color",
                        color = Color(0xFF80DEEA),
                        onClick = {
                            showGameMenuSheet = false
                            activeGame = ActiveMinigame.SORT
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MinigameItemCard(
    emoji: String,
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, color),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftTextDark
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(text = "🪙 +20", fontWeight = FontWeight.Bold, color = GoldenCoin, fontSize = 13.sp)
        }
    }
}
