package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark
import kotlin.random.Random

enum class BathMode {
    TUB_BATH,
    BRUSH_TEETH
}

data class BathBubble(
    val id: Long = Random.nextLong(),
    val xPercent: Float,
    val yPercent: Float,
    val sizeDp: Int = Random.nextInt(30, 52)
)

@Composable
fun BathroomScreen(
    state: LottiePetState,
    mood: LottieMood,
    onBrushTeeth: (Float) -> Unit,
    onApplySoap: (Float) -> Unit,
    onRinseWater: () -> Unit,
    onPetHead: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var currentMode by remember { mutableStateOf(BathMode.TUB_BATH) }
    var soapLatherPercent by remember { mutableFloatStateOf(0f) }
    var brushProgressPercent by remember { mutableFloatStateOf(0f) }

    val interactiveBubbles = remember {
        mutableStateListOf(
            BathBubble(xPercent = 0.20f, yPercent = 0.58f),
            BathBubble(xPercent = 0.72f, yPercent = 0.52f),
            BathBubble(xPercent = 0.36f, yPercent = 0.72f),
            BathBubble(xPercent = 0.62f, yPercent = 0.66f),
            BathBubble(xPercent = 0.26f, yPercent = 0.42f),
            BathBubble(xPercent = 0.76f, yPercent = 0.70f)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Bathroom background edge-to-edge
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
                .padding(contentPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Mode Selector Tabs (Tina vs Cepillo)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xF0FFFFFF),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 520.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 42.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentMode == BathMode.TUB_BATH) Color(0xFF03A9F4) else Color.Transparent
                            )
                            .clickable { currentMode = BathMode.TUB_BATH }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .testTag("tab_tub_bath"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🛁 Baño de Tina",
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            color = if (currentMode == BathMode.TUB_BATH) PureWhite else SoftTextDark
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 42.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentMode == BathMode.BRUSH_TEETH) Color(0xFF00BCD4) else Color.Transparent
                            )
                            .clickable { currentMode = BathMode.BRUSH_TEETH }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .testTag("tab_brush_teeth"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🪥 Lavar Dientes",
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            color = if (currentMode == BathMode.BRUSH_TEETH) PureWhite else SoftTextDark
                        )
                    }
                }
            }

            // Grounded Panda Stage (directly over the bathroom background, no box behind panda)
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                val stageWidth = maxWidth
                val stageHeight = maxHeight
                val pandaSize = min(stageWidth * 0.78f, stageHeight * 0.92f).coerceIn(130.dp, 300.dp)

                // Lottie Panda resting naturally at the bottom/tub level of the stage
                LottiePandaView(
                    state = state,
                    mood = mood,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                        .size(pandaSize),
                    onPetHead = onPetHead
                )

                // Interactive Pop-able Bubbles positioned responsively within the stage
                if (currentMode == BathMode.TUB_BATH) {
                    interactiveBubbles.forEach { bubble ->
                        val bubbleSize = bubble.sizeDp.dp
                        val offsetX = ((stageWidth - bubbleSize) * bubble.xPercent.coerceIn(0.05f, 0.92f))
                        val offsetY = ((stageHeight - bubbleSize) * bubble.yPercent.coerceIn(0.15f, 0.90f))

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = offsetX, y = offsetY)
                                .size(bubbleSize)
                                .clip(CircleShape)
                                .background(Color(0x77E1F5FE))
                                .border(2.dp, Color(0xAAFFFFFF), CircleShape)
                                .pointerInput(bubble.id) {
                                    detectTapGestures {
                                        interactiveBubbles.remove(bubble)
                                        onApplySoap(2f)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🫧", fontSize = (bubble.sizeDp * 0.58f).sp)
                        }
                    }
                }
            }

            // Interactive Controls at Bottom
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 560.dp)
                    .clip(RoundedCornerShape(24.dp)),
                color = Color(0xF5FFFFFF),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentMode == BathMode.TUB_BATH) {
                        Text(
                            text = "¡Toca las burbujas para explotarlas o usa la regadera!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SoftTextDark,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Soap Bar Button
                            Button(
                                onClick = {
                                    soapLatherPercent = (soapLatherPercent + 0.25f).coerceAtMost(1f)
                                    onApplySoap(12f)
                                    interactiveBubbles.add(
                                        BathBubble(
                                            xPercent = Random.nextFloat() * 0.68f + 0.14f,
                                            yPercent = Random.nextFloat() * 0.40f + 0.38f
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("soap_button")
                            ) {
                                Text(
                                    text = "🧼 Enjabonar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = PureWhite
                                )
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
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("shower_button")
                            ) {
                                Text(
                                    text = "🚿 Enjuagar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = PureWhite
                                )
                            }
                        }
                    } else {
                        // BRUSH TEETH MODE
                        Text(
                            text = "¡Toca para cepillar y dejar los dientitos relucientes!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SoftTextDark,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                brushProgressPercent = (brushProgressPercent + 0.25f).coerceAtMost(1f)
                                onBrushTeeth(15f)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("brush_action_button"),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "🪥", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (brushProgressPercent >= 1f) "¡Dientes limpios y brillantes! ✨"
                                    else "¡Cepillar los dientes! 🍓",
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
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
