package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddRoad
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.components.PaymentMethodsDialog
import kz.smartcarshare.app.ui.theme.*

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
    onUpdateProfile: (firstName: String, lastName: String, phone: String, email: String) -> Unit = { _, _, _, _ -> }
) {
    val initial = currentUser?.name?.trim()?.firstOrNull()?.uppercaseChar()?.toString() ?: "П"
    val displayName = currentUser?.name?.takeIf { it.isNotBlank() } ?: (if (lang == Lang.KZ) "Пайдаланушы" else "Пользователь")
    val displayPhone = currentUser?.phone?.takeIf { it.isNotBlank() } ?: currentUser?.email ?: (if (lang == Lang.KZ) "Тіркелгі жоқ" else "Нет аккаунта")

    var isEditDialogVisible by remember { mutableStateOf(false) }
    var isPaymentDialogVisible by remember { mutableStateOf(false) }
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

    var newCarName by remember { mutableStateOf("") }
    var newCarPriceHour by remember { mutableStateOf("1200") }
    var newCarPriceDay by remember { mutableStateOf("15000") }
    var newCarLocation by remember { mutableStateOf("Астана · Мангилик Ел") }
    var newCarImageUrl by remember { mutableStateOf("") }
    var newCarOwnerKaspi by remember(currentUser) { mutableStateOf(currentUser?.phone ?: "+7 (701) 000-00-00") }

    val ownerEarnings = vm.getOwnerEarnings(currentUser?.email ?: "")

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Ice, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(initial, color = Navy900, style = MaterialTheme.typography.headlineSmall)
                }
                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(displayName, style = MaterialTheme.typography.titleLarge, color = TextHi)
                    Text(displayPhone, style = MaterialTheme.typography.bodySmall, color = TextMid)
                }
                if (currentUser != null) {
                    IconButton(
                        onClick = { isEditDialogVisible = true },
                        modifier = Modifier
                            .background(Surface1, RoundedCornerShape(12.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Өңдеу", tint = Ice)
                    }
                }
            }
        }

        if (currentUser != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .border(1.dp, Amber.copy(alpha = 0.3f), RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface1)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Amber.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = Amber, modifier = Modifier.size(24.dp))
                        }
                        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(
                                if (lang == Lang.KZ) "Жалға беруден түскен табыс" else "Доход от сдачи авто в аренду",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMid
                            )
                            Text(
                                formatTenge(ownerEarnings),
                                style = MaterialTheme.typography.titleLarge,
                                color = Ice,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                if (lang == Lang.KZ) "Kaspi Перевод: ${currentUser.phone}" else "Kaspi Перевод по номеру: ${currentUser.phone}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLow
                            )
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
                        sub = if (lang == Lang.KZ) "+ Автомобиль қосып табыс табу" else "+ Добавить авто и получать доход",
                        onClick = { isAddCarDialogVisible = true }
                    )
                    MenuRow(
                        icon = Icons.Filled.Edit,
                        title = Strings.get(lang, "profile_edit"),
                        sub = if (lang == Lang.KZ) "Аты, тегі, номер, почта" else "Имя, фамилия, телефон, почта",
                        onClick = { isEditDialogVisible = true }
                    )
                }
                MenuRow(Icons.Filled.Chat, Strings.get(lang, "ai_chat"), onClick = onChatClick)
                MenuRow(
                    icon = Icons.Filled.DirectionsCar,
                    title = Strings.get(lang, "my_trips"),
                    sub = if (lang == Lang.KZ) "Тарих пен төлем чектер" else "История и чеки поездок",
                    onClick = onMyTripsClick
                )
                MenuRow(
                    icon = Icons.Filled.CreditCard,
                    title = Strings.get(lang, "payment_methods"),
                    sub = vm.selectedPaymentMethod.title,
                    onClick = { isPaymentDialogVisible = true }
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

    if (isPaymentDialogVisible) {
        PaymentMethodsDialog(
            paymentMethods = vm.paymentMethods,
            selectedId = vm.selectedPaymentMethodId,
            lang = lang,
            onSelect = { vm.selectPaymentMethod(it) },
            onAddCard = { number, holder -> vm.addCard(number, holder) },
            onDeleteCard = { vm.deleteCard(it) },
            onDismiss = { isPaymentDialogVisible = false }
        )
    }

    if (isAddCarDialogVisible) {
        AlertDialog(
            onDismissRequest = { isAddCarDialogVisible = false },
            containerColor = Surface1,
            title = {
                Text(if (lang == Lang.KZ) "Өз көлігіңді жалға беру" else "Сдать свое авто в аренду", color = TextHi)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            label = { Text(if (lang == Lang.KZ) "Бағасы ₸ / сағат" else "Цена ₸ / час") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = newCarPriceDay,
                            onValueChange = { newCarPriceDay = it.filter { c -> c.isDigit() } },
                            label = { Text(if (lang == Lang.KZ) "Бағасы ₸ / тәулік" else "Цена ₸ / сутки") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = newCarOwnerKaspi,
                        onValueChange = { newCarOwnerKaspi = it },
                        label = { Text(if (lang == Lang.KZ) "Ақша қабылдайтын Kaspi Номер" else "Kaspi Номер для перевода денег") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = newCarLocation,
                        onValueChange = { newCarLocation = it },
                        label = { Text(if (lang == Lang.KZ) "Астанадағы орналасқан жері" else "Локация в Астане") },
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = newCarImageUrl,
                        onValueChange = { newCarImageUrl = it },
                        label = { Text(if (lang == Lang.KZ) "Сурет URL (міндетті емес)" else "Ссылка на фото (необязательно)") },
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text(
                        if (lang == Lang.KZ) "Клиенттер төлемді осы Kaspi номерге ($newCarOwnerKaspi) аударады" else "Клиенты будут переводить оплату на ваш Kaspi номер ($newCarOwnerKaspi)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Ice
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCarName.isNotBlank()) {
                            val pHour = newCarPriceHour.toIntOrNull() ?: 1200
                            val pDay = newCarPriceDay.toIntOrNull() ?: 15000
                            vm.addCustomCar(
                                name = newCarName.trim(),
                                pricePerHour = pHour,
                                pricePerDay = pDay,
                                location = newCarLocation.trim(),
                                imageUrl = newCarImageUrl.trim(),
                                ownerName = currentUser?.name ?: "Падаланушы",
                                ownerEmail = currentUser?.email ?: "user@smartcarshare.kz",
                                ownerKaspiNumber = newCarOwnerKaspi.trim()
                            )
                            isAddCarDialogVisible = false
                            newCarName = ""
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
                            editEmail.trim()
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
