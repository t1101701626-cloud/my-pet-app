package com.example.ui.screens

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.model.GameCatalogs
import com.example.model.LottieMood
import com.example.model.LottiePetState
import com.example.model.WardrobeCategory
import com.example.model.WardrobeItem
import com.example.ui.components.LottiePandaView
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.GoldenCoin
import com.example.ui.theme.PastelPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark
import com.example.ui.theme.StarYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BedroomScreen(
    state: LottiePetState,
    mood: LottieMood,
    onToggleSleep: () -> Unit,
    onPetHead: () -> Unit,
    onBuyOrEquipWardrobeItem: (WardrobeItem) -> Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var showClosetSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(WardrobeCategory.HATS) }
    var wardrobeMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        // Bedroom background edge-to-edge
        Image(
            painter = painterResource(id = R.drawable.img_home_room),
            contentDescription = "Dormitorio de Lottie",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Night / Dark overlay when sleeping
        if (state.isSleeping) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xDD0D1B2A),
                                Color(0xCC1A237E),
                                Color(0xEE0D1B2A)
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Status Banner (+ stars when sleeping, placed safely inside contentPadding)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 520.dp)
            ) {
                if (state.isSleeping) {
                    Text(
                        text = "✨ 🌟 🌙 💫",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.isSleeping) Color(0xDD1E293B) else Color(0xEEFFFFFF)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (state.isSleeping) "Lottie está durmiendo plácidamente... Zzz 💤 (Toca para despertar)"
                        else "¡Hora de descansar o probarte ropita nueva en el clóset!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (state.isSleeping) StarYellow else SoftTextDark,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Grounded Panda Stage (no colored box behind panda)
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                val pandaSize = min(maxWidth * 0.78f, maxHeight * 0.94f).coerceIn(130.dp, 300.dp)

                LottiePandaView(
                    state = state,
                    mood = mood,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                        .size(pandaSize),
                    onPetHead = onPetHead
                )
            }

            // Bottom Actions: Lamp / Sleep Switch & Wardrobe Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 560.dp)
                    .clip(RoundedCornerShape(24.dp)),
                color = if (state.isSleeping) Color(0xE61E293B) else Color(0xF5FFFFFF),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sleep / Wake Lamp Button
                    Button(
                        onClick = onToggleSleep,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isSleeping) Color(0xFFFFCA28) else Color(0xFF5C6BC0)
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .testTag("sleep_lamp_button")
                    ) {
                        Text(
                            text = if (state.isSleeping) "☀️ Despertar" else "💡 Apagar Luz",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            color = if (state.isSleeping) SoftTextDark else PureWhite
                        )
                    }

                    // Wardrobe / Closet Button
                    Button(
                        onClick = { showClosetSheet = true },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PastelPink),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .testTag("open_wardrobe_button")
                    ) {
                        Text(
                            text = "👗 Clóset",
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

        // Wardrobe Bottom Sheet Modal (Talking Tom dress-up style)
        if (showClosetSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showClosetSheet = false
                    wardrobeMessage = null
                },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color(0xFFFFFDF7)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🛍️ Vestidor de ${state.name}",
                            fontSize = 19.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.ExtraBold,
                            color = SoftTextDark,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFFF8E1))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🪙 ${state.bambooCoins}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57F17)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Wardrobe Category Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WardrobeCategory.entries.forEach { cat ->
                            val isSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 42.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) BambooGreenPrimary else Color(0xFFEEEEEE)
                                    )
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 6.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat.displayName,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) PureWhite else SoftTextDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Feedback message
                    if (wardrobeMessage != null) {
                        Text(
                            text = wardrobeMessage ?: "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD32F2F),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Wardrobe Items Grid
                    val currentItems = GameCatalogs.wardrobe.filter { it.category == selectedCategory }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {
                        items(currentItems) { item ->
                            val isUnlocked = state.unlockedItemIds.contains(item.id)
                            val isEquipped = when (item.category) {
                                WardrobeCategory.HATS -> state.equippedHatId == item.id
                                WardrobeCategory.OUTFITS -> state.equippedOutfitId == item.id
                                WardrobeCategory.ACCESSORIES -> state.equippedAccessoryId == item.id
                            }

                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isEquipped) Color(0xFFE8F5E9) else Color(0xFFF9F9F9)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isEquipped) 2.5.dp else 1.dp,
                                    color = if (isEquipped) BambooGreenPrimary else Color(0xFFE0E0E0)
                                ),
                                modifier = Modifier
                                    .clickable {
                                        val success = onBuyOrEquipWardrobeItem(item)
                                        if (!success) {
                                            wardrobeMessage = "¡Te faltan monedas! Gana monedas jugando 🎋"
                                        } else {
                                            wardrobeMessage = null
                                        }
                                    }
                                    .testTag("wardrobe_item_${item.id}")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = item.emoji, fontSize = 30.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = SoftTextDark
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                when {
                                                    isEquipped -> BambooGreenPrimary
                                                    isUnlocked -> Color(0xFF81C784)
                                                    else -> GoldenCoin
                                                }
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = when {
                                                isEquipped -> "Puesto"
                                                isUnlocked -> "Usar"
                                                else -> "🪙 ${item.price}"
                                            },
                                            fontSize = 11.sp,
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
    }
}
