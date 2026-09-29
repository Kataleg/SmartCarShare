package kz.smartcarshare.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    headlineSmall = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 22.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 19.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 10.sp)
)