package ir.mehdi.imposter.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.mehdi.imposter.presentation.theme.BackgroundDarker
import ir.mehdi.imposter.presentation.theme.BackgroundGradient
import ir.mehdi.imposter.presentation.theme.BorderSubtle
import ir.mehdi.imposter.presentation.theme.Cyan400
import ir.mehdi.imposter.presentation.theme.Purple500
import ir.mehdi.imposter.presentation.theme.SurfaceDark
import ir.mehdi.imposter.presentation.theme.TextMuted
import ir.mehdi.imposter.presentation.theme.TextPrimary
import ir.mehdi.imposter.presentation.theme.TopBarGradientNew

@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onRules: () -> Unit,
    onAbout: () -> Unit
) {
    var buttonsVisible by remember { mutableStateOf(false) }
    var pressedButton by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        buttonsVisible = true
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGradient)
                .padding(paddingValues)
        ) {
            // ── Full-screen Layout ──────────────────────────
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Top App Bar ─────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TopBarGradientNew)
                        .padding(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left avatar circle
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.sweepGradient(
                                        colors = listOf(Purple500, Cyan400, Purple500)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gamepad,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // App name
                        Text(
                            text = "Imposter",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // ── Divider with Cyan accent ────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Cyan400.copy(alpha = 0.3f),
                                    Purple500.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // ── Menu Buttons (centered) ─────────────────
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = buttonsVisible,
                        enter = fadeIn(animationSpec = tween(800)) +
                                scaleIn(initialScale = 0.5f, animationSpec = spring(dampingRatio = 0.6f))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // ── "شروع بازی" ─────────────────
                            PremiumMenuButton(
                                text = "شروع بازی",
                                icon = Icons.Default.Gamepad,
                                accentColor = Purple500,
                                isPressed = pressedButton == "start",
                                onClick = {
                                    pressedButton = "start"
                                    onStartGame()
                                }
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // ── "قوانین بازی" ───────────────
                            PremiumMenuButton(
                                text = "قوانین بازی",
                                icon = Icons.AutoMirrored.Filled.MenuBook,
                                accentColor = Cyan400,
                                isPressed = pressedButton == "rules",
                                onClick = {
                                    pressedButton = "rules"
                                    onRules()
                                }
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // ── "درباره برنامه" ─────────────
                            PremiumMenuButton(
                                text = "درباره برنامه",
                                icon = Icons.Default.Info,
                                accentColor = Purple500,
                                isPressed = pressedButton == "about",
                                onClick = {
                                    pressedButton = "about"
                                    onAbout()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Reusable Premium Menu Button
// ═══════════════════════════════════════════════════════════════

@Composable
private fun PremiumMenuButton(
    text: String,
    icon: ImageVector,
    accentColor: Color,
    isPressed: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.5f),
        label = "menuBtnScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = accentColor.copy(alpha = 0.15f),
                spotColor = accentColor.copy(alpha = 0.2f),
                clip = false
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SurfaceDark,
                        BackgroundDarker
                    )
                )
            )
            .then(
                Modifier.shadow(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = true
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Border glow overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            Color.Transparent,
                            accentColor.copy(alpha = 0.05f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = accentColor
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}
