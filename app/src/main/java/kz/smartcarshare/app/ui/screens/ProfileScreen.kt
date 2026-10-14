package kz.smartcarshare.app.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddRoad
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.theme.*
import java.io.File

@Composable
fun ProfileScreen(
    vm: RentalViewModel,
    currentUser: LocalUser? = null,
    lang: Lang = Lang.KZ,
    onToggleLang: () -> Unit = {},
    onChatClick: () -> Unit,
    onB2BClick: () -> Unit,
    onMyTripsClick: () -> Unit = {},
    onLogoutClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onSendPasswordReset: (email: String) -> Unit = {},
    onUpdateProfile: (firstName: String, lastName: String, phone: String, email: String, avatarUrl: String) -> Unit = { _, _, _, _, _ -> }
) {
    val initial = currentUser?.name?.trim()?.firstOrNull()?.uppercaseChar()?.toString() ?: "П"
    val displayName = currentUser?.name?.takeIf { it.isNotBlank() } ?: (if (lang == Lang.KZ) "Пайдаланушы" else "Пользователь")
    val displayPhone = currentUser?.phone?.takeIf { it.isNotBlank() } ?: currentUser?.email ?: (if (lang == Lang.KZ) "Тіркелгі жоқ" else "Нет аккаунта")

    val context = LocalContext.current

    var isEditDialogVisible by remember { mutableStateOf(false) }
    var isAddCarDialogVisible by remember { mutableStateOf(false) }
    var resetLinkSentNotice by remember { mutableStateOf(false) }

    var editFirstName by remember(currentUser) {
        val parts = (currentUser?.name ?: "").split(" ", limit = 2)
        mutableStateOf(parts.getOrElse(0) { "" })
    }
    var editLastName by remember(currentUser) {
        val parts = (currentUser?.name ?: "").split(" ", limit = 2)
        mutableStateOf(parts.getOrElse(1) { "" })
    }
    var editPhone by remember(currentUser) { mutableStateOf(currentUser?.phone ?: "") }
    var editEmail by remember(currentUser) { mutableStateOf(currentUser?.email ?: "") }
    var editAvatarUrl by remember(currentUser) { mutableStateOf(currentUser?.avatarUrl ?: "") }

    val avatarGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val permanentUrl = saveUriToInternalStorage(context, uri, "avatar")
            editAvatarUrl = permanentUrl
        }
    }

    val avatarCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val permanentUrl = saveBitmapToInternalStorage(context, bitmap, "avatar")
            editAvatarUrl = permanentUrl
        }
    }

    var newCarName by remember { mutableStateOf("") }
    var newCarPriceHour by remember { mutableStateOf("2000") }
    var newCarPriceDay by remember { mutableStateOf("20000") }
    var newCarLocation by remember { mutableStateOf("Астана · Мангилик Ел") }
    var newCarTransmission by remember { mutableStateOf("Автомат") }
    var newCarFuel by remember { mutableStateOf("Бензин AI-95") }
    var newCarSeats by remember { mutableStateOf("5 орын") }
    var newCarCategory by remember { mutableStateOf("Комфорт") }
    var newCarIsLux by remember { mutableStateOf(false) }
    var newCarImageUrl by remember { mutableStateOf("") }
    var newCarOwnerKaspi by remember(currentUser) { mutableStateOf(currentUser?.phone ?: "+7 (701) 000-00-00") }

    val carGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val permanentUrl = saveUriToInternalStorage(context, uri, "car")
            newCarImageUrl = permanentUrl
        }
    }

    val carCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val permanentUrl = saveBitmapToInternalStorage(context, bitmap, "car")
            newCarImageUrl = permanentUrl
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Surface1,
        unfocusedContainerColor = Surface1,
        focusedBorderColor = Ice,
        unfocusedBorderColor = BorderColor,
        focusedLabelColor = Ice,
        unfocusedLabelColor = TextMid,
        focusedTextColor = TextHi,
        unfocusedTextColor = TextHi
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = Surface1,
                border = BorderStroke(1.dp, BorderColor),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(Ice, ElectricIndigo))
                                )
                                .padding(3.dp)
                                .clip(CircleShape)
                                .background(Navy900)
                                .clickable { if (currentUser != null) isEditDialogVisible = true },
                            contentAlignment = Alignment.Center
                        ) {
                            if (!currentUser?.avatarUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = currentUser.avatarUrl,
                                    contentDescription = "Аватар",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = initial,
                                    color = Ice,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = displayName,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextHi,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Surface(
                                    color = Ice.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "PRO",
                                        color = Ice,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = displayPhone,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMid,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        if (currentUser != null) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Surface2)
                                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                                    .clickable { isEditDialogVisible = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Өңдеу",
                                    tint = Ice,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface1, RoundedCornerShape(18.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
            ) {
                if (currentUser != null) {
                    MenuRow(
                        icon = Icons.Filled.AddRoad,
                        title = if (lang == Lang.KZ) "Өз көлігімді жалға беру" else "Сдать свое авто в аренду",
                        sub = if (lang == Lang.KZ) "+ Автомобиль қосу" else "+ Добавить авто",
                        onClick = { isAddCarDialogVisible = true }
                    )
                    MenuRow(
                        icon = Icons.Filled.Edit,
                        title = Strings.get(lang, "profile_edit"),
                        sub = if (lang == Lang.KZ) "Аты, тегі, суреті, номер, почта" else "Имя, фамилия, фото, телефон, почта",
                        onClick = { isEditDialogVisible = true }
                    )
                }
                MenuRow(Icons.Filled.Chat, Strings.get(lang, "ai_chat"), onClick = onChatClick)
                MenuRow(
                    icon = Icons.Filled.DirectionsCar,
                    title = Strings.get(lang, "my_trips"),
                    sub = if (lang == Lang.KZ) "Тарих пен сапарлар" else "История поездок",
                    onClick = onMyTripsClick
                )
                MenuRow(Icons.Filled.Apartment, Strings.get(lang, "for_business"), onClick = onB2BClick, showDivider = true)

                if (currentUser != null) {
                    MenuRow(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = Strings.get(lang, "logout"),
                        onClick = onLogoutClick,
                        showDivider = false
                    )
                } else {
                    MenuRow(
                        icon = Icons.Filled.PersonAdd,
                        title = if (lang == Lang.KZ) "Тіркелу / Кіру" else "Регистрация / Вход",
                        onClick = onRegisterClick,
                        showDivider = false
                    )
                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .background(Surface1, RoundedCornerShape(18.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                    .clickable { onToggleLang() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(Strings.get(lang, "language"), style = MaterialTheme.typography.labelLarge, color = TextHi)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("ҚАЗ", color = if (lang == Lang.KZ) Amber else TextMid, fontWeight = if (lang == Lang.KZ) FontWeight.Bold else FontWeight.Normal)
                    Text("/", color = TextLow)
                    Text("РУС", color = if (lang == Lang.RU) Amber else TextMid, fontWeight = if (lang == Lang.RU) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Smart CarShare командасы · ЕНУ ФИТ ВТиПО",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextLow
                )
            }
        }
    }

    if (isAddCarDialogVisible) {
        AlertDialog(
            onDismissRequest = { isAddCarDialogVisible = false },
            containerColor = Surface1,
            title = {
                Text(if (lang == Lang.KZ) "Өз көлігіңді жалға беру" else "Сдать свое авто в аренду", color = TextHi, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = newCarName,
                        onValueChange = { newCarName = it },
                        label = { Text(if (lang == Lang.KZ) "Көлік маркасы мен моделі" else "Марка и модель (напр. Kia K5 2023)") },
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newCarPriceHour,
                            onValueChange = { newCarPriceHour = it.filter { c -> c.isDigit() } },
                            label = { Text(if (lang == Lang.KZ) "₸ / сағат" else "₸ / час") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = newCarPriceDay,
                            onValueChange = { newCarPriceDay = it.filter { c -> c.isDigit() } },
                            label = { Text(if (lang == Lang.KZ) "₸ / тәулік" else "₸ / сутки") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = newCarLocation,
                        onValueChange = { newCarLocation = it },
                        label = { Text(if (lang == Lang.KZ) "Астанадағы орналасқан жері" else "Локация в Астане") },
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text(if (lang == Lang.KZ) "Беріліс қорабы:" else "Коробка передач:", style = MaterialTheme.typography.labelMedium, color = Amber)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Автомат", "Механика", "Вариатор", "Робот").forEach { tr ->
                            FilterChip(
                                selected = newCarTransmission == tr,
                                onClick = { newCarTransmission = tr },
                                label = { Text(tr, fontSize = 10.sp) }
                            )
                        }
                    }

                    Text(if (lang == Lang.KZ) "Отын түрі:" else "Тип топлива:", style = MaterialTheme.typography.labelMedium, color = Amber)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Бензин AI-95", "Бензин AI-92", "Дизель", "Электр", "Гибрид").take(3).forEach { fl ->
                            FilterChip(
                                selected = newCarFuel == fl,
                                onClick = { newCarFuel = fl },
                                label = { Text(fl, fontSize = 10.sp) }
                            )
                        }
                    }

                    Text(if (lang == Lang.KZ) "Орын саны:" else "Количество мест:", style = MaterialTheme.typography.labelMedium, color = Amber)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("2 орын", "4 орын", "5 орын", "7 орын", "8 орын").forEach { st ->
                            FilterChip(
                                selected = newCarSeats == st,
                                onClick = { newCarSeats = st },
                                label = { Text(st, fontSize = 10.sp) }
                            )
                        }
                    }

                    Text(if (lang == Lang.KZ) "Көлік класы / Категория:" else "Класс / Категория:", style = MaterialTheme.typography.labelMedium, color = Amber)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Эконом", "Комфорт", "Бизнес", "Кроссовер", "Lux").forEach { cat ->
                            FilterChip(
                                selected = newCarCategory == cat,
                                onClick = { newCarCategory = cat },
                                label = { Text(cat, fontSize = 10.sp) }
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = newCarIsLux,
                            onCheckedChange = { newCarIsLux = it },
                            colors = CheckboxDefaults.colors(checkedColor = Amber, checkmarkColor = AmberOnDark)
                        )
                        Text(if (lang == Lang.KZ) "Lux / Премиум статус" else "Статус Lux / Премиум", color = TextHi, style = MaterialTheme.typography.bodySmall)
                    }

                    Text(if (lang == Lang.KZ) "Көліктің фотосы:" else "Фотография авто:", style = MaterialTheme.typography.labelMedium, color = Amber)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { carCameraLauncher.launch(null) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Ice),
                            border = BorderStroke(1.dp, Ice)
                        ) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(if (lang == Lang.KZ) "Камера" else "Камера", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { carGalleryLauncher.launch("image/*") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Ice),
                            border = BorderStroke(1.dp, Ice)
                        ) {
                            Icon(Icons.Filled.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(if (lang == Lang.KZ) "Галерея" else "Галерея", fontSize = 11.sp)
                        }
                    }

                    if (newCarImageUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Surface2)
                                .border(1.dp, Success, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = newCarImageUrl,
                                contentDescription = "Көлік суреті",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    OutlinedTextField(
                        value = newCarOwnerKaspi,
                        onValueChange = { newCarOwnerKaspi = it },
                        label = { Text(if (lang == Lang.KZ) "Kaspi телефон номеріңіз (төлемдер үшін)" else "Номер Kaspi для получения выплат") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCarName.isNotBlank()) {
                            val pHour = newCarPriceHour.toIntOrNull() ?: 2000
                            val pDay = newCarPriceDay.toIntOrNull() ?: 20000
                            vm.addCustomCar(
                                name = newCarName.trim(),
                                pricePerHour = pHour,
                                pricePerDay = pDay,
                                location = newCarLocation.trim(),
                                imageUrl = newCarImageUrl.trim(),
                                seats = newCarSeats,
                                transmission = newCarTransmission,
                                fuel = newCarFuel,
                                categoryClass = newCarCategory,
                                isLux = newCarIsLux,
                                ownerName = currentUser?.name ?: "Падаланушы",
                                ownerEmail = currentUser?.email ?: "user@smartcarshare.kz",
                                ownerKaspiNumber = newCarOwnerKaspi.trim()
                            )
                            isAddCarDialogVisible = false
                            newCarName = ""
                            newCarImageUrl = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                ) {
                    Text(if (lang == Lang.KZ) "Жариялау" else "Опубликовать", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddCarDialogVisible = false }) {
                    Text(Strings.get(lang, "cancel"), color = TextMid)
                }
            }
        )
    }

    if (isEditDialogVisible) {
        AlertDialog(
            onDismissRequest = { isEditDialogVisible = false },
            containerColor = Surface1,
            title = {
                Text(if (lang == Lang.KZ) "Профиль параметрлерін өзгерту" else "Изменить параметры профиля", color = TextHi)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = editFirstName,
                            onValueChange = { editFirstName = it },
                            label = { Text(Strings.get(lang, "first_name")) },
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = editLastName,
                            onValueChange = { editLastName = it },
                            label = { Text(Strings.get(lang, "last_name")) },
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Text(if (lang == Lang.KZ) "Профиль суреті:" else "Фото профиля:", style = MaterialTheme.typography.labelSmall, color = Amber)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { avatarCameraLauncher.launch(null) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Ice),
                            border = BorderStroke(1.dp, Ice)
                        ) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(if (lang == Lang.KZ) "Камера" else "Камера", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { avatarGalleryLauncher.launch("image/*") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Ice),
                            border = BorderStroke(1.dp, Ice)
                        ) {
                            Icon(Icons.Filled.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(if (lang == Lang.KZ) "Галерея" else "Галерея", fontSize = 11.sp)
                        }
                    }

                    if (editAvatarUrl.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Surface2)
                                    .border(1.dp, Success, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = editAvatarUrl,
                                    contentDescription = "Аватар",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Text(
                                text = if (lang == Lang.KZ) "Сурет орнатылды және сақталады" else "Фото успешно выбрано и будет сохранено",
                                style = MaterialTheme.typography.bodySmall,
                                color = Success
                            )
                        }
                    }

                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text(Strings.get(lang, "phone")) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text(Strings.get(lang, "email")) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedButton(
                        onClick = {
                            val emailToSend = editEmail.ifBlank { currentUser?.email ?: "" }
                            if (emailToSend.isNotBlank()) {
                                onSendPasswordReset(emailToSend)
                                resetLinkSentNotice = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Ice)
                    ) {
                        Icon(Icons.Filled.LockReset, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (lang == Lang.KZ) "Құпия сөзді өзгерту сілтемесін поштаға жіберу" else "Отправить ссылку смены пароля на почту", style = MaterialTheme.typography.bodySmall)
                    }

                    if (resetLinkSentNotice) {
                        Text(
                            if (lang == Lang.KZ) "Құпия сөзді сброс жасау сілтемесі поштаңызға жіберілді!" else "Ссылка для сброса пароля отправлена на почту!",
                            color = Success,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(
                            editFirstName.trim(),
                            editLastName.trim(),
                            editPhone.trim(),
                            editEmail.trim(),
                            editAvatarUrl.trim()
                        )
                        isEditDialogVisible = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                ) {
                    Text(Strings.get(lang, "save"), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { isEditDialogVisible = false }) {
                    Text(Strings.get(lang, "cancel"), color = TextMid)
                }
            }
        )
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    title: String,
    sub: String? = null,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Surface2, RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Ice, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.labelLarge, color = TextHi)
                if (sub != null) {
                    Text(sub, style = MaterialTheme.typography.bodySmall, color = TextLow)
                }
            }
            Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMid)
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .height(1.dp)
                    .background(BorderColor)
            )
        }
    }
}

private fun saveUriToInternalStorage(context: Context, uri: Uri, prefix: String): String {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return uri.toString()
        val file = File(context.filesDir, "${prefix}_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        Uri.fromFile(file).toString()
    } catch (e: Exception) {
        uri.toString()
    }
}

private fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap, prefix: String): String {
    return try {
        val file = File(context.filesDir, "${prefix}_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
        }
        Uri.fromFile(file).toString()
    } catch (e: Exception) {
        ""
    }
}
