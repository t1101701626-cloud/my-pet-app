package com.example.ui.minigames

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.GoldenCoin
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark
import kotlinx.coroutines.delay

data class MemoryCard(
    val id: Int,
    val emoji: String,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)

@Composable
fun MemoryGame(
    onGameComplete: (coinsWon: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emojis = listOf("🐼", "🎋", "🍓", "🌸")
    val cards = remember {
        mutableStateListOf<MemoryCard>().apply {
            val deck = (emojis + emojis).shuffled().mapIndexed { index, emoji ->
                MemoryCard(id = index, emoji = emoji)
            }
            addAll(deck)
        }
    }

    var firstSelectedIndex by remember { mutableStateOf<Int?>(null) }
    var isBusy by remember { mutableStateOf(false) }
    var pairsFound by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(pairsFound) {
        if (pairsFound == emojis.size && !isFinished) {
            isFinished = true
            onGameComplete(20)
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF1F8E9)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("memory_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar al bosque",
                        tint = BambooGreenPrimary
                    )
                }

                Text(
                    text = "🧠 Encuentra las Parejas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BambooGreenPrimary
                    )
                )

                Text(
                    text = "⭐ $pairsFound/${emojis.size}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = GoldenCoin
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "¡Toca dos cartas para descubrir animalitos y frutas iguales!",
                fontSize = 13.sp,
                color = SoftTextDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Cards Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(cards) { index, card ->
                    val isRevealed = card.isFaceUp || card.isMatched
                    val rotation by animateFloatAsState(
                        targetValue = if (isRevealed) 180f else 0f,
                        animationSpec = tween(350),
                        label = "card_flip"
                    )

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (card.isMatched) Color(0xFFC8E6C9)
                            else if (card.isFaceUp) PureWhite
                            else Color(0xFFA5D6A7)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            3.dp,
                            if (card.isMatched) BambooGreenPrimary else Color(0xFF81C784)
                        ),
                        modifier = Modifier
                            .size(110.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                            .clickable(enabled = !isBusy && !card.isMatched && !card.isFaceUp) {
                                card.isFaceUp = true
                                if (firstSelectedIndex == null) {
                                    firstSelectedIndex = index
                                } else {
                                    val firstIndex = firstSelectedIndex!!
                                    val firstCard = cards[firstIndex]
                                    if (firstCard.emoji == card.emoji) {
                                        firstCard.isMatched = true
                                        card.isMatched = true
                                        pairsFound++
                                        firstSelectedIndex = null
                                    } else {
                                        isBusy = true
                                    }
                                }
                            }
                            .testTag("memory_card_$index")
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (rotation > 90f) {
                                Text(
                                    text = card.emoji,
                                    fontSize = 42.sp,
                                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                                )
                            } else {
                                Text(text = "❓", fontSize = 34.sp)
                            }
                        }
                    }
                }
            }

            // Flip back unmatched cards after delay
            LaunchedEffect(isBusy) {
                if (isBusy) {
                    delay(900)
                    cards.forEach { if (!it.isMatched) it.isFaceUp = false }
                    firstSelectedIndex = null
                    isBusy = false
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
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🎉 ¡FANTÁSTICO! 🎉", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFFF57F17))
                        Text(text = "¡Ganaste +20 monedas de bambú!", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BambooGreenPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onBack,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BambooGreenPrimary)
                        ) {
                            Text(text = "¡Volver al Bosque! 🎋", color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
