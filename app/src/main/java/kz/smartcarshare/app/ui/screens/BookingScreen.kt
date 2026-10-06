package kz.smartcarshare.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.Tariff
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.PaymentType
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

@Composable
fun BookingScreen(
    vm: RentalViewModel,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    val car = vm.selectedCar ?: return
    val lang = vm.currentLang
    val context = LocalContext.current
    val tariffLabel = if (vm.tariff == Tariff.HOUR) Strings.get(lang, "hourly") else Strings.get(lang, "daily")

    var isPaymentDialogVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(title = Strings.get(lang, "confirm_payment"), onBack = onBack)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                Text(Strings.get(lang, "selected_car"), style = MaterialTheme.typography.bodySmall, color = Amber)
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 12.dp)
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

            if (car.company.contains("CashAuto", ignoreCase = true)) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                            .background(Surface1, RoundedCornerShape(18.dp))
                            .border(1.dp, Amber, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Amber, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Language, contentDescription = null, tint = Navy900, modifier = Modifier.size(20.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "CASHAUTO КОРПОРАТИВТІК ШЛҮЗІ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Amber,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    " cashauto.kz ресми сайты арқылы төлеу",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextHi
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cashauto.kz/ru/"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Amber)
                        ) {
                            Text(if (lang == Lang.KZ) "CashAuto сайтында төлеу (cashauto.kz)" else "Оплатить на сайте CashAuto (cashauto.kz)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                            .background(Surface1, RoundedCornerShape(18.dp))
                            .border(1.dp, Amber.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(Amber, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.QrCodeScanner, contentDescription = null, tint = Navy900, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(
                                    if (lang == Lang.KZ) "КӨЛІК ИЕСІНЕ KASPI ПЕРЕВОД" else "ПРЯМОЙ KASPI ПЕРЕВОД ВЛАДЕЛЬЦУ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Amber,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = car.company,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextHi
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .background(Surface2, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    if (lang == Lang.KZ) "Ақша аударатын Kaspi Номер:" else "Kaspi Номер для перевода средств:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMid
                                )
                                Text(
                                    text = car.ownerKaspiNumber,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Ice,
                                    fontWeight = FontWeight.Bold
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
                            Text(Strings.get(lang, "autopay_title"), style = MaterialTheme.typography.labelLarge, color = TextHi)
                            Text(
                                Strings.get(lang, "autopay_sub"),
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
                            .clickable { isPaymentDialogVisible = true }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 38.dp, height = 26.dp)
                                .background(if (vm.selectedPaymentMethod.type == PaymentType.KASPI_PAY) Amber else Ice, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (vm.selectedPaymentMethod.type == PaymentType.KASPI_PAY) "Kaspi" else "CARD",
                                color = Navy900,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(vm.selectedPaymentMethod.title, style = MaterialTheme.typography.labelLarge, color = TextHi)
                            Text(vm.selectedPaymentMethod.subtitle, style = MaterialTheme.typography.bodySmall, color = TextMid)
                        }
                        Text(Strings.get(lang, "change"), style = MaterialTheme.typography.bodySmall, color = Ice, fontWeight = FontWeight.Bold)
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
                    Text(Strings.get(lang, "pre_calculation"), style = MaterialTheme.typography.bodySmall, color = Amber)
                    SummaryRow(Strings.get(lang, "tariff"), tariffLabel)
                    val unit = if (vm.tariff == Tariff.HOUR) (if (lang == Lang.KZ) "/ сағат" else "/ час") else (if (lang == Lang.KZ) "/ тәулік" else "/ сутки")
                    SummaryRow(Strings.get(lang, "estimated_price"), "${formatTenge(vm.currentPrice())} $unit")
                    SummaryRow(Strings.get(lang, "insurance"), if (vm.insuranceOn) Strings.get(lang, "added") else Strings.get(lang, "none"))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy800)
                .padding(20.dp)
        ) {
            PrimaryButton(text = Strings.get(lang, "confirm_rental"), onClick = onConfirm)
        }
    }

    if (isPaymentDialogVisible) {
        PaymentMethodsDialog(
            paymentMethods = vm.paymentMethods,
            selectedId = vm.selectedPaymentMethodId,
            lang = vm.currentLang,
            onSelect = { vm.selectPaymentMethod(it) },
            onAddCard = { number, holder -> vm.addCard(number, holder) },
            onDeleteCard = { vm.deleteCard(it) },
            onDismiss = { isPaymentDialogVisible = false }
        )
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
