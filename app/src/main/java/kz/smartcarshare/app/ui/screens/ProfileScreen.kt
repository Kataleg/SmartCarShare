package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.theme.*

@Composable
fun ProfileScreen(
    currentUser: LocalUser? = null,
    onChatClick: () -> Unit,
    onB2BClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val initial = currentUser?.name?.trim()?.firstOrNull()?.uppercaseChar()?.toString() ?: "П"
    val displayName = currentUser?.name?.takeIf { it.isNotBlank() } ?: "Пайдаланушы"
    val displayPhone = currentUser?.phone?.takeIf { it.isNotBlank() } ?: currentUser?.email ?: "Тіркелгі жоқ"

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
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(displayName, style = MaterialTheme.typography.titleLarge, color = TextHi)
                    Text(displayPhone, style = MaterialTheme.typography.bodySmall, color = TextMid)
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
                MenuRow(Icons.Filled.Chat, "ЖИ-көмекшіден көлік сұрау", onClick = onChatClick)
                MenuRow(Icons.Filled.DirectionsCar, "Менің сапарларым", sub = "0 аяқталған сапар")
                MenuRow(Icons.Filled.CreditCard, "Төлем әдістері", sub = "Карта қосу")
                MenuRow(Icons.Filled.Apartment, "Компания үшін", onClick = onB2BClick, showDivider = true)

                if (currentUser != null) {
                    MenuRow(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = "Аккаунттан шығу",
                        onClick = onLogoutClick,
                        showDivider = false
                    )
                } else {
                    MenuRow(
                        icon = Icons.Filled.PersonAdd,
                        title = "Тіркелу / Кіру",
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
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Тіл", style = MaterialTheme.typography.labelLarge, color = TextHi)
                Text("ҚАЗ  /  РУС", style = MaterialTheme.typography.bodyMedium, color = TextMid)
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
