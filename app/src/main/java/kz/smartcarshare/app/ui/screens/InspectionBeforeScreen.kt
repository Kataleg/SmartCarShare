package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

private enum class BeforeScan { IDLE, ANALYZING, DONE }

@Composable
fun InspectionBeforeScreen(
    lang: Lang = Lang.KZ,
    onBack: () -> Unit,
    onStartRental: () -> Unit
) {
    var scanState by remember { mutableStateOf(BeforeScan.IDLE) }

    LaunchedEffect(scanState) {
        if (scanState == BeforeScan.ANALYZING) {
            delay(1400)
            scanState = BeforeScan.DONE
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(
            title = if (lang == Lang.KZ) "ЖИ-тексеру · алдын ала" else "ИИ-проверка · осмотр",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                Text(
                    text = if (lang == Lang.KZ)
                        "Көлікті алар алдында суретке түсіріңіз — жасанды интеллект кузов пен салонның жағдайын автоматты тіркейді."
                    else
                        "Сделайте фото автомобиля перед поездкой — искусственный интеллект автоматически зафиксирует состояние кузова и салона.",
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
                        BeforeScan.IDLE -> {
                            Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = TextMid)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                if (lang == Lang.KZ) "Камераға түсіру" else "Сделать фото",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextHi
                            )
                            Text(
                                if (lang == Lang.KZ) "Машинаны 4 жағынан суретке түсіріңіз" else "Сфотографируйте авто с 4 сторон",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLow
                            )
                        }
                        BeforeScan.ANALYZING -> {
                            CircularProgressIndicator(color = Ice)
                            Spacer(Modifier.height(10.dp))
                            Text(
                                if (lang == Lang.KZ) "ЖИ талдап жатыр..." else "ИИ анализирует...",
                                style = MaterialTheme.typography.labelLarge,
                                color = Ice
                            )
                        }
                        BeforeScan.DONE -> {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Success)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                if (lang == Lang.KZ) "Сурет сәтті тіркелді" else "Фото успешно зарегистрировано",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextHi
                            )
                        }
                    }
                }
            }
            item {
                if (scanState == BeforeScan.IDLE) {
                    OutlinedButton(
                        onClick = { scanState = BeforeScan.ANALYZING },
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
                if (scanState == BeforeScan.DONE) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .background(Surface1, RoundedCornerShape(18.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            if (lang == Lang.KZ) "ЖИ ҚОРЫТЫНДЫСЫ" else "ЗАКЛЮЧЕНИЕ ИИ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Amber
                        )
                        BeforeCheckRow(if (lang == Lang.KZ) "Кузовта зақым табылмады" else "Повреждений кузова не обнаружено")
                        BeforeCheckRow(if (lang == Lang.KZ) "Дөңгелектер қалыпты күйде" else "Шины и колеса в норме")
                        BeforeCheckRow(if (lang == Lang.KZ) "Салон таза, зақым жоқ" else "Салон чистый, без повреждений")
                        BeforeCheckRow(if (lang == Lang.KZ) "Жанармай деңгейі: 78%" else "Уровень топлива: 78%")
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
                text = if (lang == Lang.KZ) "Жалдауды бастау" else "Начать аренду",
                enabled = scanState == BeforeScan.DONE,
                onClick = onStartRental
            )
        }
    }
}

@Composable
private fun BeforeCheckRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Success)
        Text(text, style = MaterialTheme.typography.bodyLarge, color = TextHi)
    }
}