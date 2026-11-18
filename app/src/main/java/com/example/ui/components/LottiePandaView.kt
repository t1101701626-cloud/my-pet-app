package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LottieMood
import com.example.model.LottiePetState
import com.example.ui.theme.GoldenCoin
import com.example.ui.theme.PandaBlack
import com.example.ui.theme.PastelPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftLilac
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

data class FloatingParticle(
    val id: Long = Random.nextLong(),
    val emoji: String,
    val startX: Float,
    val startY: Float,
    val offsetX: Float = (Random.nextFloat() - 0.5f) * 60f
)

@Composable
fun LottiePandaView(
    state: LottiePetState,
    mood: LottieMood,
    modifier: Modifier = Modifier,
    onPetHead: () -> Unit = {},
    onTickleBelly: () -> Unit = {},
    onTapNose: () -> Unit = {},
    onTapFeet: () -> Unit = {}
) {
    val particles = remember { mutableStateListOf<FloatingParticle>() }

    // Breathing and idle bouncing
    val infiniteTransition = rememberInfiniteTransition(label = "lottie_idle")
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.035f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    val earWiggle by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ear_wiggle"
    )

    // Mouth talking flap
    val mouthFlap by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(140, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mouth_flap"
    )

    // Jiggle for tickle
    val jiggleX = remember { Animatable(0f) }
    val jumpY = remember { Animatable(0f) }

    LaunchedEffect(mood) {
        when (mood) {
            LottieMood.TICKLED -> {
                launch {
                    repeat(6) {
                        jiggleX.animateTo(8f, tween(50))
                        jiggleX.animateTo(-8f, tween(50))
                    }
                    jiggleX.animateTo(0f, spring())
                }
                particles.add(FloatingParticle(emoji = "😄", startX = 0.5f, startY = 0.6f))
                particles.add(FloatingParticle(emoji = "✨", startX = 0.4f, startY = 0.5f))
            }
            LottieMood.LOVED -> {
                particles.add(FloatingParticle(emoji = "💖", startX = 0.45f, startY = 0.35f))
                particles.add(FloatingParticle(emoji = "🌸", startX = 0.6f, startY = 0.3f))
                particles.add(FloatingParticle(emoji = "💕", startX = 0.35f, startY = 0.4f))
            }
            LottieMood.SURPRISED -> {
                launch {
                    jumpY.animateTo(-25f, tween(120, easing = FastOutSlowInEasing))
                    jumpY.animateTo(0f, spring())
                }
                particles.add(FloatingParticle(emoji = "⭐", startX = 0.5f, startY = 0.3f))
            }
            LottieMood.SNEEZING -> {
                launch {
                    jumpY.animateTo(-15f, tween(90))
                    jumpY.animateTo(0f, spring())
                }
                particles.add(FloatingParticle(emoji = "💨", startX = 0.5f, startY = 0.45f))
            }
            LottieMood.CHEWING -> {
                particles.add(FloatingParticle(emoji = "🍪", startX = 0.5f, startY = 0.5f))
            }
            LottieMood.BATHING -> {
                particles.add(FloatingParticle(emoji = "🫧", startX = 0.4f, startY = 0.6f))
                particles.add(FloatingParticle(emoji = "🫧", startX = 0.6f, startY = 0.5f))
            }
            LottieMood.BRUSHING -> {
                particles.add(FloatingParticle(emoji = "✨", startX = 0.5f, startY = 0.45f))
            }
            LottieMood.SLEEPING -> {
                particles.add(FloatingParticle(emoji = "💤", startX = 0.65f, startY = 0.3f))
            }
            else -> {}
        }
    }

    // Auto clean particles
    LaunchedEffect(particles.size) {
        if (particles.isNotEmpty()) {
            delay(1600)
            if (particles.isNotEmpty()) {
                particles.removeAt(0)
            }
        }
    }

    Box(
        modifier = modifier
            .testTag("lottie_character_view")
            .pointerInput(state.isSleeping) {
                detectTapGestures { offset ->
                    val relativeY = offset.y / size.height
                    val relativeX = offset.x / size.width

                    if (state.isSleeping) {
                        onPetHead()
                        return@detectTapGestures
                    }

                    when {
                        // Head area
                        relativeY < 0.42f -> onPetHead()
                        // Nose area (center of head)
                        relativeY in 0.40f..0.50f && relativeX in 0.42f..0.58f -> onTapNose()
                        // Belly area
                        relativeY in 0.50f..0.78f -> onTickleBelly()
                        // Feet / paws
                        else -> onTapFeet()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f + jiggleX.value
            val centerY = height / 2f + jumpY.value

            val scale = breatheScale

            // Draw shadow beneath Lottie
            drawOval(
                color = Color(0x25000000),
                topLeft = Offset(centerX - width * 0.28f, centerY + height * 0.38f),
                size = Size(width * 0.56f, height * 0.08f)
            )

            // Draw Ears
            val earRadius = width * 0.12f
            val earOffsetY = centerY - height * 0.28f * scale

            // Left Ear
            val leftEarX = centerX - width * 0.25f
            drawCircle(
                color = PandaBlack,
                radius = earRadius,
                center = Offset(leftEarX, earOffsetY + earWiggle)
            )
            drawCircle(
                color = PastelPink,
                radius = earRadius * 0.55f,
                center = Offset(leftEarX + 4f, earOffsetY + earWiggle + 4f)
            )

            // Right Ear
            val rightEarX = centerX + width * 0.25f
            drawCircle(
                color = PandaBlack,
                radius = earRadius,
                center = Offset(rightEarX, earOffsetY - earWiggle)
            )
            drawCircle(
                color = PastelPink,
                radius = earRadius * 0.55f,
                center = Offset(rightEarX - 4f, earOffsetY - earWiggle + 4f)
            )

            // Body
            val bodyWidth = width * 0.52f * scale
            val bodyHeight = height * 0.38f * scale
            val bodyTop = centerY - height * 0.02f

            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(PureWhite, Color(0xFFF0F0EE), Color(0xFFE2E2DC)),
                    center = Offset(centerX, bodyTop + bodyHeight * 0.4f),
                    radius = bodyWidth * 0.8f
                ),
                topLeft = Offset(centerX - bodyWidth / 2f, bodyTop),
                size = Size(bodyWidth, bodyHeight)
            )

            // Outfit if equipped
            drawEquippedOutfit(state.equippedOutfitId, centerX, bodyTop, bodyWidth, bodyHeight)

            // Belly squish highlight
            drawOval(
                color = Color(0x18000000),
                topLeft = Offset(centerX - bodyWidth * 0.35f, bodyTop + bodyHeight * 0.25f),
                size = Size(bodyWidth * 0.7f, bodyHeight * 0.6f),
                style = Stroke(width = 3f)
            )

            // Feet with cute pink pads
            val footRadiusX = width * 0.09f
            val footRadiusY = height * 0.06f
            val feetY = bodyTop + bodyHeight * 0.82f

            // Left Foot
            drawOval(
                color = PandaBlack,
                topLeft = Offset(centerX - bodyWidth * 0.44f, feetY),
                size = Size(footRadiusX * 2, footRadiusY * 2)
            )
            drawCircle(
                color = PastelPink,
                radius = footRadiusX * 0.45f,
                center = Offset(centerX - bodyWidth * 0.44f + footRadiusX, feetY + footRadiusY * 1.1f)
            )

            // Right Foot
            drawOval(
                color = PandaBlack,
                topLeft = Offset(centerX + bodyWidth * 0.44f - footRadiusX * 2, feetY),
                size = Size(footRadiusX * 2, footRadiusY * 2)
            )
            drawCircle(
                color = PastelPink,
                radius = footRadiusX * 0.45f,
                center = Offset(centerX + bodyWidth * 0.44f - footRadiusX, feetY + footRadiusY * 1.1f)
            )

            // Arms / Paws
            val armWidth = width * 0.12f
            val armHeight = height * 0.20f
            val armsY = bodyTop + height * 0.02f

            // Left Arm
            drawRoundRect(
                color = PandaBlack,
                topLeft = Offset(centerX - bodyWidth * 0.52f, armsY),
                size = Size(armWidth, armHeight),
                cornerRadius = CornerRadius(armWidth / 2f, armWidth / 2f)
            )
            // Right Arm (waves if HAPPY or TALKING)
            val rightArmAngle = if (mood == LottieMood.TALKING || mood == LottieMood.HAPPY) earWiggle * 2f else 0f
            drawRoundRect(
                color = PandaBlack,
                topLeft = Offset(centerX + bodyWidth * 0.52f - armWidth, armsY + rightArmAngle),
                size = Size(armWidth, armHeight),
                cornerRadius = CornerRadius(armWidth / 2f, armWidth / 2f)
            )

            // Head (cute large baby proportions)
            val headWidth = width * 0.68f * scale
            val headHeight = height * 0.38f * scale
            val headTop = centerY - height * 0.32f

            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(PureWhite, Color(0xFFFCFCFA), Color(0xFFEBEBE4)),
                    center = Offset(centerX, headTop + headHeight * 0.45f),
                    radius = headWidth * 0.7f
                ),
                topLeft = Offset(centerX - headWidth / 2f, headTop),
                size = Size(headWidth, headHeight)
            )

            // Black eye patches (characteristic cute teardrop/oval tilted panda patches)
            val patchWidth = headWidth * 0.28f
            val patchHeight = headHeight * 0.32f
            val patchY = headTop + headHeight * 0.32f

            // Left eye patch
            drawOval(
                color = PandaBlack,
                topLeft = Offset(centerX - headWidth * 0.38f, patchY),
                size = Size(patchWidth, patchHeight)
            )

            // Right eye patch
            drawOval(
                color = PandaBlack,
                topLeft = Offset(centerX + headWidth * 0.38f - patchWidth, patchY),
                size = Size(patchWidth, patchHeight)
            )

            // Blushing Cheeks
            val cheekRadius = headWidth * 0.08f
            val cheekY = headTop + headHeight * 0.62f
            drawCircle(
                color = Color(0x66FF80AB),
                radius = cheekRadius,
                center = Offset(centerX - headWidth * 0.34f, cheekY)
            )
            drawCircle(
                color = Color(0x66FF80AB),
                radius = cheekRadius,
                center = Offset(centerX + headWidth * 0.34f, cheekY)
            )

            // Eyes rendering based on mood
            val leftEyeCenter = Offset(centerX - headWidth * 0.24f, patchY + patchHeight * 0.5f)
            val rightEyeCenter = Offset(centerX + headWidth * 0.24f, patchY + patchHeight * 0.5f)
            val eyeRadius = patchWidth * 0.32f

            drawEyes(mood, state.isSleeping, leftEyeCenter, rightEyeCenter, eyeRadius)

            // Cute Nose
            val noseY = headTop + headHeight * 0.60f
            val nosePath = Path().apply {
                moveTo(centerX, noseY + headHeight * 0.05f)
                lineTo(centerX - headWidth * 0.045f, noseY)
                lineTo(centerX + headWidth * 0.045f, noseY)
                close()
            }
            drawPath(nosePath, color = PandaBlack)
            // Nose highlight
            drawCircle(
                color = Color(0x99FFFFFF),
                radius = 3.5f,
                center = Offset(centerX - 3f, noseY + 4f)
            )

            // Mouth
            drawMouth(mood, centerX, noseY + headHeight * 0.065f, headWidth, mouthFlap)

            // Accessories: Hat & Glasses
            drawEquippedAccessories(state.equippedHatId, state.equippedAccessoryId, centerX, headTop, headWidth, headHeight)
        }

        // Render Floating Emojis / Particles
        particles.forEach { particle ->
            TextParticle(particle = particle)
        }
    }
}

private fun DrawScope.drawEyes(
    mood: LottieMood,
    isSleeping: Boolean,
    leftEye: Offset,
    rightEye: Offset,
    radius: Float
) {
    if (isSleeping || mood == LottieMood.SLEEPING) {
        // Closed curved peaceful sleeping eyes
        val eyePathLeft = Path().apply {
            moveTo(leftEye.x - radius, leftEye.y)
            quadraticTo(leftEye.x, leftEye.y + radius * 0.9f, leftEye.x + radius, leftEye.y)
        }
        val eyePathRight = Path().apply {
            moveTo(rightEye.x - radius, rightEye.y)
            quadraticTo(rightEye.x, rightEye.y + radius * 0.9f, rightEye.x + radius, rightEye.y)
        }
        drawPath(eyePathLeft, color = PureWhite, style = Stroke(width = 5f, cap = StrokeCap.Round))
        drawPath(eyePathRight, color = PureWhite, style = Stroke(width = 5f, cap = StrokeCap.Round))
        return
    }

    when (mood) {
        LottieMood.TICKLED -> {
            // Squinting laughing eyes
            val eyePathLeft = Path().apply {
                moveTo(leftEye.x - radius, leftEye.y + radius * 0.3f)
                quadraticTo(leftEye.x, leftEye.y - radius * 0.8f, leftEye.x + radius, leftEye.y + radius * 0.3f)
            }
            val eyePathRight = Path().apply {
                moveTo(rightEye.x - radius, rightEye.y + radius * 0.3f)
                quadraticTo(rightEye.x, rightEye.y - radius * 0.8f, rightEye.x + radius, rightEye.y + radius * 0.3f)
            }
            drawPath(eyePathLeft, color = PureWhite, style = Stroke(width = 6f, cap = StrokeCap.Round))
            drawPath(eyePathRight, color = PureWhite, style = Stroke(width = 6f, cap = StrokeCap.Round))
        }
        LottieMood.LOVED -> {
            // Heart-shaped eyes
            drawHeartEye(leftEye, radius * 1.1f, Color(0xFFFF4081))
            drawHeartEye(rightEye, radius * 1.1f, Color(0xFFFF4081))
        }
        else -> {
            // Big sparkling baby eyes
            drawCircle(color = PureWhite, radius = radius, center = leftEye)
            drawCircle(color = PureWhite, radius = radius, center = rightEye)

            // Pupils
            drawCircle(color = Color(0xFF3E2723), radius = radius * 0.8f, center = leftEye)
            drawCircle(color = Color(0xFF3E2723), radius = radius * 0.8f, center = rightEye)

            // Sparkle Highlights (large & small)
            drawCircle(color = PureWhite, radius = radius * 0.35f, center = Offset(leftEye.x - radius * 0.25f, leftEye.y - radius * 0.25f))
            drawCircle(color = PureWhite, radius = radius * 0.16f, center = Offset(leftEye.x + radius * 0.3f, leftEye.y + radius * 0.3f))

            drawCircle(color = PureWhite, radius = radius * 0.35f, center = Offset(rightEye.x - radius * 0.25f, rightEye.y - radius * 0.25f))
            drawCircle(color = PureWhite, radius = radius * 0.16f, center = Offset(rightEye.x + radius * 0.3f, rightEye.y + radius * 0.3f))
        }
    }
}

private fun DrawScope.drawHeartEye(center: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y + size * 0.6f)
        cubicTo(
            center.x - size, center.y,
            center.x - size * 0.8f, center.y - size * 0.8f,
            center.x, center.y - size * 0.3f
        )
        cubicTo(
            center.x + size * 0.8f, center.y - size * 0.8f,
            center.x + size, center.y,
            center.x, center.y + size * 0.6f
        )
    }
    drawPath(path, color = color)
}

private fun DrawScope.drawMouth(
    mood: LottieMood,
    centerX: Float,
    mouthY: Float,
    headWidth: Float,
    flap: Float
) {
    val mouthWidth = headWidth * 0.14f

    when (mood) {
        LottieMood.TICKLED, LottieMood.TALKING -> {
            // Open laughing or talking mouth (D-shaped happy smile with clipped cute tongue inside)
            val openDepth = headWidth * (0.08f + 0.08f * flap)
            val mouthPath = Path().apply {
                moveTo(centerX - mouthWidth, mouthY)
                quadraticTo(
                    centerX,
                    mouthY + headWidth * 0.015f,
                    centerX + mouthWidth,
                    mouthY
                )
                quadraticTo(
                    centerX,
                    mouthY + openDepth,
                    centerX - mouthWidth,
                    mouthY
                )
                close()
            }
            // Mouth cavity
            drawPath(mouthPath, color = Color(0xFF7A1C33))
            // Clipped cute pink tongue strictly inside the mouth cavity
            clipPath(mouthPath) {
                drawOval(
                    color = Color(0xFFFF8FAB),
                    topLeft = Offset(
                        centerX - mouthWidth * 0.52f,
                        mouthY + openDepth * 0.24f
                    ),
                    size = Size(mouthWidth * 1.04f, openDepth * 0.55f)
                )
            }
            // Crisp mouth outline
            drawPath(
                mouthPath,
                color = PandaBlack,
                style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
        LottieMood.CHEWING -> {
            val chewHeight = headWidth * 0.055f * flap
            drawOval(
                color = Color(0xFF7A1C33),
                topLeft = Offset(centerX - mouthWidth * 0.65f, mouthY),
                size = Size(mouthWidth * 1.3f, chewHeight)
            )
            drawOval(
                color = PandaBlack,
                topLeft = Offset(centerX - mouthWidth * 0.65f, mouthY),
                size = Size(mouthWidth * 1.3f, chewHeight),
                style = Stroke(width = 3f)
            )
        }
        LottieMood.SLEEPING -> {
            val path = Path().apply {
                moveTo(centerX - mouthWidth * 0.4f, mouthY)
                quadraticTo(centerX, mouthY + headWidth * 0.03f, centerX + mouthWidth * 0.4f, mouthY)
            }
            drawPath(path, color = PandaBlack, style = Stroke(width = 4f, cap = StrokeCap.Round))
        }
        else -> {
            // Sweet baby panda smile (W curve)
            val path = Path().apply {
                moveTo(centerX - mouthWidth, mouthY)
                quadraticTo(centerX - mouthWidth * 0.5f, mouthY + headWidth * 0.04f, centerX, mouthY + 2f)
                quadraticTo(centerX + mouthWidth * 0.5f, mouthY + headWidth * 0.04f, centerX + mouthWidth, mouthY)
            }
            drawPath(path, color = PandaBlack, style = Stroke(width = 4.5f, cap = StrokeCap.Round))
        }
    }
}

private fun DrawScope.drawEquippedOutfit(
    outfitId: String?,
    centerX: Float,
    bodyTop: Float,
    bodyWidth: Float,
    bodyHeight: Float
) {
    if (outfitId == null) return

    when (outfitId) {
        "mint_romper" -> {
            drawRoundRect(
                color = Color(0xFFA5D6A7),
                topLeft = Offset(centerX - bodyWidth * 0.42f, bodyTop + bodyHeight * 0.22f),
                size = Size(bodyWidth * 0.84f, bodyHeight * 0.65f),
                cornerRadius = CornerRadius(24f, 24f)
            )
            // Cute tiny pocket
            drawRoundRect(
                color = Color(0xFF81C784),
                topLeft = Offset(centerX - bodyWidth * 0.16f, bodyTop + bodyHeight * 0.45f),
                size = Size(bodyWidth * 0.32f, bodyHeight * 0.22f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            // Romper straps
            drawRoundRect(color = Color(0xFF66BB6A), topLeft = Offset(centerX - bodyWidth * 0.32f, bodyTop + bodyHeight * 0.06f), size = Size(bodyWidth * 0.12f, bodyHeight * 0.25f), cornerRadius = CornerRadius(8f, 8f))
            drawRoundRect(color = Color(0xFF66BB6A), topLeft = Offset(centerX + bodyWidth * 0.20f, bodyTop + bodyHeight * 0.06f), size = Size(bodyWidth * 0.12f, bodyHeight * 0.25f), cornerRadius = CornerRadius(8f, 8f))
        }
        "star_pajamas" -> {
            drawRoundRect(
                color = Color(0xFFCE93D8),
                topLeft = Offset(centerX - bodyWidth * 0.44f, bodyTop + bodyHeight * 0.15f),
                size = Size(bodyWidth * 0.88f, bodyHeight * 0.72f),
                cornerRadius = CornerRadius(26f, 26f)
            )
            // Tiny painted stars
            drawCircle(color = GoldenCoin, radius = 6f, center = Offset(centerX - bodyWidth * 0.2f, bodyTop + bodyHeight * 0.4f))
            drawCircle(color = GoldenCoin, radius = 5f, center = Offset(centerX + bodyWidth * 0.18f, bodyTop + bodyHeight * 0.35f))
            drawCircle(color = GoldenCoin, radius = 7f, center = Offset(centerX, bodyTop + bodyHeight * 0.58f))
        }
        "striped_overalls" -> {
            drawRoundRect(
                color = Color(0xFF90CAF9),
                topLeft = Offset(centerX - bodyWidth * 0.42f, bodyTop + bodyHeight * 0.26f),
                size = Size(bodyWidth * 0.84f, bodyHeight * 0.60f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            // Yellow buttons
            drawCircle(color = GoldenCoin, radius = 7f, center = Offset(centerX - bodyWidth * 0.24f, bodyTop + bodyHeight * 0.30f))
            drawCircle(color = GoldenCoin, radius = 7f, center = Offset(centerX + bodyWidth * 0.24f, bodyTop + bodyHeight * 0.30f))
        }
        "pink_vest" -> {
            drawRoundRect(
                color = Color(0xFFF48FB1),
                topLeft = Offset(centerX - bodyWidth * 0.40f, bodyTop + bodyHeight * 0.12f),
                size = Size(bodyWidth * 0.80f, bodyHeight * 0.68f),
                cornerRadius = CornerRadius(22f, 22f)
            )
            // Zipper line
            drawLine(color = PureWhite, start = Offset(centerX, bodyTop + bodyHeight * 0.15f), end = Offset(centerX, bodyTop + bodyHeight * 0.78f), strokeWidth = 4f)
        }
        "fairy_cape" -> {
            drawOval(
                brush = Brush.radialGradient(listOf(Color(0xFFFFD54F), Color(0xFFFFAB40), Color(0x00FFAB40))),
                topLeft = Offset(centerX - bodyWidth * 0.55f, bodyTop + bodyHeight * 0.10f),
                size = Size(bodyWidth * 1.1f, bodyHeight * 0.8f)
            )
        }
    }
}

private fun DrawScope.drawEquippedAccessories(
    hatId: String?,
    accId: String?,
    centerX: Float,
    headTop: Float,
    headWidth: Float,
    headHeight: Float
) {
    // Hats
    when (hatId) {
        "flower_clip" -> {
            // Cherry blossom flower on right ear
            val flowerX = centerX + headWidth * 0.30f
            val flowerY = headTop + headHeight * 0.08f
            for (i in 0 until 5) {
                val angle = (i * 72f) * (Math.PI.toFloat() / 180f)
                val petalX = flowerX + sin(angle) * 16f
                val petalY = flowerY + kotlin.math.cos(angle) * 16f
                drawCircle(color = PastelPink, radius = 11f, center = Offset(petalX, petalY))
            }
            drawCircle(color = GoldenCoin, radius = 7f, center = Offset(flowerX, flowerY))
        }
        "pink_bow" -> {
            val bowX = centerX
            val bowY = headTop + headHeight * 0.02f
            val leftBow = Path().apply {
                moveTo(bowX, bowY)
                lineTo(bowX - 26f, bowY - 16f)
                lineTo(bowX - 26f, bowY + 16f)
                close()
            }
            val rightBow = Path().apply {
                moveTo(bowX, bowY)
                lineTo(bowX + 26f, bowY - 16f)
                lineTo(bowX + 26f, bowY + 16f)
                close()
            }
            drawPath(leftBow, color = Color(0xFFF06292))
            drawPath(rightBow, color = Color(0xFFF06292))
            drawCircle(color = Color(0xFFE91E63), radius = 9f, center = Offset(bowX, bowY))
        }
        "royal_crown" -> {
            val crownX = centerX
            val crownY = headTop - headHeight * 0.05f
            val crownWidth = headWidth * 0.32f
            val crownHeight = headHeight * 0.22f
            val path = Path().apply {
                moveTo(crownX - crownWidth / 2f, crownY + crownHeight)
                lineTo(crownX - crownWidth / 2f, crownY)
                lineTo(crownX - crownWidth / 4f, crownY + crownHeight * 0.4f)
                lineTo(crownX, crownY - crownHeight * 0.1f)
                lineTo(crownX + crownWidth / 4f, crownY + crownHeight * 0.4f)
                lineTo(crownX + crownWidth / 2f, crownY)
                lineTo(crownX + crownWidth / 2f, crownY + crownHeight)
                close()
            }
            drawPath(path, color = GoldenCoin)
            drawCircle(color = Color(0xFFE91E63), radius = 5f, center = Offset(crownX, crownY + crownHeight * 0.5f))
        }
        "nightcap" -> {
            val capX = centerX - headWidth * 0.15f
            val capY = headTop - headHeight * 0.06f
            val path = Path().apply {
                moveTo(capX - headWidth * 0.25f, capY + headHeight * 0.16f)
                lineTo(capX + headWidth * 0.20f, capY + headHeight * 0.16f)
                lineTo(capX + headWidth * 0.42f, capY - headHeight * 0.06f)
                close()
            }
            drawPath(path, color = Color(0xFF90CAF9))
            // Pompom
            drawCircle(color = PureWhite, radius = 14f, center = Offset(capX + headWidth * 0.42f, capY - headHeight * 0.06f))
        }
        "detective_hat" -> {
            val hatY = headTop + headHeight * 0.05f
            drawOval(color = Color(0xFF8D6E63), topLeft = Offset(centerX - headWidth * 0.35f, hatY), size = Size(headWidth * 0.7f, headHeight * 0.14f))
            drawRoundRect(color = Color(0xFF6D4C41), topLeft = Offset(centerX - headWidth * 0.22f, hatY - headHeight * 0.12f), size = Size(headWidth * 0.44f, headHeight * 0.18f), cornerRadius = CornerRadius(14f, 14f))
        }
        "chef_hat" -> {
            val hatY = headTop - headHeight * 0.10f
            drawCircle(color = PureWhite, radius = headWidth * 0.22f, center = Offset(centerX, hatY))
            drawRoundRect(color = Color(0xFFECEFF1), topLeft = Offset(centerX - headWidth * 0.20f, hatY + headHeight * 0.08f), size = Size(headWidth * 0.40f, headHeight * 0.10f), cornerRadius = CornerRadius(6f, 6f))
        }
    }

    // Accessories
    when (accId) {
        "heart_glasses" -> {
            val glassesY = headTop + headHeight * 0.48f
            val glassRadius = headWidth * 0.13f
            drawHeartGlassesLens(Offset(centerX - headWidth * 0.22f, glassesY), glassRadius)
            drawHeartGlassesLens(Offset(centerX + headWidth * 0.22f, glassesY), glassRadius)
            drawLine(color = Color(0xFFFF4081), start = Offset(centerX - headWidth * 0.08f, glassesY), end = Offset(centerX + headWidth * 0.08f, glassesY), strokeWidth = 5f)
        }
        "golden_bell" -> {
            val neckY = headTop + headHeight * 0.94f
            // Red ribbon
            drawLine(color = Color(0xFFD32F2F), start = Offset(centerX - headWidth * 0.25f, neckY), end = Offset(centerX + headWidth * 0.25f, neckY), strokeWidth = 6f)
            // Bell
            drawCircle(color = GoldenCoin, radius = 13f, center = Offset(centerX, neckY + 8f))
            drawCircle(color = Color(0xFF5D4037), radius = 3.5f, center = Offset(centerX, neckY + 12f))
        }
        "cute_pacifier" -> {
            val pacifierY = headTop + headHeight * 0.68f
            drawCircle(color = Color(0xFF80DEEA), radius = headWidth * 0.08f, center = Offset(centerX, pacifierY))
            drawCircle(color = PureWhite, radius = headWidth * 0.045f, center = Offset(centerX, pacifierY))
        }
        "rainbow_scarf" -> {
            val scarfY = headTop + headHeight * 0.90f
            drawRoundRect(color = Color(0xFFFF8A80), topLeft = Offset(centerX - headWidth * 0.28f, scarfY), size = Size(headWidth * 0.56f, headHeight * 0.14f), cornerRadius = CornerRadius(16f, 16f))
            drawRoundRect(color = Color(0xFFB388FF), topLeft = Offset(centerX - headWidth * 0.18f, scarfY + headHeight * 0.08f), size = Size(headWidth * 0.20f, headHeight * 0.16f), cornerRadius = CornerRadius(10f, 10f))
        }
    }
}

private fun DrawScope.drawHeartGlassesLens(center: Offset, size: Float) {
    val path = Path().apply {
        moveTo(center.x, center.y + size * 0.7f)
        cubicTo(center.x - size, center.y, center.x - size * 0.8f, center.y - size * 0.8f, center.x, center.y - size * 0.3f)
        cubicTo(center.x + size * 0.8f, center.y - size * 0.8f, center.x + size, center.y, center.x, center.y + size * 0.7f)
    }
    drawPath(path, color = Color(0x66FF4081))
    drawPath(path, color = Color(0xFFFF4081), style = Stroke(width = 4.5f))
}

@Composable
private fun TextParticle(particle: FloatingParticle) {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(particle.id) {
        launch {
            offsetY.animateTo(-120f, tween(1400, easing = FastOutSlowInEasing))
        }
        launch {
            delay(800)
            alpha.animateTo(0f, tween(600))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = particle.emoji,
            fontSize = 28.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(alpha.value)
                .offset(
                    x = particle.offsetX.dp,
                    y = (offsetY.value).dp
                )
        )
    }
}
