package kz.smartcarshare.app.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*
import java.io.File

@Composable
fun InspectionAfterScreen(
    vm: RentalViewModel,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<String?>(null) }
    var rating by remember { mutableStateOf(5) }
    val lang = vm.currentLang

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val file = File(context.filesDir, "after_inspection_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            photoUri = Uri.fromFile(file).toString()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            if (lang == Lang.KZ) "ЖИ-тексеру · қорытынды" else "ИИ-проверка · итоги",
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
                    text = if (lang == Lang.KZ) "Тапсыру алдында соңғы суретке түсіріңіз — жүйе суретті сақтайды." else "Сделайте финальное фото перед сдачей — фотофиксация сохранится в системе.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMid,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Surface1, RoundedCornerShape(20.dp))
                        .border(1.5.dp, if (photoUri != null) Success else BorderColor, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUri != null) {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = "Соңғы сурет",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Surface(
                            color = Success,
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AmberOnDark, modifier = Modifier.size(16.dp))
                                Text(
                                    if (lang == Lang.KZ) "Соңғы фото сақталды" else "Финальное фото сохранено",
                                    color = AmberOnDark,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = null, tint = Ice, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(10.dp))
                            Text(if (lang == Lang.KZ) "Соңғы суретке түсіру" else "Сделать финальное фото", style = MaterialTheme.typography.labelLarge, color = TextHi)
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = { cameraLauncher.launch(null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ice, contentColor = Navy900)
                ) {
                    Icon(Icons.Filled.PhotoCamera, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (photoUri == null)
                            (if (lang == Lang.KZ) "Камераны ашу және суретке түсіру" else "Открыть камеру и сделать фото")
                        else
                            (if (lang == Lang.KZ) "Қайта суретке түсіру" else "Переснять фото"),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            item {
                if (photoUri != null) {
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .background(Surface1, RoundedCornerShape(18.dp))
                                .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            AfterCheckRow(if (lang == Lang.KZ) "Соңғы фотофиксация сәтті аяқталды" else "Финальная фотофиксация успешно завершена")
                            AfterCheckRow(if (lang == Lang.KZ) "Көлік тапсыруға дайын" else "Автомобиль готов к сдаче")
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
                                Text(if (lang == Lang.KZ) "Жалпы сомасы" else "Итоговая сумма", style = MaterialTheme.typography.bodyMedium, color = TextMid)
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
                enabled = photoUri != null,
                onClick = onDone
            )
        }
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
