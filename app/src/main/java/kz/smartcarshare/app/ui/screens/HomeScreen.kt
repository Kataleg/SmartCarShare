package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

@Composable
fun HomeScreen(
    vm: RentalViewModel,
    currentUser: LocalUser? = null,
    onCarClick: (Int) -> Unit,
    onB2BClick: () -> Unit
) {
    val greetingText = if (!currentUser?.name.isNullOrBlank()) {
        "Сәлем, ${currentUser.name}"
    } else {
        "Сәлем!"
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(listOf(Surface1, Amber.copy(alpha = 0.12f))),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .border(1.dp, BorderColor, RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(greetingText, style = MaterialTheme.typography.bodySmall, color = TextMid)
                    Text(
                        "Қандай көлік іздеп жүрсіз?",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextHi,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        item {
            SectionTitle(title = "Жақын маңдағы көліктер", trailing = "${vm.cars.size} қолжетімді")
        }
        items(vm.cars) { car ->
            CarRow(car = car, onClick = { onCarClick(car.id) })
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Surface1, RoundedCornerShape(16.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                    .clickable { onB2BClick() }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconBadge(Icons.Filled.Apartment, bg = Ice.copy(alpha = 0.16f), tint = Ice, sizeDp = 38)
                Column(modifier = Modifier.weight(1f)) {
                    Text("Компанияңызға абонемент керек пе?", style = MaterialTheme.typography.labelLarge, color = TextHi)
                    Text("Қызметкерлерге арналған айлық тариф", style = MaterialTheme.typography.bodySmall, color = TextMid)
                }
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMid)
            }
        }
    }
}

@Composable
private fun CarRow(car: Car, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(Surface1, RoundedCornerShape(18.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconBadge(
            icon = if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
            bg = Ice,
            tint = AmberOnDark
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(car.name, style = MaterialTheme.typography.titleMedium, color = TextHi)
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(top = 5.dp)
            ) {
                car.tags.forEach { Chip(it) }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(formatTenge(car.pricePerHour), color = Ice, style = MaterialTheme.typography.titleMedium)
            Text("/ сағат", style = MaterialTheme.typography.bodySmall, color = TextMid)
        }
    }
}