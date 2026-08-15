package ir.mehdi.imposter.presentation.screen.discussion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.mehdi.imposter.presentation.theme.BackgroundGradient
import ir.mehdi.imposter.presentation.theme.Cyan400
import ir.mehdi.imposter.presentation.theme.CyanButtonGradient
import ir.mehdi.imposter.presentation.theme.Purple500
import ir.mehdi.imposter.presentation.theme.SurfaceDark
import ir.mehdi.imposter.presentation.theme.TextMuted
import ir.mehdi.imposter.presentation.theme.TextPrimary

/**
 * Shown right after every player has seen their role card and before the
 * discussion starts. Announces the randomly chosen starting player.
 */
@Composable
fun DiscussionStartScreen(
    onStartDiscussion: () -> Unit,
    viewModel: DiscussionStartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var contentVisible by remember { mutableStateOf(false) }
    val iconScale by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 0f,
        animationSpec = tween(700),
        label = "discussionIconScale"
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
                // ── Header ─────────────────────────────────
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(600)) + scaleIn(animationSpec = tween(600))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "نمایش کارت‌ها تمام شد",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "حالا نوبت گفتگو است",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // ── Starting player card ──────────────────
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(700, delayMillis = 150)) + scaleIn(animationSpec = tween(700))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(iconScale)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = Cyan400.copy(alpha = 0.12f),
                                spotColor = Purple500.copy(alpha = 0.12f),
                                clip = false
                            )
                            .clip(RoundedCornerShape(24.dp))
                            .background(SurfaceDark)
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Cyan400.copy(alpha = 0.3f),
                                        Purple500.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(vertical = 36.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Cyan400.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Cyan400
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "شروع‌کننده بحث",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "بازیکن ${(uiState.startingPlayerIndex ?: 0) + 1}",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "بحث را از این بازیکن شروع کنید",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // ── Start discussion button ───────────────
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(700, delayMillis = 350))
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
                                viewModel.beginDiscussion()
                                onStartDiscussion()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                                tint = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "شروع بحث",
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