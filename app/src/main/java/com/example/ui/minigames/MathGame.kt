package com.example.ui.minigames

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.GoldenCoin
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark

data class MathQuestion(
    val title: String,
    val itemsEmoji: String,
    val count: Int,
    val options: List<Int>,
    val correctAnswer: Int
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MathGame(
    onGameComplete: (coinsWon: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember {
        listOf(
            MathQuestion(
                title = "¿Cuántas ramitas de bambú hay?",
                itemsEmoji = "🎋",
                count = 3,
                options = listOf(2, 3, 4, 5),
                correctAnswer = 3
            ),
            MathQuestion(
                title = "¿Cuántas manzanas deliciosas ves?",
                itemsEmoji = "🍎",
                count = 5,
                options = listOf(3, 4, 5, 6),
                correctAnswer = 5
            ),
            MathQuestion(
                title = "¿Cuántas frutillas dulces hay?",
                itemsEmoji = "🍓",
                count = 4,
                options = listOf(2, 4, 6, 8),
                correctAnswer = 4
            )
        )
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var feedbackText by remember { mutableStateOf<String?>(null) }
    var isFinished by remember { mutableStateOf(false) }

    val currentQ = questions[currentQuestionIndex]

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFFFFDE7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                    text = "🔢 Matemática Mágica",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF57F17)
                    )
                )

                Text(
                    text = "Ronda ${currentQuestionIndex + 1}/${questions.size}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = SoftTextDark
                )
            }

            // Question Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQ.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftTextDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Visual items to count
                    FlowRow(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(currentQ.count) {
                            Text(
                                text = currentQ.itemsEmoji,
                                fontSize = 42.sp,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }

            // Options Buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (feedbackText != null) {
                    Text(
                        text = feedbackText ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BambooGreenPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    currentQ.options.forEach { option ->
                        val isSelected = selectedOption == option
                        val isCorrect = option == currentQ.correctAnswer

                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSelected && isCorrect -> Color(0xFFC8E6C9)
                                        isSelected && !isCorrect -> Color(0xFFFFCDD2)
                                        else -> PureWhite
                                    }
                                )
                                .border(
                                    width = 3.dp,
                                    color = if (isSelected) BambooGreenPrimary else Color(0xFFFFD54F),
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedOption = option
                                    if (isCorrect) {
                                        feedbackText = "¡Excelente! ¡Contaste muy bien! 🌟"
                                        if (currentQuestionIndex < questions.size - 1) {
                                            currentQuestionIndex++
                                            selectedOption = null
                                            feedbackText = null
                                        } else {
                                            isFinished = true
                                            onGameComplete(20)
                                        }
                                    } else {
                                        feedbackText = "¡Intenta contar de nuevo con tu dedito! ☝️"
                                    }
                                }
                                .testTag("math_option_$option"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$option",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = SoftTextDark
                            )
                        }
                    }
                }
            }

            // Victory Dialog
            if (isFinished) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
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
                        Text(text = "🎉 ¡ERES UN GENIO! 🎉", fontSize = 20.sp, fontWeight = FontWeight.Black, color = BambooGreenPrimary)
                        Text(text = "¡Ganaste +20 monedas de bambú!", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF57F17))
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
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
