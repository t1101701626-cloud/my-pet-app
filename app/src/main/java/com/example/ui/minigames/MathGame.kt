package com.example.ui.minigames

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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
                        text = "🔢 Matemática Mágica",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57F17)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Ronda ${currentQuestionIndex + 1}/${questions.size}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = SoftTextDark
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Question Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentQ.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftTextDark,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Visual items to count
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(currentQ.count) {
                                Text(
                                    text = currentQ.itemsEmoji,
                                    fontSize = 38.sp,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Options Buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (feedbackText != null) {
                        Text(
                            text = feedbackText ?: "",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BambooGreenPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                    ) {
                        currentQ.options.forEach { option ->
                            val isSelected = selectedOption == option
                            val isCorrect = option == currentQ.correctAnswer

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .sizeIn(maxWidth = 72.dp, maxHeight = 72.dp)
                                    .aspectRatio(1f)
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
                                    fontSize = 26.sp,
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
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🎉 ¡ERES UN GENIO! 🎉", fontSize = 19.sp, fontWeight = FontWeight.Black, color = BambooGreenPrimary)
                            Text(text = "¡Ganaste +20 monedas de bambú!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF57F17))
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
