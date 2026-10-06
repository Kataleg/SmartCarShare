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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

@Composable
fun HomeScreen(
    vm: RentalViewModel,
    currentUser: LocalUser? = null,
    lang: Lang = Lang.KZ,
    onCarClick: (Int) -> Unit,
    onB2BClick: () -> Unit
) {
    val greetingText = if (!currentUser?.name.isNullOrBlank()) {
        if (lang == Lang.KZ) "Сәлем, ${currentUser.name}" else "Привет, ${currentUser.name}"
    } else {
        if (lang == Lang.KZ) "Сәлем!" else "Привет!"
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
                        Strings.get(lang, "home_greeting"),
                        style = MaterialTheme.typography.titleLarge,
                        color = TextHi,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        item {
            SectionTitle(title = Strings.get(lang, "nearby_cars"), trailing = "${vm.cars.size} ${Strings.get(lang, "available")}")
        }
        items(vm.cars) { car ->
            CarRow(car = car, lang = lang, onClick = { onCarClick(car.id) })
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
                    Text(Strings.get(lang, "b2b_prompt"), style = MaterialTheme.typography.labelLarge, color = TextHi)
                    Text(Strings.get(lang, "b2b_sub"), style = MaterialTheme.typography.bodySmall, color = TextMid)
                }
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMid)
            }
        }
    }
}

@Composable
private fun CarRow(car: Car, lang: Lang, onClick: () -> Unit) {
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
        if (car.imageUrl.isNotBlank()) {
            Box(
                modifier = Modifier
                    .size(width = 76.dp, height = 54.dp)
                    .background(Surface2, RoundedCornerShape(12.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = car.imageUrl,
                    contentDescription = car.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            IconBadge(
                icon = if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                bg = Ice,
                tint = AmberOnDark
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(car.name, style = MaterialTheme.typography.titleMedium, color = TextHi)
            Text(car.location, style = MaterialTheme.typography.bodySmall, color = TextLow)
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(top = 5.dp)
            ) {
                car.tags.forEach { Chip(it) }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(formatTenge(car.pricePerHour), color = Ice, style = MaterialTheme.typography.titleMedium)
            Text(if (lang == Lang.KZ) "/ сағат" else "/ час", style = MaterialTheme.typography.bodySmall, color = TextMid)
        }
    }
}
