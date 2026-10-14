package kz.smartcarshare.app.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoCamera
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
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*
import java.io.File

@Composable
fun InspectionBeforeScreen(
    lang: Lang = Lang.KZ,
    onBack: () -> Unit,
    onStartRental: () -> Unit
) {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val file = File(context.filesDir, "before_inspection_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            photoUri = Uri.fromFile(file).toString()
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
                        "Көлікті алар алдында суретке түсіріңіз — кузов пен салонның жағдайы сақталады."
                    else
                        "Сделайте фото автомобиля перед поездкой — фотофиксация сохранится в системе.",
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
                            contentDescription = "Көлік суреті",
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
                                    if (lang == Lang.KZ) "Сурет сақталды" else "Фото сохранено",
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
                            Text(
                                if (lang == Lang.KZ) "Камераға түсіру" else "Сделать фото на камеру",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextHi
                            )
                            Text(
                                if (lang == Lang.KZ) "Машинаны суретке түсіріңіз" else "Сфотографируйте автомобиль",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLow
                            )
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .background(Surface1, RoundedCornerShape(18.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            if (lang == Lang.KZ) "ФОТОФИКСАЦИЯ НӘТИЖЕСІ" else "РЕЗУЛЬТАТ ФОТОФИКСАЦИИ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Amber
                        )
                        BeforeCheckRow(if (lang == Lang.KZ) "Көліктің бастапқы фотосы тіркелді" else "Начальное фото авто зафиксировано")
                        BeforeCheckRow(if (lang == Lang.KZ) "Сапарды бастауға дайын" else "Готово к началу поездки")
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
                enabled = photoUri != null,
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
