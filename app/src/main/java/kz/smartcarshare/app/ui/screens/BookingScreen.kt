package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.Tariff
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

@Composable
fun BookingScreen(
    vm: RentalViewModel,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    val car = vm.selectedCar ?: return
    val lang = vm.currentLang
    val tariffLabel = if (vm.tariff == Tariff.HOUR) Strings.get(lang, "hourly") else Strings.get(lang, "daily")

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(
            title = if (lang == Lang.KZ) "Брондауды растау" else "Подтверждение бронирования",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                Text(Strings.get(lang, "selected_car"), style = MaterialTheme.typography.bodySmall, color = Amber)
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                        .background(Surface1, RoundedCornerShape(18.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (car.displayImageUrl.isNotBlank()) {
                        val context = LocalContext.current
                        Box(
                            modifier = Modifier
                                .size(width = 76.dp, height = 54.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Surface2, RoundedCornerShape(12.dp))
                                .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
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
                        }
                    } else {
                        IconBadge(
                            if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                            bg = Ice,
                            tint = AmberOnDark
                        )
                    }
                    Column {
                        Text(car.name, style = MaterialTheme.typography.titleMedium, color = TextHi)
                        Spacer(Modifier.height(5.dp))
                        Chip(tariffLabel)
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(Surface1, RoundedCornerShape(18.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Text(Strings.get(lang, "pre_calculation"), style = MaterialTheme.typography.bodySmall, color = Amber)
                    SummaryRow(Strings.get(lang, "tariff"), tariffLabel)
                    val unit = if (vm.tariff == Tariff.HOUR) (if (lang == Lang.KZ) "/ сағат" else "/ час") else (if (lang == Lang.KZ) "/ тәулік" else "/ сутки")
                    SummaryRow(Strings.get(lang, "estimated_price"), "${formatTenge(vm.currentPrice())} $unit")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy800)
                .padding(20.dp)
        ) {
            PrimaryButton(
                text = if (lang == Lang.KZ) "Брондауды растау" else "Подтвердить бронирование",
                onClick = onConfirm
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextMid)
        Text(value, style = MaterialTheme.typography.labelLarge, color = TextHi)
    }
}
