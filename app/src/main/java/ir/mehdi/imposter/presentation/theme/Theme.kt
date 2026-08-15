package ir.mehdi.imposter.presentation.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ═══════════════════════════════════════════════════════════════
// Dark-Only Color Scheme
// ═══════════════════════════════════════════════════════════════

private val ImposterColorScheme = darkColorScheme(
    primary             = DarkPrimary,
    onPrimary           = DarkOnPrimary,
    primaryContainer    = DarkPrimaryContainer,
    onPrimaryContainer  = DarkOnPrimaryContainer,
    secondary           = DarkSecondary,
    onSecondary         = DarkOnSecondary,
    secondaryContainer  = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary            = DarkTertiary,
    onTertiary          = DarkOnTertiary,
    tertiaryContainer   = DarkTertiaryContainer,
    onTertiaryContainer  = DarkOnTertiaryContainer,
    background          = DarkBackground,
    onBackground        = DarkOnBackground,
    surface             = DarkSurface,
    onSurface           = DarkOnSurface,
    surfaceVariant      = DarkSurfaceVariant,
    onSurfaceVariant    = DarkOnSurfaceVariant,
    error               = DarkError,
    onError             = DarkOnError
)

// ═══════════════════════════════════════════════════════════════
// Reusable Gradient Brushes
// ═══════════════════════════════════════════════════════════════

/** Subtle background gradient: dark navy → slightly lighter navy */
val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0B1020),
        Color(0xFF0D152A),
        Color(0xFF0F1830)
    )
)

/** Purple gradient for primary buttons */
val PurpleButtonGradient = Brush.horizontalGradient(
    colors = listOf(Purple500, Purple600)
)

/** Cyan gradient for secondary buttons */
val CyanButtonGradient = Brush.horizontalGradient(
    colors = listOf(Cyan500, Color(0xFF00A8C8))
)

/** Pink gradient for imposter elements */
val PinkButtonGradient = Brush.horizontalGradient(
    colors = listOf(Pink500, PinkDark)
)

/** Subtle purple glow gradient */
val PurpleGlowGradient = Brush.verticalGradient(
    colors = listOf(
        Purple500.copy(alpha = 0.12f),
        Purple500.copy(alpha = 0.02f)
    )
)

/** Subtle cyan glow gradient */
val CyanGlowGradient = Brush.verticalGradient(
    colors = listOf(
        Cyan400.copy(alpha = 0.10f),
        Cyan400.copy(alpha = 0.02f)
    )
)

/** Top bar gradient */
val TopBarGradientNew = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF101830),
        Color(0xFF0D152A),
        Color(0xFF0B1020)
    )
)

/** Card background gradient */
val CardGradient = Brush.verticalGradient(
    colors = listOf(
        SurfaceDark,
        SurfaceLightAlt
    )
)

// ═══════════════════════════════════════════════════════════════
// Theme
// ═══════════════════════════════════════════════════════════════

@Composable
fun ImposterTheme(
    darkTheme: Boolean = true, // Always dark
    content: @Composable () -> Unit
) {
    // The entire UI is Persian and there is no values-fa locale, so on a
    // non-Persian device LocalLayoutDirection defaults to LTR. Compose Text
    // derives its base textDirection from the layout direction, which makes a
    // trailing '.' (U+002E, a neutral char) resolve against an LTR paragraph
    // and jump to the wrong side of the sentence. Forcing the base layout
    // direction to RTL fixes BiDi resolution for every Text (short/long,
    // multiline, mixed Fa/En, any punctuation) without touching individual
    // strings or converting '.' to Persian punctuation.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = ImposterColorScheme,
            typography  = AppTypography,
            content     = content
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BackgroundDark.toArgb()
            @Suppress("DEPRECATION")
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
}
