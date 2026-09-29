package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ActiveRentalScreen(
    vm: RentalViewModel,
    onFinishTrip: () -> Unit
) {
    val car = vm.selectedCar

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Сапар барысында",
                style = MaterialTheme.typography.titleLarge,
                color = TextHi,
                modifier = Modifier.weight(1f)
            )
            Chip("Белсенді", accent = true)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .size(190.dp)
                    .background(Surface2, CircleShape)
                    .border(10.dp, Amber, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(vm.elapsedFormatted(), style = MaterialTheme.typography.headlineSmall, color = TextHi)
                    Text("жүру уақыты", style = MaterialTheme.typography.bodySmall, color = TextMid)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 14.dp)
            ) {
                Text(
                    formatTenge(vm.liveAccruedPrice()),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Ice
                )
                Text(
                    "автоплатеж арқылы есептелуде",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMid
                )
            }

            if (car != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Surface1, RoundedCornerShape(18.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Көлік", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                        Text(car.name, style = MaterialTheme.typography.labelLarge, color = TextHi)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Тариф", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                        Text(
                            if (vm.tariff == Tariff.HOUR) "Сағаттық" else "Тәуліктік",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextHi
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy800)
                .padding(20.dp)
        ) {
            OutlineActionButton(text = "Сапарды аяқтау", onClick = onFinishTrip)
        }
    }
}