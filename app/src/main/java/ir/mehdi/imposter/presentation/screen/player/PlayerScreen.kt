package ir.mehdi.imposter.presentation.screen.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.mehdi.imposter.presentation.theme.BackgroundGradient
import ir.mehdi.imposter.presentation.theme.Cyan400
import ir.mehdi.imposter.presentation.theme.CyanButtonGradient
import ir.mehdi.imposter.presentation.theme.Pink500
import ir.mehdi.imposter.presentation.theme.Purple500
import ir.mehdi.imposter.presentation.theme.PurpleButtonGradient
import ir.mehdi.imposter.presentation.theme.SurfaceDark
import ir.mehdi.imposter.presentation.theme.SurfaceLightAlt
import ir.mehdi.imposter.presentation.theme.TextMuted
import ir.mehdi.imposter.presentation.theme.TextPrimary
import ir.mehdi.imposter.presentation.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun PlayerScreen(
    onGameFinished: () -> Unit,
    viewModel: PlayerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }
    val cardScale = remember { Animatable(1f) }

    LaunchedEffect(uiState.gameFinished) {
        if (uiState.gameFinished) {
            onGameFinished()
        }
    }

    val gameState = uiState.gameState ?: return

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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // ── Player Indicator ──────────────────────
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Purple500.copy(alpha = 0.1f))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "بازیکن ${gameState.currentPlayerIndex + 1} از ${gameState.totalPlayers}",
                        style = MaterialTheme.typography.titleSmall,
                        color = Purple500,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "گوشی را به بازیکن بعدی بدهید",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Card with Flip Animation ──────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                        .scale(cardScale.value)
                        .graphicsLayer {
                            rotationY = rotation.value
                            cameraDistance = 12f * density
                        }
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = Cyan400.copy(alpha = 0.12f),
                            spotColor = Purple500.copy(alpha = 0.12f),
                            clip = false
                        )
                        .clip(RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation.value <= 90f) {
                        // ── Card Back (Hidden) ────────────
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SurfaceDark)
                                .border(
                                    width = 2.dp,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Purple500.copy(alpha = 0.15f),
                                            Cyan400.copy(alpha = 0.05f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                // Decorative dots
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    repeat(3) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(Purple500.copy(alpha = 0.3f))
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Icon(
                                    imageVector = Icons.Default.TouchApp,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Purple500.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "کارت خود را مشاهده کنید",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "دکمه زیر را فشار دهید",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        // ── Card Front (Revealed) ─────────
                        val currentPlayer = gameState.playerCards.getOrNull(gameState.currentPlayerIndex)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { rotationY = 180f }
                                .background(SurfaceDark)
                                .border(
                                    width = 2.dp,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Purple500.copy(alpha = 0.15f),
                                            Cyan400.copy(alpha = 0.05f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                if (currentPlayer?.isImposter == true) {
                                    // ── Imposter Card ──────
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = Pink500
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "شما ایمپاستر هستید",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Pink500,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    val myHint = currentPlayer.hint
                                    if (myHint != null) {
                                        // Hints are enabled: show only this
                                        // imposter's own unique hint.
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Cyan400.copy(alpha = 0.08f))
                                                .border(
                                                    width = 1.dp,
                                                    color = Cyan400.copy(alpha = 0.25f),
                                                    shape = RoundedCornerShape(14.dp)
                                                )
                                                .padding(horizontal = 16.dp, vertical = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "راهنمای شما",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Cyan400
                                                )
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = myHint,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextPrimary,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    } else {
                                        // Hints are off: no hint UI at all.
                                        Text(
                                            text = "شما از کلمه چیزی نمی‌دانید؛\nدر بحث شرکت کنید و حدس بزنید",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextMuted,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "سعی کنید لو نروید\u200F!",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextMuted,
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    // ── Normal Player Card ──
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                        tint = Cyan400
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = currentPlayer?.word ?: "",
                                        style = MaterialTheme.typography.displayLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // ── Action Button ─────────────────────────
                AnimatedVisibility(
                    visible = !uiState.isCardRevealed,
                    enter = fadeIn(tween(300)),
                    exit = fadeOut(tween(200))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(18.dp),
                                ambientColor = Purple500.copy(alpha = 0.3f),
                                spotColor = Purple500.copy(alpha = 0.4f),
                                clip = true
                            )
                            .clip(RoundedCornerShape(18.dp))
                            .background(PurpleButtonGradient)
                            .clickable {
                                scope.launch {
                                    cardScale.animateTo(0.97f, tween(100))
                                    rotation.animateTo(
                                        targetValue = 180f,
                                        animationSpec = tween(
                                            durationMillis = 600,
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                    cardScale.animateTo(1f, tween(200))
                                    viewModel.revealCard()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "نمایش کارت",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }

                AnimatedVisibility(
                    visible = uiState.isCardRevealed,
                    enter = fadeIn(tween(300)),
                    exit = fadeOut(tween(200))
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
                            .clickable {
                                scope.launch {
                                    cardScale.animateTo(0.97f, tween(100))
                                    rotation.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(
                                            durationMillis = 450,
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                    cardScale.animateTo(1f, tween(200))
                                    viewModel.nextPlayer()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "بازیکن بعدی",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = TextPrimary
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = uiState.isCardRevealed,
                    enter = fadeIn(tween(500))
                ) {
                    Text(
                        text = "گوشی را به بازیکن بعدی بدهید",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}
