package kz.smartcarshare.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(vertical = 14.dp, horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Amber,
            contentColor = AmberOnDark,
            disabledContainerColor = Amber.copy(alpha = 0.3f),
            disabledContentColor = AmberOnDark.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            letterSpacing = 0.2.sp
        )
    }
}

@Composable
fun OutlineActionButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(vertical = 14.dp, horizontal = 20.dp),
        border = BorderStroke(1.dp, BorderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Surface2.copy(alpha = 0.4f),
            contentColor = TextHi
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            letterSpacing = 0.2.sp
        )
    }
}

@Composable
fun SectionTitle(
    title: String,
    trailing: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Amber)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextHi,
                fontWeight = FontWeight.Bold
            )
        }
        if (trailing.isNotEmpty()) {
            Text(
                text = trailing,
                style = MaterialTheme.typography.bodySmall,
                color = TextMid,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    accent: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(if (accent) Ice.copy(alpha = 0.16f) else Surface2)
            .border(
                1.dp,
                if (accent) Ice.copy(alpha = 0.35f) else BorderColor,
                RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (accent) Ice else TextMid,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
fun IconBadge(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    bg: Color = Ice,
    tint: Color = Color(0xFF0B0D28),
    sizeDp: Int = 54
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(RoundedCornerShape((sizeDp * 0.28f).dp))
            .background(bg)
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape((sizeDp * 0.28f).dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size((sizeDp * 0.48f).dp)
        )
    }
}

@Composable
fun BackTopBar(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Surface1)
                .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Артқа",
                tint = TextHi,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = TextHi,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ModernCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    border: BorderStroke = BorderStroke(1.dp, BorderColor),
    backgroundColor: Color = Surface1,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardShape = RoundedCornerShape(22.dp)
    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        shape = cardShape,
        color = backgroundColor,
        border = border,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}