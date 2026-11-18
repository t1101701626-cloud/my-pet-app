package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
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
import com.example.ui.theme.GoldenCoin
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
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var activeGame by remember { mutableStateOf(ActiveMinigame.NONE) }
    var showGameMenuSheet by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "gamepad_pulse")
    val gamepadScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gamepad_scale"
    )

    // If a minigame is actively running, render it cleanly inside contentPadding
    when (activeGame) {
        ActiveMinigame.MEMORY -> {
            MemoryGame(
                onGameComplete = { coins ->
                    onRewardMinigame(coins, "¡Increíble memoria! ¡Encontraste todas las parejas!")
                },
                onBack = { activeGame = ActiveMinigame.NONE },
                contentPadding = contentPadding
            )
            return
        }
        ActiveMinigame.MATH -> {
            MathGame(
                onGameComplete = { coins ->
                    onRewardMinigame(coins, "¡Qué bien cuentas! ¡Eres muy inteligente!")
                },
                onBack = { activeGame = ActiveMinigame.NONE },
                contentPadding = contentPadding
            )
            return
        }
        ActiveMinigame.SEQUENCE -> {
            SequenceStoryGame(
                onGameComplete = { coins ->
                    onRewardMinigame(coins, "¡Completaste la historia del bosque a la perfección!")
                },
                onBack = { activeGame = ActiveMinigame.NONE },
                contentPadding = contentPadding
            )
            return
        }
        ActiveMinigame.SORT -> {
            SortGame(
                onGameComplete = { coins ->
                    onRewardMinigame(coins, "¡Excelente clasificación de colores y objetos!")
                },
                onBack = { activeGame = ActiveMinigame.NONE },
                contentPadding = contentPadding
            )
            return
        }
        ActiveMinigame.NONE -> {}
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Enchanted bamboo forest background edge-to-edge
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
                .padding(contentPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Cheerful Banner
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 520.dp)
            ) {
                Text(
                    text = "🎋 Bosque Mágico de Bambú 🦋",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = BambooGreenDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Grounded Panda enjoying the nature (standing at bottom floor of stage)
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                val pandaSize = min(maxWidth * 0.78f, maxHeight * 0.94f).coerceIn(130.dp, 310.dp)

                LottiePandaView(
                    state = state,
                    mood = mood,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                        .size(pandaSize),
                    onPetHead = onPetHead,
                    onTickleBelly = onTickleBelly
                )
            }

            // Side Game Controller Floating Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 560.dp)
                    .clip(RoundedCornerShape(24.dp)),
                color = Color(0xF5FFFFFF),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp)
                    ) {
                        Text(
                            text = "Juegos Educativos 🎮",
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.ExtraBold,
                            color = SoftTextDark
                        )
                        Text(
                            text = "¡Juega y gana monedas para Lottie!",
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF66BB6A),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Game Controller Action Button
                    Button(
                        onClick = { showGameMenuSheet = true },
                        modifier = Modifier
                            .heightIn(min = 48.dp)
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
                        .widthIn(max = 600.dp)
                        .align(Alignment.CenterHorizontally)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 8.dp)
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

                    Spacer(modifier = Modifier.height(14.dp))

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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

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

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "🪙 +20", fontWeight = FontWeight.Bold, color = GoldenCoin, fontSize = 13.sp)
        }
    }
}
