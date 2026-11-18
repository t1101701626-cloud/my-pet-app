package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.model.FoodItem
import com.example.model.GameCatalogs
import com.example.model.LottieMood
import com.example.model.LottiePetState
import com.example.ui.components.LottiePandaView
import com.example.ui.theme.BambooGreenPrimary
import com.example.ui.theme.GoldenCoin
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftTextDark
import kotlinx.coroutines.launch

@Composable
fun KitchenScreen(
    state: LottiePetState,
    mood: LottieMood,
    onFeedFood: (FoodItem) -> Boolean,
    onPetHead: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val foods = GameCatalogs.foods
    var selectedFoodIndex by remember { mutableIntStateOf(0) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val currentFood = foods[selectedFoodIndex]

    Box(modifier = modifier.fillMaxSize()) {
        // Kitchen background edge-to-edge
        Image(
            painter = painterResource(id = R.drawable.img_kitchen),
            contentDescription = "Cocina de Lottie",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Soft warm ambient tint
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
            // Cheerful title / reaction banner
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xEEFFFFFF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 520.dp)
            ) {
                Text(
                    text = if (mood == LottieMood.CHEWING) "¡Ñam ñam ñam! ¡Qué rico está! 😋"
                    else "¡Hora de comer! Dale comidita a ${state.name} 🍓",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftTextDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Grounded Panda Stage (transparent directly over kitchen scene, no box behind panda)
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                val pandaSize = min(maxWidth * 0.76f, maxHeight * 0.94f).coerceIn(120.dp, 290.dp)

                LottiePandaView(
                    state = state,
                    mood = mood,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 2.dp)
                        .size(pandaSize),
                    onPetHead = onPetHead
                )
            }

            // Dining Table & Sliding Food Carousel
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 560.dp)
                    .clip(RoundedCornerShape(24.dp)),
                color = Color(0xF7FFFFFF),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Carousel controls row with side arrows
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (selectedFoodIndex > 0) {
                                    selectedFoodIndex--
                                    scope.launch { listState.animateScrollToItem(selectedFoodIndex) }
                                }
                            },
                            enabled = selectedFoodIndex > 0
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Plato anterior",
                                tint = if (selectedFoodIndex > 0) BambooGreenPrimary else Color.LightGray
                            )
                        }

                        // Selected Food Details
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = currentFood.name,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = SoftTextDark
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (currentFood.price == 0) "Gratis" else "🪙 ${currentFood.price}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentFood.price == 0) BambooGreenPrimary else GoldenCoin
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "+${currentFood.hungerRestore.toInt()} Hambre",
                                    fontSize = 12.sp,
                                    color = Color(0xFF66BB6A)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                if (selectedFoodIndex < foods.size - 1) {
                                    selectedFoodIndex++
                                    scope.launch { listState.animateScrollToItem(selectedFoodIndex) }
                                }
                            },
                            enabled = selectedFoodIndex < foods.size - 1
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Plato siguiente",
                                tint = if (selectedFoodIndex < foods.size - 1) BambooGreenPrimary else Color.LightGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Horizontal Food Items Carousel
                    LazyRow(
                        state = listState,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(foods) { index, food ->
                            val isSelected = index == selectedFoodIndex
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        if (isSelected) Color(0xFFFFE082) else Color(0xFFF5F5F5)
                                    )
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) GoldenCoin else Color(0xFFE0E0E0),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .clickable { selectedFoodIndex = index }
                                    .testTag("food_item_${food.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = food.emoji, fontSize = 28.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Feed Button
                    Button(
                        onClick = {
                            val success = onFeedFood(currentFood)
                            if (!success) {
                                snackbarMessage = "¡Necesitas más monedas de bambú! Juega un minijuego 🎋"
                            } else {
                                snackbarMessage = null
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .testTag("feed_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF9800)
                        )
                    ) {
                        Text(
                            text = "Dar ${currentFood.name} ${currentFood.emoji}",
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }

                    // Not enough coins feedback
                    if (snackbarMessage != null) {
                        Text(
                            text = snackbarMessage ?: "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFD32F2F),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
