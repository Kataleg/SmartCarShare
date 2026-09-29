package kz.smartcarshare.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.smartcarshare.app.ui.theme.*

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Amber,
            contentColor = AmberOnDark,
            disabledContainerColor = Amber.copy(alpha = 0.35f),
            disabledContentColor = AmberOnDark.copy(alpha = 0.6f)
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun OutlineActionButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        border = BorderStroke(1.dp, BorderColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextHi)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SectionTitle(title: String, trailing: String = "") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = TextHi)
        if (trailing.isNotEmpty()) {
            Text(trailing, style = MaterialTheme.typography.bodySmall, color = TextMid)
        }
    }
}

@Composable
fun Chip(text: String, accent: Boolean = false) {
    Box(
        modifier = Modifier
            .background(
                color = if (accent) Ice.copy(alpha = 0.12f) else Surface2,
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                1.dp,
                if (accent) Ice.copy(alpha = 0.3f) else BorderColor,
                RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (accent) Ice else TextMid
        )
    }
}

@Composable
fun IconBadge(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    bg: Color = Ice,
    tint: Color = Color(0xFF0B0D28),
    sizeDp: Int = 58
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .background(bg, RoundedCornerShape((sizeDp * 0.24f).dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size((sizeDp * 0.5f).dp)
        )
    }
}

@Composable
fun BackTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Surface1, RoundedCornerShape(12.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Артқа",
                    tint = TextHi,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Text(title, style = MaterialTheme.typography.titleLarge, color = TextHi)
    }
}