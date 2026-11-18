package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.ui.theme.BambooGreenDark
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.GoldenCoin
import com.example.ui.theme.PastelPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WelcomeScreen(
    onConfirmName: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var hasPressedPlay by remember { mutableStateOf(false) }
    var petNameInput by remember { mutableStateOf("Lottie") }

    val nameSuggestions = listOf("Lottie", "Bambú", "Pandi", "Mochi", "Copito")

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_pulse"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Nature / Nursery Background Image
        Image(
            painter = painterResource(id = R.drawable.img_forest_habitat),
            contentDescription = "Fondo de bosque mágico",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Soft pastel gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x77000000),
                            Color(0x33000000),
                            Color(0xAAFFFFFF)
                        )
                    )
                )
        )

        if (!hasPressedPlay) {
            // STEP 1: Welcome & Logo Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Title Banner with natural floral elements
                Card(
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🌿 🌸 🎋", fontSize = 26.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "My Friend Lottie",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BambooGreenDark,
                                fontSize = 32.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tu tierna bebé panda virtual",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF66BB6A)
                        )
                    }
                }

                // Center Panda Preview
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .clip(CircleShape)
                        .background(Color(0xCCFFFFFF))
                        .border(4.dp, PastelPink, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    LottiePandaView(
                        state = LottiePetState(name = "Lottie"),
                        mood = LottieMood.HAPPY,
                        modifier = Modifier.size(210.dp)
                    )
                }

                // Big "Jugar" Button
                Button(
                    onClick = { hasPressedPlay = true },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(64.dp)
                        .scale(buttonScale)
                        .testTag("play_button"),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BambooGreenPrimary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¡JUGAR!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = PureWhite
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "🐾", fontSize = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        } else {
            // STEP 2: Arrival & Naming in Cozy Room
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 200 })
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // Nursery background
                    Image(
                        painter = painterResource(id = R.drawable.img_home_room),
                        contentDescription = "Habitación con cunita",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .padding(16.dp),
                        shape = RoundedCornerShape(32.dp),
                        color = Color(0xF7FFFFFF),
                        shadowElevation = 12.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🍼 ✨ 🛏️", fontSize = 24.sp)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "¡Ha llegado tu bebé panda!",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BambooGreenDark
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "¿Cómo quieres llamar a tu panda?",
                                fontSize = 15.sp,
                                color = SoftTextDark,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Panda in crib illustration
                            Box(
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                LottiePandaView(
                                    state = LottiePetState(name = petNameInput),
                                    mood = LottieMood.LOVED,
                                    modifier = Modifier.size(160.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Name Input
                            OutlinedTextField(
                                value = petNameInput,
                                onValueChange = { if (it.length <= 15) petNameInput = it },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BambooGreenPrimary,
                                    unfocusedBorderColor = Color(0xFFC8E6C9),
                                    focusedContainerColor = Color(0xFFF1F8E9),
                                    unfocusedContainerColor = Color(0xFFF1F8E9)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("pet_name_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Quick suggestion chips
                            FlowRow(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                nameSuggestions.forEach { suggestion ->
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                if (petNameInput == suggestion) BambooGreenPrimary else Color(0xFFE0E0E0)
                                            )
                                            .clickable { petNameInput = suggestion }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = suggestion,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (petNameInput == suggestion) PureWhite else SoftTextDark
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Confirm Button
                            Button(
                                onClick = { onConfirmName(petNameInput) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .testTag("confirm_name_button"),
                                shape = RoundedCornerShape(26.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BambooGreenPrimary)
                            ) {
                                Text(
                                    text = "¡Adoptar a $petNameInput! 💕",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
