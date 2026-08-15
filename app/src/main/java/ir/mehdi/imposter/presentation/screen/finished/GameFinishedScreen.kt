package ir.mehdi.imposter.presentation.screen.finished

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.mehdi.imposter.presentation.theme.BackgroundGradient
import ir.mehdi.imposter.presentation.theme.Cyan400
import ir.mehdi.imposter.presentation.theme.CyanButtonGradient
import ir.mehdi.imposter.presentation.theme.Purple500
import ir.mehdi.imposter.presentation.theme.TextMuted
import ir.mehdi.imposter.presentation.theme.TextPrimary

@Composable
fun GameFinishedScreen(
    onBackToHome: () -> Unit
) {
    var contentVisible by remember { mutableStateOf(false) }
    val iconScale by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 0f,
        animationSpec = tween(700),
        label = "finishedIconScale"
    )

    LaunchedEffect(Unit) {
        contentVisible = true
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Decorative illustration ───────────────
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = scaleIn(animationSpec = tween(600)) + fadeIn(tween(600))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Abstract illustration using compose primitives
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .scale(iconScale),
                            contentAlignment = Alignment.Center
                        ) {
                            // Outer ring
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Purple500.copy(alpha = 0.15f))
                            )
                            // Inner circle
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Cyan400.copy(alpha = 0.4f),
                                                Cyan400.copy(alpha = 0.1f)
                                            )
                                        )
                                    )
                            )
                            // People icon
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                modifier = Modifier.size(38.dp),
                                tint = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Decorative dots row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (index == 1) Purple500.copy(alpha = 0.9f)
                                            else TextPrimary.copy(alpha = 0.25f)
                                        )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ── Title text ────────────────────────────
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(800, delayMillis = 200))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "بازی شروع شد",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "ایمپاستر را پیدا کنید",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Cyan400,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "بحث کنید و رأی دهید",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(56.dp))

                // ── Home Button ───────────────────────────
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(800, delayMillis = 600))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(18.dp),
                                ambientColor = Cyan400.copy(alpha = 0.3f),
                                spotColor = Cyan400.copy(alpha = 0.4f),
                                clip = true
                            )
                            .clip(RoundedCornerShape(18.dp))
                            .background(CyanButtonGradient)
                            .clickable(onClick = onBackToHome),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "بازگشت به خانه",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
