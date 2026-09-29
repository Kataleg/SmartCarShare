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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val tariffLabel = if (vm.tariff == Tariff.HOUR) "Сағаттық тариф" else "Тәуліктік тариф"

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(title = "Растау және төлем", onBack = onBack)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                Text("ТАҢДАЛҒАН КӨЛІК", style = MaterialTheme.typography.bodySmall, color = Amber)
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
                    IconBadge(
                        if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                        bg = Ice,
                        tint = AmberOnDark
                    )
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
                        .background(Surface1, RoundedCornerShape(18.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp)
                        ) {
                            Text("Автоплатеж қосу", style = MaterialTheme.typography.labelLarge, color = TextHi)
                            Text(
                                "Сома алу/тапсыру уақыты бойынша карта автоматты есептен шығарады",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMid
                            )
                        }
                        Switch(
                            checked = vm.autopayOn,
                            onCheckedChange = { vm.autopayOn = it },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Amber,
                                checkedThumbColor = AmberOnDark
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                            .background(Surface2, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 34.dp, height = 24.dp)
                                .background(Amber, RoundedCornerShape(5.dp))
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Visa •••• 4417", style = MaterialTheme.typography.labelLarge, color = TextHi)
                            Text("Негізгі карта", style = MaterialTheme.typography.bodySmall, color = TextMid)
                        }
                        Text("Өзгерту", style = MaterialTheme.typography.bodySmall, color = TextLow)
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 20.dp)
                        .background(Surface1, RoundedCornerShape(18.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Text("АЛДЫН АЛА ЕСЕП", style = MaterialTheme.typography.bodySmall, color = Amber)
                    SummaryRow("Тариф", tariffLabel)
                    val unit = if (vm.tariff == Tariff.HOUR) "/ сағат" else "/ тәулік"
                    SummaryRow("Болжамды баға", "${formatTenge(vm.currentPrice())} $unit")
                    SummaryRow("Сақтандыру", if (vm.insuranceOn) "Қосылды" else "Жоқ")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy800)
                .padding(20.dp)
        ) {
            PrimaryButton(text = "Жалдауды растау", onClick = onConfirm)
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