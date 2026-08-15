package ir.mehdi.imposter.presentation.screen.setup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.mehdi.imposter.domain.model.GameConfig
import ir.mehdi.imposter.domain.model.WordType
import ir.mehdi.imposter.presentation.theme.BackgroundGradient
import ir.mehdi.imposter.presentation.theme.BorderSubtle
import ir.mehdi.imposter.presentation.theme.Cyan400
import ir.mehdi.imposter.presentation.theme.CyanGlowGradient
import ir.mehdi.imposter.presentation.theme.Pink500
import ir.mehdi.imposter.presentation.theme.Purple500
import ir.mehdi.imposter.presentation.theme.PurpleButtonGradient
import ir.mehdi.imposter.presentation.theme.SurfaceDark
import ir.mehdi.imposter.presentation.theme.SurfaceLightAlt
import ir.mehdi.imposter.presentation.theme.TextMuted
import ir.mehdi.imposter.presentation.theme.TextPrimary
import ir.mehdi.imposter.presentation.theme.TextSecondary
import ir.mehdi.imposter.presentation.theme.TopBarGradientNew
import ir.mehdi.imposter.presentation.theme.ErrorRed

@Composable
fun GameSetupScreen(
    onBack: () -> Unit,
    onGameStarted: () -> Unit,
    viewModel: GameSetupViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val gameState by viewModel.createdGameState.collectAsState()
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        contentVisible = true
        viewModel.resetGameStarted()
    }

    LaunchedEffect(gameState) {
        if (gameState != null) {
            onGameStarted()
        }
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
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Top Bar ───────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TopBarGradientNew)
                        .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "بازگشت",
                                tint = TextSecondary
                            )
                        }
                        Text(
                            text = "تنظیمات بازی",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }

                // Subtle divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Cyan400.copy(alpha = 0.2f), Color.Transparent)
                            )
                        )
                )

                // ── Scrollable Content ────────────────────
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(400)) + scaleIn(initialScale = 0.9f, animationSpec = tween(400)),
                    exit = fadeOut(tween(300)) + scaleOut(targetScale = 0.9f, animationSpec = tween(300))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // ── Section Header: تعداد بازیکنان ──
                        SectionHeader(title = "تعداد بازیکنان")

                        // ── Player Count Card ─────────────
                        SetupCard {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                CounterButton(
                                    icon = Icons.Default.Remove,
                                    onClick = { viewModel.updatePlayerCount(uiState.playerCount - 1) },
                                    enabled = uiState.playerCount > GameConfig.MIN_PLAYERS,
                                    color = Cyan400
                                )
                                Spacer(modifier = Modifier.width(36.dp))
                                Text(
                                    text = "${uiState.playerCount}",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Purple500
                                )
                                Spacer(modifier = Modifier.width(36.dp))
                                CounterButton(
                                    icon = Icons.Default.Add,
                                    onClick = { viewModel.updatePlayerCount(uiState.playerCount + 1) },
                                    enabled = uiState.playerCount < GameConfig.MAX_PLAYERS,
                                    color = Purple500
                                )
                            }
                        }

                        // ── Section Header: تعداد ایمپاستر ──
                        SectionHeader(title = "تعداد ایمپاستر")

                        // ── Imposter Count Card ───────────
                        SetupCard {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                GameConfig.IMPOSTER_OPTIONS.forEach { count ->
                                    val isSelected = uiState.imposterCount == count
                                    val isDisabled = count >= uiState.playerCount
                                    val buttonScale by animateFloatAsState(
                                        if (isSelected) 1.06f else 1f,
                                        spring(),
                                        label = "impScale$count"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .scale(buttonScale)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(
                                                when {
                                                    isDisabled -> SurfaceDark.copy(alpha = 0.4f)
                                                    isSelected -> Pink500.copy(alpha = 0.2f)
                                                    else -> SurfaceLightAlt
                                                }
                                            )
                                            .then(
                                                if (isSelected && !isDisabled) {
                                                    Modifier.shadow(
                                                        elevation = 6.dp,
                                                        shape = RoundedCornerShape(16.dp),
                                                        ambientColor = Pink500.copy(alpha = 0.3f),
                                                        spotColor = Pink500.copy(alpha = 0.4f),
                                                        clip = false
                                                    )
                                                } else Modifier
                                            )
                                            .clickable(
                                                enabled = !isDisabled,
                                                onClick = { viewModel.updateImposterCount(count) },
                                                role = Role.Button
                                            )
                                            .padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$count",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = when {
                                                isDisabled -> TextMuted
                                                isSelected -> Pink500
                                                else -> TextSecondary
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // ── Section Header: راهنما (Hints) ──
                        SectionHeader(title = "راهنما (Hints)")

                        // ── Hints Toggle Card ────────────
                        SetupCard {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "راهنمای ایمپاستر",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (uiState.hintsEnabled) {
                                            "هر ایمپاستر یک راهنمای مبهم و متفاوت می‌بیند"
                                        } else {
                                            "ایمپاسترها هیچ راهنمایی نمی‌بینند"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextMuted
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (uiState.hintsEnabled) "فعال" else "خاموش",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (uiState.hintsEnabled) Cyan400 else TextMuted
                                    )
                                }
                                Switch(
                                    checked = uiState.hintsEnabled,
                                    onCheckedChange = { viewModel.toggleHints() },
                                    modifier = Modifier.scale(0.9f)
                                )
                            }
                        }

                        // ── Section Header: سطح کلمه ──
                        SectionHeader(title = "سطح کلمه")

                        // ── Level Selector ───────────────
                        SetupCard {
                            LevelSelector(
                                levels = WordType.entries,
                                selectedLevel = uiState.level,
                                onLevelClick = { viewModel.updateLevel(it) }
                            )
                        }

                        // ── Error Message ──────────────────
                        AnimatedVisibility(visible = uiState.error != null) {
                            Text(
                                text = uiState.error ?: "",
                                color = ErrorRed,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                            )
                        }

                        // ── Start Button ───────────────────
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .shadow(
                                    elevation = 12.dp,
                                    shape = RoundedCornerShape(18.dp),
                                    ambientColor = Purple500.copy(alpha = 0.3f),
                                    spotColor = Purple500.copy(alpha = 0.4f),
                                    clip = true
                                )
                                .clip(RoundedCornerShape(18.dp))
                                .background(PurpleButtonGradient)
                                .clickable(enabled = !uiState.isLoading) {
                                    viewModel.startGame()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(26.dp),
                                    color = TextPrimary,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "شروع بازی",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Reusable Components
// ═══════════════════════════════════════════════════════════════

@Composable
private fun SectionHeader(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 4.dp)
    ) {
        // Cyan bullet
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Cyan400)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Cyan400
        )
    }
}

@Composable
private fun SetupCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Purple500.copy(alpha = 0.06f),
                spotColor = Purple500.copy(alpha = 0.08f),
                clip = false
            )
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

// ═══════════════════════════════════════════════════════════════
// Level Components
// ═══════════════════════════════════════════════════════════════

@Composable
private fun LevelSelector(
    levels: List<WordType>,
    selectedLevel: WordType,
    onLevelClick: (WordType) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        levels.forEach { level ->
            val isSelected = selectedLevel == level
            val cardScale by animateFloatAsState(
                if (isSelected) 1.03f else 1f,
                spring(dampingRatio = 0.6f),
                label = "levelCardScale${level.name}"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(cardScale)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isSelected) Purple500.copy(alpha = 0.16f) else SurfaceLightAlt
                    )
                    .then(
                        if (isSelected) {
                            Modifier.shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = Purple500.copy(alpha = 0.25f),
                                spotColor = Purple500.copy(alpha = 0.3f),
                                clip = false
                            )
                        } else Modifier
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Purple500.copy(alpha = 0.4f) else BorderSubtle,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable(onClick = { onLevelClick(level) }, role = Role.Button)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = level.persianName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isSelected) Purple500 else TextPrimary
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Purple500
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CounterButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    color: Color
) {
    val buttonScale by animateFloatAsState(
        if (enabled) 1f else 0.92f,
        spring(),
        label = "counterScale"
    )

    Box(
        modifier = Modifier
            .size(52.dp)
            .scale(buttonScale)
            .clip(CircleShape)
            .background(if (enabled) color.copy(alpha = 0.12f) else SurfaceLightAlt.copy(alpha = 0.4f))
            .clickable(enabled = enabled, onClick = onClick, role = Role.Button),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = if (enabled) color else TextMuted
        )
    }
}
