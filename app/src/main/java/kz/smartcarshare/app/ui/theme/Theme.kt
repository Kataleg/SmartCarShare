package kz.smartcarshare.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val Scheme = darkColorScheme(
    primary = Amber,
    onPrimary = AmberOnDark,
    secondary = Ice,
    onSecondary = Navy900,
    background = Navy800,
    onBackground = TextHi,
    surface = Surface1,
    onSurface = TextHi,
    surfaceVariant = Surface2,
    onSurfaceVariant = TextMid
)

@Composable
fun SmartCarShareTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Scheme,
        typography = AppTypography,
        content = content
    )
}