package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.PastelPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftLilac
import com.example.ui.theme.SoftTextDark

enum class MainSection(
    val title: String,
    val iconEmoji: String,
    val activeColor: Color
) {
    SALA("Sala", "🏠", BambooGreenPrimary),
    COMER("Comer", "🍽️", Color(0xFFFF9800)),
    BANO("Baño", "🛁", Color(0xFF03A9F4)),
    DORMIR("Dormir", "🛏️", Color(0xFF9C27B0)),
    HABITAT("Hábitat", "🎋", Color(0xFF4CAF50))
}

@Composable
fun BottomNav(
    currentSection: MainSection,
    onSectionSelected: (MainSection) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_navigation_bar"),
        color = Color(0xF2FFFFFF),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainSection.entries.forEach { section ->
                val isSelected = currentSection == section
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.15f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "scale_${section.name}"
                )
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) section.activeColor.copy(alpha = 0.18f) else Color.Transparent,
                    label = "bg_${section.name}"
                )

                Column(
                    modifier = Modifier
                        .scale(scale)
                        .clip(RoundedCornerShape(20.dp))
                        .background(backgroundColor)
                        .clickable { onSectionSelected(section) }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("nav_${section.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) section.activeColor else Color(0xFFF0F0F0)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = section.iconEmoji,
                            fontSize = 20.sp
                        )
                    }

                    Text(
                        text = section.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) section.activeColor else SoftTextDark,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
