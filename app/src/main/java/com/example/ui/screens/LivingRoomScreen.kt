package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.R
import com.example.model.LottieMood
import com.example.model.LottiePetState
import com.example.ui.components.LottiePandaView
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark

@Composable
fun LivingRoomScreen(
    state: LottiePetState,
    mood: LottieMood,
    isListening: Boolean,
    isSpeaking: Boolean,
    speechText: String?,
    onPetHead: () -> Unit,
    onTickleBelly: () -> Unit,
    onTapNose: () -> Unit,
    onTapFeet: () -> Unit,
    onStartListening: () -> Unit,
    onSpeakPhrase: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onStartListening()
        } else {
            onSpeakPhrase("¡Hola! ¡Puedes tocarme para jugar conmigo!")
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.2f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isListening) 400 else 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val quickPhrases = listOf(
        "¡Hola amiguita! 🐼",
        "¿Quieres jugar? 🎈",
        "¡Eres muy tierna! 💕",
        "¡Te quiero mucho! 🌸",
        "¿Tienes hambre? 🥞",
        "¡Vamos a cantar! 🎶"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Living room background image edge-to-edge
        Image(
            painter = painterResource(id = R.drawable.img_home_room),
            contentDescription = "Sala de estar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Soft daylight overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x12FFFFFF))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Speech Balloon (Echo display)
            AnimatedVisibility(
                visible = !speechText.isNullOrBlank() || isListening || isSpeaking,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut()
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xF5FFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .widthIn(max = 520.dp)
                        .testTag("speech_balloon")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isListening) "👂 ¡Escuchándote con mis orejitas!..."
                            else if (isSpeaking) "🗣️ \"${speechText ?: "¡Ji ji ji!"}\""
                            else "\"${speechText ?: ""}\"",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SoftTextDark,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Grounded Central Lottie Panda Character
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
                    onTickleBelly = onTickleBelly,
                    onTapNose = onTapNose,
                    onTapFeet = onTapFeet
                )
            }

            // Bottom Actions: Voice Echo Mic + Quick Phrase Carousel
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
            ) {
                // Interactive Hints
                Text(
                    text = "Tócame la pancita para reír o la cabecita para mimarme ✨",
                    fontSize = 11.sp,
                    color = Color(0xDD374151),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xAAFFFFFF))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Microphone Echo Button (Talking Tom classic echo)
                Box(contentAlignment = Alignment.Center) {
                    if (isListening) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(Color(0x44E91E63))
                        )
                    }

                    IconButton(
                        onClick = {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission) {
                                onStartListening()
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        modifier = Modifier
                            .size(58.dp)
                            .scale(if (!isListening) pulseScale else 1f)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        if (isListening) Color(0xFFFF4081) else BambooGreenPrimary,
                                        if (isListening) Color(0xFFC2185B) else Color(0xFF2E7D32)
                                    )
                                )
                            )
                            .testTag("mic_echo_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Habla con Lottie",
                            tint = PureWhite,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Text(
                    text = if (isListening) "¡Dime algo, te escucho!" else "Toca para hablar con Lottie",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isListening) Color(0xFFD81B60) else SoftTextDark,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x99FFFFFF))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Talk Phrase Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(quickPhrases) { phrase ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xEEFFFFFF))
                                .clickable { onSpeakPhrase(phrase) }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = phrase,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SoftTextDark
                            )
                        }
                    }
                }
            }
        }
    }
}
