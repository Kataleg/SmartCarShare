package kz.smartcarshare.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val Scheme = lightColorScheme(
    primary = Amber,
    onPrimary = AmberOnDark,
    primaryContainer = Surface2,
    onPrimaryContainer = TextHi,
    secondary = Ice,
    onSecondary = Surface1,
    secondaryContainer = Surface3,
    onSecondaryContainer = Ice,
    background = Navy800,
    onBackground = TextHi,
    surface = Surface1,
    onSurface = TextHi,
    surfaceVariant = Surface2,
    onSurfaceVariant = TextMid,
    outline = BorderColor,
    outlineVariant = BorderLight
)

@Composable
fun SmartCarShareTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Scheme,
        typography = AppTypography,
        content = content
    )
}
