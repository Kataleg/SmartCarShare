package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

private enum class AfterScan { IDLE, ANALYZING, DONE }

@Composable
fun InspectionAfterScreen(
    vm: RentalViewModel,
    onDone: () -> Unit
) {
    var scanState by remember { mutableStateOf(AfterScan.IDLE) }
    var rating by remember { mutableStateOf(5) }

    LaunchedEffect(scanState) {
        if (scanState == AfterScan.ANALYZING) {
            delay(1400)
            scanState = AfterScan.DONE
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "ЖИ-тексеру · қорытынды",
            style = MaterialTheme.typography.titleLarge,
            color = TextHi,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                Text(
                    "Тапсыру алдында соңғы суретке түсіріңіз — жүйе оны бастапқы фотомен автоматты салыстырады.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMid,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .background(Surface1, RoundedCornerShape(20.dp))
                        .border(1.5.dp, BorderColor, RoundedCornerShape(20.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (scanState) {
                        AfterScan.IDLE -> {
                            Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = TextMid)
                            Spacer(Modifier.height(8.dp))
                            Text("Соңғы суретке түсіру", style = MaterialTheme.typography.labelLarge, color = TextHi)
                        }
                        AfterScan.ANALYZING -> {
                            CircularProgressIndicator(color = Ice)
                            Spacer(Modifier.height(10.dp))
                            Text("Салыстыру жүргізілуде...", style = MaterialTheme.typography.labelLarge, color = Ice)
                        }
                        AfterScan.DONE -> {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Success)
                            Spacer(Modifier.height(8.dp))
                            Text("Сурет сәтті тіркелді", style = MaterialTheme.typography.labelLarge, color = TextHi)
                        }
                    }
                }
            }
            item {
                if (scanState == AfterScan.IDLE) {
                    OutlinedButton(
                        onClick = { scanState = AfterScan.ANALYZING },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Суретке түсіру", color = TextHi)
                    }
                }
            }
            item {
                if (scanState == AfterScan.DONE) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            PhotoPlaceholder("Дейін", Modifier.weight(1f))
                            PhotoPlaceholder("Кейін", Modifier.weight(1f))
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .background(Surface1, RoundedCornerShape(18.dp))
                                .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            AfterCheckRow("Жаңа зақым анықталмады")
                            AfterCheckRow("Салон жағдайы өзгеріссіз")
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .background(Surface1, RoundedCornerShape(18.dp))
                                .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            Text("ТҮПКІЛІКТІ ЕСЕП", style = MaterialTheme.typography.bodySmall, color = Amber)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Жалпы уақыт", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                                Text(vm.elapsedFormatted(), style = MaterialTheme.typography.labelLarge, color = TextHi)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Сомма (автоплатеж)", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                                Text(
                                    formatTenge(vm.liveAccruedPrice()),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Ice
                                )
                            }
                        }
                        Text(
                            "САПАРДЫ БАҒАЛАҢЫЗ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Amber,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            (1..5).forEach { i ->
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = if (i <= rating) Amber else Surface2,
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .size(32.dp)
                                        .clickable {
                                            rating = i
                                            vm.rating = i
                                        }
                                )
                            }
                        }
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
                text = "Бас бетке оралу",
                enabled = scanState == AfterScan.DONE,
                onClick = onDone
            )
        }
    }
}

@Composable
private fun PhotoPlaceholder(label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .background(Surface1, RoundedCornerShape(14.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Image, contentDescription = null, tint = TextLow)
        }
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = TextMid,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun AfterCheckRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Success)
        Text(text, style = MaterialTheme.typography.bodyLarge, color = TextHi)
    }
}