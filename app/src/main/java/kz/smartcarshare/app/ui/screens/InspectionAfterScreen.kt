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
import kz.smartcarshare.app.data.Lang
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
    val lang = vm.currentLang

    LaunchedEffect(scanState) {
        if (scanState == AfterScan.ANALYZING) {
            delay(1400)
            scanState = AfterScan.DONE
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            if (lang == Lang.KZ) "ЖИ-тексеру · қорытынды" else "AI-проверка · итоги",
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
                    text = if (lang == Lang.KZ) "Тапсыру алдында соңғы суретке түсіріңіз — жүйе оны бастапқы фотомен автоматты салыстырады." else "Сделайте финальное фото перед сдачей — система автоматически сравнит его с начальным.",
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
                            Text(if (lang == Lang.KZ) "Соңғы суретке түсіру" else "Сделать финальное фото", style = MaterialTheme.typography.labelLarge, color = TextHi)
                        }
                        AfterScan.ANALYZING -> {
                            CircularProgressIndicator(color = Ice)
                            Spacer(Modifier.height(10.dp))
                            Text(if (lang == Lang.KZ) "Салыстыру жүргізілуде..." else "Выполняется сравнение...", style = MaterialTheme.typography.labelLarge, color = Ice)
                        }
                        AfterScan.DONE -> {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Success)
                            Spacer(Modifier.height(8.dp))
                            Text(if (lang == Lang.KZ) "Сурет сәтті тіркелді" else "Фото успешно зарегистрировано", style = MaterialTheme.typography.labelLarge, color = TextHi)
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
                        Text(if (lang == Lang.KZ) "Суретке түсіру" else "Сделать фото", color = TextHi)
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
                            PhotoPlaceholder(if (lang == Lang.KZ) "Дейін" else "До", Modifier.weight(1f))
                            PhotoPlaceholder(if (lang == Lang.KZ) "Кейін" else "После", Modifier.weight(1f))
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .background(Surface1, RoundedCornerShape(18.dp))
                                .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            AfterCheckRow(if (lang == Lang.KZ) "Жаңа зақым анықталмады" else "Новых повреждений не обнаружено")
                            AfterCheckRow(if (lang == Lang.KZ) "Салон жағдайы өзгеріссіз" else "Состояние салона без изменений")
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .background(Surface1, RoundedCornerShape(18.dp))
                                .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            Text(if (lang == Lang.KZ) "ТҮПКІЛІКТІ ЕСЕП" else "ИТОГОВЫЙ РАСЧЕТ", style = MaterialTheme.typography.bodySmall, color = Amber)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (lang == Lang.KZ) "Жалпы уақыт" else "Общее время", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                                Text(vm.elapsedFormatted(), style = MaterialTheme.typography.labelLarge, color = TextHi)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (lang == Lang.KZ) "Сомасы (автоплатеж)" else "Сумма (автоплатеж)", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                                Text(
                                    formatTenge(vm.liveAccruedPrice()),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Ice
                                )
                            }
                        }
                        Text(
                            if (lang == Lang.KZ) "САПАРДЫ БАҒАЛАҢЫЗ" else "ОЦЕНИТЕ ПОЕЗДКУ",
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
                text = if (lang == Lang.KZ) "Бас бетке оралу" else "Вернуться на главную",
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
