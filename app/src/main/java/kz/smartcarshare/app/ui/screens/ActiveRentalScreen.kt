package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kz.smartcarshare.app.data.Lang
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
    val lang = vm.currentLang

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (lang == Lang.KZ) "Белсенді сапар" else "Активная поездка",
                style = MaterialTheme.typography.titleLarge,
                color = TextHi,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )
            Chip(if (lang == Lang.KZ) "Белсенді" else "В пути", accent = true)
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
                    .padding(top = 16.dp, bottom = 10.dp)
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Surface1)
                    .border(
                        8.dp,
                        Brush.sweepGradient(listOf(Amber, Ice, ElectricIndigo, Amber)),
                        CircleShape
                    )
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(Surface2),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = vm.elapsedFormatted(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextHi,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (lang == Lang.KZ) "жүру уақыты" else "время поездки",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMid
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Surface1,
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = formatTenge(vm.liveAccruedPrice()),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Ice,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (lang == Lang.KZ) "ағымдағы сапар сомасы" else "текущая стоимость поездки",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMid
                    )
                }
            }

            if (car != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Surface1, RoundedCornerShape(22.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(22.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (lang == Lang.KZ) "Көлік" else "Авто", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                        Text(car.name, style = MaterialTheme.typography.labelLarge, color = TextHi, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .height(1.dp)
                            .background(BorderColor)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (lang == Lang.KZ) "Тариф" else "Тариф", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                        Text(
                            if (vm.tariff == Tariff.HOUR) (if (lang == Lang.KZ) "Сағаттық" else "Почасовой") else (if (lang == Lang.KZ) "Тәуліктік" else "Суточный"),
                            style = MaterialTheme.typography.labelLarge,
                            color = Amber,
                            fontWeight = FontWeight.Bold
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
            PrimaryButton(
                text = if (lang == Lang.KZ) "Сапарды аяқтау" else "Завершить поездку",
                onClick = onFinishTrip
            )
        }
    }
}
