package com.example.ui.minigames

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark

data class StoryCard(
    val id: Int,
    val stepNumber: Int,
    val emoji: String,
    val text: String
)

@Composable
fun SequenceStoryGame(
    onGameComplete: (coinsWon: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val storyCards = remember {
        listOf(
            StoryCard(1, 1, "🌱", "1. Sembrar la semillita"),
            StoryCard(2, 2, "🎋", "2. Crece el bambú verde"),
            StoryCard(3, 3, "🐼", "3. Lottie come bambú feliz")
        )
    }

    var nextExpectedStep by remember { mutableIntStateOf(1) }
    val placedCards = remember { mutableStateListOf<StoryCard>() }
    var hintText by remember { mutableStateOf("Toca qué pasa PRIMERO en la historia 🌱") }
    var isFinished by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF3E5F5)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar al bosque",
                            tint = BambooGreenPrimary
                        )
                    }

                    Text(
                        text = "📖 Historias y Secuencias",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8E24AA)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(text = "Paso $nextExpectedStep/3", fontWeight = FontWeight.Bold, color = SoftTextDark)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Hint Text
                Text(
                    text = hintText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftTextDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(PureWhite)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Story Sequence Slots (1, 2, 3)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (step in 1..3) {
                        val placed = placedCards.find { it.stepNumber == step }
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (placed != null) Color(0xFFE1BEE7) else PureWhite
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                if (placed != null) Color(0xFF8E24AA) else Color(0xFFCE93D8)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(115.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (placed != null) {
                                    Text(text = placed.emoji, fontSize = 34.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = placed.text,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 12.sp,
                                        color = SoftTextDark
                                    )
                                } else {
                                    Text(text = "Paso $step", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(text = "❓", fontSize = 26.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Cards available to choose from
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Opciones para elegir:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SoftTextDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val remainingCards = remember(placedCards.size) {
                            storyCards.filter { !placedCards.contains(it) }.shuffled()
                        }
                        remainingCards.forEach { card ->
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(105.dp)
                                    .clickable {
                                        if (card.stepNumber == nextExpectedStep) {
                                            placedCards.add(card)
                                            nextExpectedStep++
                                            if (nextExpectedStep <= 3) {
                                                hintText = "¡Muy bien! ¿Qué ocurre después? 🎋"
                                            } else {
                                                hintText = "¡Excelente historia completa! 💖"
                                                isFinished = true
                                                onGameComplete(20)
                                            }
                                        } else {
                                            hintText = "¡Casi! Primero sembramos la semilla 🌱"
                                        }
                                    }
                                    .testTag("story_card_${card.id}")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = card.emoji, fontSize = 32.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = card.text,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                // Victory Card
                if (isFinished) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🎉 ¡HISTORIA COMPLETADA! 🎉", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF8E24AA))
                            Text(text = "¡Ganaste +20 monedas de bambú!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BambooGreenPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onBack,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BambooGreenPrimary)
                            ) {
                                Text(text = "¡Volver al Bosque! 🎋", color = PureWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
