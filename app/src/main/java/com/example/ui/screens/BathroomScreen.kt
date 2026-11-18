package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.LottieMood
import com.example.model.LottiePetState
import com.example.ui.components.LottiePandaView
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.GoldenCoin
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark
import kotlin.math.roundToInt
import kotlin.random.Random

enum class BathMode {
    TUB_BATH,
    BRUSH_TEETH
}

data class BathBubble(
    val id: Long = Random.nextLong(),
    val xPercent: Float,
    val yPercent: Float,
    val sizeDp: Int = Random.nextInt(28, 55)
)

@Composable
fun BathroomScreen(
    state: LottiePetState,
    mood: LottieMood,
    onBrushTeeth: (Float) -> Unit,
    onApplySoap: (Float) -> Unit,
    onRinseWater: () -> Unit,
    onPetHead: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMode by remember { mutableStateOf(BathMode.TUB_BATH) }
    var soapLatherPercent by remember { mutableFloatStateOf(0f) }
    var brushProgressPercent by remember { mutableFloatStateOf(0f) }

    val interactiveBubbles = remember {
        mutableStateListOf(
            BathBubble(xPercent = 0.25f, yPercent = 0.55f),
            BathBubble(xPercent = 0.70f, yPercent = 0.50f),
            BathBubble(xPercent = 0.40f, yPercent = 0.65f),
            BathBubble(xPercent = 0.60f, yPercent = 0.60f),
            BathBubble(xPercent = 0.30f, yPercent = 0.45f),
            BathBubble(xPercent = 0.75f, yPercent = 0.62f)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Bathroom background
        Image(
            painter = painterResource(id = R.drawable.img_bathroom),
            contentDescription = "Baño de Lottie",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Soft pastel mint overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x10E0F7FA))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 85.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Mode Selector Tabs (Tina vs Cepillo)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xF0FFFFFF),
                shadowElevation = 6.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(modifier = Modifier.padding(6.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentMode == BathMode.TUB_BATH) Color(0xFF03A9F4) else Color.Transparent
                            )
                            .clickable { currentMode = BathMode.TUB_BATH }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("tab_tub_bath")
                    ) {
                        Text(
                            text = "🛁 Baño de Tina",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentMode == BathMode.TUB_BATH) PureWhite else SoftTextDark
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentMode == BathMode.BRUSH_TEETH) Color(0xFF00BCD4) else Color.Transparent
                            )
                            .clickable { currentMode = BathMode.BRUSH_TEETH }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("tab_brush_teeth")
                    ) {
                        Text(
                            text = "🪥 Lavar Dientes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentMode == BathMode.BRUSH_TEETH) PureWhite else SoftTextDark
                        )
                    }
                }
            }

            // Central Bathtub / Panda Activity Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (currentMode == BathMode.TUB_BATH) {
                    // Bathtub rim behind/around panda
                    Box(
                        modifier = Modifier
                            .size(width = 280.dp, height = 260.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))
                                )
                            )
                            .border(6.dp, Color(0xFF81D4FA), RoundedCornerShape(40.dp))
                    )
                }

                // Lottie Panda
                LottiePandaView(
                    state = state,
                    mood = mood,
                    modifier = Modifier.size(290.dp),
                    onPetHead = onPetHead
                )

                // Interactive Pop-able Bubbles in Tub Bath Mode
                if (currentMode == BathMode.TUB_BATH) {
                    interactiveBubbles.forEach { bubble ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(bubble.id) {
                                    detectTapGestures {
                                        interactiveBubbles.remove(bubble)
                                        onApplySoap(2f)
                                    }
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .offset(
                                        x = (bubble.xPercent * 320).dp,
                                        y = (bubble.yPercent * 400).dp
                                    )
                                    .size(bubble.sizeDp.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x77E1F5FE))
                                    .border(2.dp, Color(0xAAFFFFFF), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🫧", fontSize = (bubble.sizeDp * 0.6).sp)
                            }
                        }
                    }
                }
            }

            // Interactive Controls at Bottom
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(26.dp)),
                color = Color(0xF5FFFFFF),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentMode == BathMode.TUB_BATH) {
                        Text(
                            text = "¡Toca las burbujas para explotarlas o usa la regadera!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = SoftTextDark,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Soap Bar Button
                            Button(
                                onClick = {
                                    soapLatherPercent = (soapLatherPercent + 0.25f).coerceAtMost(1f)
                                    onApplySoap(12f)
                                    // Add fresh bubbles
                                    interactiveBubbles.add(
                                        BathBubble(
                                            xPercent = Random.nextFloat() * 0.6f + 0.2f,
                                            yPercent = Random.nextFloat() * 0.3f + 0.45f
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
                                modifier = Modifier.testTag("soap_button")
                            ) {
                                Text(text = "🧼 Enjabonar", fontWeight = FontWeight.Bold, color = PureWhite)
                            }

                            // Shower Rinse Button
                            Button(
                                onClick = {
                                    soapLatherPercent = 0f
                                    interactiveBubbles.clear()
                                    onRinseWater()
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4)),
                                modifier = Modifier.testTag("shower_button")
                            ) {
                                Text(text = "🚿 Enjuagar", fontWeight = FontWeight.Bold, color = PureWhite)
                            }
                        }
                    } else {
                        // BRUSH TEETH MODE
                        Text(
                            text = "¡Mueve el cepillo para dejar los dientitos relucientes!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = SoftTextDark,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                brushProgressPercent = (brushProgressPercent + 0.25f).coerceAtMost(1f)
                                onBrushTeeth(15f)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("brush_action_button"),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🪥", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (brushProgressPercent >= 1f) "¡Dientes limpios y brillantes! ✨"
                                    else "¡Cepillar los dientes con pasta rica! 🍓",
                                    fontSize = 15.sp,
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
