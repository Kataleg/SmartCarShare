package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.components.BackTopBar
import kz.smartcarshare.app.ui.components.Chip
import kz.smartcarshare.app.ui.components.IconBadge
import kz.smartcarshare.app.ui.components.PrimaryButton
import kz.smartcarshare.app.ui.theme.*

@Composable
fun FavoritesScreen(
    vm: RentalViewModel,
    currentUser: LocalUser? = null,
    lang: Lang = Lang.KZ,
    onBack: () -> Unit,
    onCarClick: (Int) -> Unit
) {
    LaunchedEffect(currentUser) {
        if (!currentUser?.email.isNullOrBlank()) {
            vm.loadFavorites(currentUser.email)
        } else {
            vm.loadFavorites("guest_user")
        }
    }

    val favoriteCars = vm.favoriteCars

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(
            title = if (lang == Lang.KZ) "Таңдаулы көліктер" else "Избранные автомобили",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (lang == Lang.KZ) "Базада сақталған автокөліктер" else "Сохраненные в облаке авто",
                        style = MaterialTheme.typography.bodySmall,
                        color = Amber
                    )
                    Chip(text = "${favoriteCars.size} ${if (lang == Lang.KZ) "сақталды" else "сохранено"}", accent = true)
                }
            }

            if (favoriteCars.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconBadge(
                            icon = Icons.Filled.Favorite,
                            bg = ErrorColor.copy(alpha = 0.12f),
                            tint = ErrorColor,
                            sizeDp = 64
                        )
                        Text(
                            text = if (lang == Lang.KZ) "Таңдаулылар тізімі бос" else "Список избранного пуст",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextHi,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (lang == Lang.KZ) "Көлік карточкасындағы жүректі басып, таңдаулыларға қосыңыз" else "Нажмите на сердечко у любого авто, чтобы сохранить его в облаке",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMid,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        PrimaryButton(
                            text = if (lang == Lang.KZ) "Көліктерді қарау" else "Смотреть авто",
                            modifier = Modifier.width(200.dp),
                            onClick = onBack
                        )
                    }
                }
            }

            items(favoriteCars) { car ->
                FavoriteCarRow(
                    car = car,
                    lang = lang,
                    onClick = { onCarClick(car.id) },
                    onRemove = {
                        val userId = currentUser?.email ?: "guest_user"
                        vm.toggleFavorite(car.id, userId)
                    }
                )
            }
        }
    }
}

@Composable
private fun FavoriteCarRow(
    car: Car,
    lang: Lang,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = Surface1,
        border = BorderStroke(1.dp, BorderColor),
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 104.dp, height = 72.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Surface2)
                        .border(1.dp, BorderColor, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (car.displayImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(car.displayImageUrl)
                                .crossfade(true)
                                .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android)")
                                .build(),
                            contentDescription = car.name,
                            contentScale = ContentScale.Crop,
                            error = rememberVectorPainter(if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar),
                            placeholder = rememberVectorPainter(if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar),
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        IconBadge(
                            icon = if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                            bg = Ice.copy(alpha = 0.12f),
                            tint = Ice,
                            sizeDp = 44
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextHi,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = car.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextLow,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = formatTenge(car.pricePerHour) + (if (lang == Lang.KZ) " / сағат" else " / час"),
                        color = Ice,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Удалить из избранного",
                        tint = ErrorColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
