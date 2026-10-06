package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.FirestoreRental
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.components.BackTopBar
import kz.smartcarshare.app.ui.components.Chip
import kz.smartcarshare.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyTripsScreen(
    vm: RentalViewModel,
    currentUser: LocalUser? = null,
    lang: Lang = Lang.KZ,
    onBack: () -> Unit
) {
    LaunchedEffect(currentUser) {
        if (!currentUser?.email.isNullOrBlank()) {
            vm.loadTripHistory(currentUser.email)
        }
    }

    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(
            title = if (lang == Lang.KZ) "Сапарлар мен төлемдер тарихы" else "История поездок и платежей",
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
                Text(
                    text = if (lang == Lang.KZ) "Аяқталған сапарлар мен транзакциялар" else "Завершенные поездки и транзакции",
                    style = MaterialTheme.typography.bodySmall,
                    color = Amber
                )
            }

            if (vm.rentalActive) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Amber, RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface1)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(vm.selectedCar?.name ?: "Белсенді сапар", style = MaterialTheme.typography.titleMedium, color = TextHi)
                                Chip(if (lang == Lang.KZ) "Жүріп жатыр" else "Активно", accent = true)
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (lang == Lang.KZ) "Уақыт:" else "Время:", style = MaterialTheme.typography.bodySmall, color = TextMid)
                                Text(vm.elapsedFormatted(), style = MaterialTheme.typography.labelLarge, color = Ice)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (lang == Lang.KZ) "Ағымдағы сома:" else "Текущая сумма:", style = MaterialTheme.typography.bodySmall, color = TextMid)
                                Text(formatTenge(vm.liveAccruedPrice()), style = MaterialTheme.typography.titleMedium, color = Amber)
                            }
                        }
                    }
                }
            }

            if (vm.tripHistory.isEmpty() && !vm.rentalActive) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (lang == Lang.KZ) "Әзірге аяқталған сапарлар жоқ" else "Пока нет завершенных поездок",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMid
                        )
                    }
                }
            }

            items(vm.tripHistory) { trip ->
                TripRow(trip, dateFormat, lang)
            }
        }
    }
}

@Composable
private fun TripRow(trip: FirestoreRental, dateFormat: SimpleDateFormat, lang: Lang) {
    val dateStr = remember(trip.startTime) { dateFormat.format(Date(trip.startTime)) }
    val durationMin = if (trip.endTime > trip.startTime) (trip.endTime - trip.startTime) / 60000 else 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Surface1)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.DirectionsCar, contentDescription = null, tint = Ice, modifier = Modifier.size(20.dp))
                    Text(trip.carName.ifBlank { "Көлік #${trip.carId}" }, style = MaterialTheme.typography.titleMedium, color = TextHi)
                }
                Text(formatTenge(trip.totalPrice), style = MaterialTheme.typography.titleMedium, color = Ice)
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(dateStr, style = MaterialTheme.typography.bodySmall, color = TextMid)
                Text(if (lang == Lang.KZ) "Ұзақтығы: ~$durationMin мин" else "Длительность: ~$durationMin мин", style = MaterialTheme.typography.bodySmall, color = TextMid)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.CreditCard, contentDescription = null, tint = TextLow, modifier = Modifier.size(16.dp))
                    Text("Kaspi Pay", style = MaterialTheme.typography.bodySmall, color = TextLow)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Success, modifier = Modifier.size(14.dp))
                    Text(if (trip.status == "FINISHED") (if (lang == Lang.KZ) "Аяқталды" else "Завершено") else trip.status, style = MaterialTheme.typography.bodySmall, color = Success)
                }
            }
        }
    }
}
