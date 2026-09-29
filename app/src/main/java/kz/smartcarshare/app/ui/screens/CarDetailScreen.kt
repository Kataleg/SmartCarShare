package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
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
                        .height(160.dp)
                        .background(
                            Brush.linearGradient(listOf(Surface1, Ice.copy(alpha = 0.18f))),
                            RoundedCornerShape(22.dp)
                        )
                        .border(1.dp, BorderColor, RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                        contentDescription = null,
                        tint = Ice,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    car.tags.forEach { Chip(it, accent = true) }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SpecBox("Орын саны", car.seats, Modifier.weight(1f))
                    SpecBox("Беріліс қорабы", car.transmission, Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SpecBox("Отын түрі", car.fuel, Modifier.weight(1f))
                    SpecBox("Санат", if (car.isLux) "Lux" else "Стандарт", Modifier.weight(1f))
                }
            }
            item { SectionTitle(title = "Жалдау форматы") }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TariffOption(
                        label = "Сағаттық",
                        value = formatTenge(car.pricePerHour),
                        selected = vm.tariff == Tariff.HOUR,
                        modifier = Modifier.weight(1f)
                    ) { vm.changeTariff(Tariff.HOUR) }
                    TariffOption(
                        label = "Тәуліктік",
                        value = car.pricePerDay?.let { formatTenge(it) } ?: "—",
                        selected = vm.tariff == Tariff.DAY,
                        enabled = car.pricePerDay != null,
                        modifier = Modifier.weight(1f)
                    ) { vm.changeTariff(Tariff.DAY) }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(Surface1, RoundedCornerShape(18.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Icon(Icons.Filled.Shield, contentDescription = null, tint = Ice, modifier = Modifier.size(18.dp))
                        Text("Сақтандыру қосу", style = MaterialTheme.typography.labelLarge, color = TextHi)
                    }
                    Switch(
                        checked = vm.insuranceOn,
                        onCheckedChange = { vm.insuranceOn = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Amber,
                            checkedThumbColor = AmberOnDark
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy800)
                .padding(20.dp)
        ) {
            val unit = if (vm.tariff == Tariff.HOUR) "/сағ" else "/тәулік"
            PrimaryButton(
                text = "Жалдау — ${formatTenge(vm.currentPrice())} $unit",
                onClick = onRentClick
            )
        }
    }
}

@Composable
private fun SpecBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Surface1, RoundedCornerShape(14.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextLow)
        Text(value, style = MaterialTheme.typography.titleMedium, color = TextHi)
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
                if (selected) Amber.copy(alpha = 0.14f) else Surface1,
                RoundedCornerShape(14.dp)
            )
            .border(1.dp, if (selected) Amber else BorderColor, RoundedCornerShape(14.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextMid)
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            color = TextHi,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}