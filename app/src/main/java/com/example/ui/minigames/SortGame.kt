package com.example.ui.minigames

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

data class SortItem(
    val id: Int,
    val emoji: String,
    val name: String,
    val targetColorGroup: String
)

data class SortBasket(
    val colorGroup: String,
    val name: String,
    val color: Color,
    val icon: String
)

@Composable
fun SortGame(
    onGameComplete: (coinsWon: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val baskets = listOf(
        SortBasket("ROJO", "Cesta Roja", Color(0xFFEF5350), "🧺"),
        SortBasket("AMARILLO", "Cesta Amarilla", Color(0xFFFFCA28), "🧺"),
        SortBasket("VERDE", "Cesta Verde", Color(0xFF66BB6A), "🧺"),
        SortBasket("AZUL", "Cesta Azul", Color(0xFF42A5F5), "🧺")
    )

    val allItems = remember {
        listOf(
            SortItem(1, "🍎", "Manzana", "ROJO"),
            SortItem(2, "🍌", "Banana", "AMARILLO"),
            SortItem(3, "🍃", "Hoja", "VERDE"),
            SortItem(4, "💧", "Gotita", "AZUL")
        )
    }

    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val sortedItems = remember { mutableStateListOf<SortItem>() }
    var hintText by remember { mutableStateOf("Toca la cesta del color correcto 🎨") }
    var isFinished by remember { mutableStateOf(false) }

    val currentItem = allItems.getOrNull(selectedItemIndex)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFE0F2F1)
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
                        text = "🎨 Clasificación de Colores",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00897B)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${sortedItems.size}/${allItems.size}",
                        fontWeight = FontWeight.Bold,
                        color = SoftTextDark
                    )
                }

                // Current item to sort
                if (currentItem != null && !isFinished) {
                    Card(
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Clasifica este objeto:", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = currentItem.emoji, fontSize = 50.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentItem.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftTextDark
                            )
                        }
                    }
                }

                Text(
                    text = hintText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftTextDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 4 Colored Baskets using weight(1f) so they never overflow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    baskets.forEach { basket ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = basket.color.copy(alpha = 0.25f)),
                            border = androidx.compose.foundation.BorderStroke(3.dp, basket.color),
                            modifier = Modifier
                                .weight(1f)
                                .height(92.dp)
                                .clickable {
                                    if (currentItem != null) {
                                        if (currentItem.targetColorGroup == basket.colorGroup) {
                                            sortedItems.add(currentItem)
                                            if (selectedItemIndex < allItems.size - 1) {
                                                selectedItemIndex++
                                                hintText = "¡Genial! ¡Coincide con la cesta! 🧺"
                                            } else {
                                                isFinished = true
                                                hintText = "¡Completaste toda la clasificación! 🎉"
                                                onGameComplete(20)
                                            }
                                        } else {
                                            hintText = "¡Mira bien los colores! ¿Cuál es igual? 👀"
                                        }
                                    }
                                }
                                .testTag("basket_${basket.colorGroup.lowercase()}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = basket.icon, fontSize = 30.sp)
                                Text(
                                    text = basket.colorGroup,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Black,
                                    color = basket.color
                                )
                            }
                        }
                    }
                }

                // Victory Card
                if (isFinished) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
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
                            Text(text = "🎉 ¡TODO CLASIFICADO! 🎉", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF00897B))
                            Text(text = "¡Ganaste +20 monedas de bambú!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BambooGreenPrimary)
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
