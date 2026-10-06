package kz.smartcarshare.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.model.PaymentMethod
import kz.smartcarshare.app.data.model.PaymentType
import kz.smartcarshare.app.ui.theme.*

@Composable
fun PaymentMethodsDialog(
    paymentMethods: List<PaymentMethod>,
    selectedId: String,
    lang: Lang,
    onSelect: (String) -> Unit,
    onAddCard: (number: String, holder: String) -> Unit,
    onDeleteCard: (String) -> Unit = {},
    onDismiss: () -> Unit
) {
    var isAddCardMode by remember { mutableStateOf(false) }
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var cardExp by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }

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

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface1,
        title = {
            Text(
                text = if (isAddCardMode) {
                    if (lang == Lang.KZ) "Жаңа карта қосу" else "Добавить новую карту"
                } else {
                    if (lang == Lang.KZ) "Төлем әдістері" else "Способы оплаты"
                },
                color = TextHi,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!isAddCardMode) {
                    paymentMethods.forEach { item ->
                        val isSelected = item.id == selectedId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) Amber.copy(alpha = 0.12f) else Surface2,
                                    RoundedCornerShape(14.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) Amber else BorderColor,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onSelect(item.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (item.type == PaymentType.KASPI_PAY) Amber else Ice,
                                        RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (item.type == PaymentType.KASPI_PAY) Icons.Filled.QrCodeScanner else Icons.Filled.CreditCard,
                                    contentDescription = null,
                                    tint = Navy900,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, style = MaterialTheme.typography.labelLarge, color = TextHi)
                                Text(item.subtitle, style = MaterialTheme.typography.bodySmall, color = TextMid)
                            }

                            if (isSelected) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Amber, modifier = Modifier.size(20.dp))
                            }

                            if (paymentMethods.size > 1) {
                                IconButton(
                                    onClick = { onDeleteCard(item.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Жою",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { isAddCardMode = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Ice)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (lang == Lang.KZ) "+ Банк картасын қосу" else "+ Добавить банковскую карту")
                    }
                } else {
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { if (it.length <= 16) cardNumber = it },
                        label = { Text(if (lang == Lang.KZ) "Карта нөмірі (16 сан)" else "Номер карты (16 цифр)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = cardHolder,
                        onValueChange = { cardHolder = it },
                        label = { Text(if (lang == Lang.KZ) "Карта иесінің аты-жөні" else "Имя на карте (как на латинице)") },
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = cardExp,
                            onValueChange = { if (it.length <= 5) cardExp = it },
                            label = { Text("MM/YY") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = cardCvv,
                            onValueChange = { if (it.length <= 3) cardCvv = it },
                            label = { Text("CVV") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (isAddCardMode) {
                Button(
                    onClick = {
                        if (cardNumber.length >= 12) {
                            onAddCard(cardNumber, cardHolder)
                            isAddCardMode = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                ) {
                    Text(if (lang == Lang.KZ) "Қосу" else "Добавить", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                ) {
                    Text(if (lang == Lang.KZ) "Дайын" else "Готово", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = {
                if (isAddCardMode) isAddCardMode = false else onDismiss()
            }) {
                Text(if (lang == Lang.KZ) "Бас тарту" else "Отмена", color = TextMid)
            }
        }
    )
}
