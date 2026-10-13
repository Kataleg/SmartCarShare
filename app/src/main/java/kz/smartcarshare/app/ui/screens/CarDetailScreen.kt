package kz.smartcarshare.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
fun CarDetailScreen(
    vm: RentalViewModel,
    onBack: () -> Unit,
    onRentClick: () -> Unit
) {
    val car = vm.selectedCar ?: return
    val lang = vm.currentLang

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(title = car.name, onBack = onBack)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(listOf(Surface1, Ice.copy(alpha = 0.18f))),
                            RoundedCornerShape(22.dp)
                        )
                        .border(1.dp, BorderColor, RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (car.displayImageUrl.isNotBlank()) {
                        val context = LocalContext.current
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
                        Icon(
                            imageVector = if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                            contentDescription = null,
                            tint = Ice,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Ice, modifier = Modifier.size(16.dp))
                    Text(car.location, style = MaterialTheme.typography.bodySmall, color = Ice)
                }
            }
            item {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    car.tags.forEach { Chip(it, accent = true) }
                }
            }
            item {
                val context = LocalContext.current
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(Surface1, RoundedCornerShape(18.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (lang == Lang.KZ) "КӨЛІК ПАРКІ ЖӘНЕ БАЙЛАНЫС" else "АВТОПАРК И КОНТАКТЫ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Amber,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (car.company.contains("CashAuto", ignoreCase = true)) "CashAuto Rent Астана (24/7)" else car.company,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextHi,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(10.dp))

                    // Phone 1: Airport T1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+77779991112"))
                                context.startActivity(intent)
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Phone, contentDescription = null, tint = Ice, modifier = Modifier.size(18.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "+7 (777) 999-11-12",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Ice,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (lang == Lang.KZ) "Аэропорт Т1 (24/7)" else "Аэропорт Т1 (24/7)",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMid
                            )
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    // Phone 2: Zhaidarman 2/1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+77029981112"))
                                context.startActivity(intent)
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Phone, contentDescription = null, tint = Ice, modifier = Modifier.size(18.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "+7 (702) 998-11-12",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Ice,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (lang == Lang.KZ) "Жайдарман көшесі 2/1" else "ул. Жайдарман 2/1",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMid
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // 2GIS Map Link Button
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://2gis.kz/astana/geo/70000001054775158"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Amber),
                        border = BorderStroke(1.dp, Amber)
                    ) {
                        Icon(Icons.Filled.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (lang == Lang.KZ) "2GIS картадан орналасуды ашу" else "Открыть в 2GIS на карте",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SpecBox(Strings.get(lang, "seats"), car.displaySeats(lang), Modifier.weight(1f))
                    SpecBox(Strings.get(lang, "transmission"), car.displayTransmission(lang), Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SpecBox(Strings.get(lang, "fuel"), car.displayFuel(lang), Modifier.weight(1f))
                    SpecBox(Strings.get(lang, "category"), if (car.isLux) "Lux" else "Standard", Modifier.weight(1f))
                }
            }
            item { SectionTitle(title = Strings.get(lang, "rental_format")) }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TariffOption(
                        label = Strings.get(lang, "hourly"),
                        value = formatTenge(car.pricePerHour),
                        selected = vm.tariff == Tariff.HOUR,
                        modifier = Modifier.weight(1f)
                    ) { vm.changeTariff(Tariff.HOUR) }
                    TariffOption(
                        label = Strings.get(lang, "daily"),
                        value = car.pricePerDay?.let { formatTenge(it) } ?: "—",
                        selected = vm.tariff == Tariff.DAY,
                        enabled = car.pricePerDay != null,
                        modifier = Modifier.weight(1f)
                    ) { vm.changeTariff(Tariff.DAY) }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy800)
                .padding(20.dp)
        ) {
            val unit = if (vm.tariff == Tariff.HOUR) (if (lang == Lang.KZ) "/сағ" else "/час") else (if (lang == Lang.KZ) "/тәулік" else "/сутки")
            PrimaryButton(
                text = "${Strings.get(lang, "rent")} — ${formatTenge(vm.currentPrice())} $unit",
                onClick = onRentClick
            )
        }
    }
}

@Composable
private fun SpecBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Surface2, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextLow)
        Text(value, style = MaterialTheme.typography.titleMedium, color = TextHi, modifier = Modifier.padding(top = 2.dp))
    }
}

@Composable
private fun TariffOption(
    label: String,
    value: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                if (selected) Amber.copy(alpha = 0.18f) else Surface1,
                RoundedCornerShape(16.dp)
            )
            .border(1.5.dp, if (selected) Amber else BorderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (selected) Amber else TextLow,
                modifier = Modifier.size(16.dp)
            )
            Text(label, style = MaterialTheme.typography.bodySmall, color = if (selected) Amber else TextMid, fontWeight = FontWeight.Bold)
        }
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            color = TextHi,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
