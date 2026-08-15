package ir.mehdi.imposter.presentation.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.mehdi.imposter.presentation.theme.BackgroundGradient
import ir.mehdi.imposter.presentation.theme.Cyan400
import ir.mehdi.imposter.presentation.theme.Purple500
import ir.mehdi.imposter.presentation.theme.SurfaceDark
import ir.mehdi.imposter.presentation.theme.TextMuted
import ir.mehdi.imposter.presentation.theme.TextPrimary
import ir.mehdi.imposter.presentation.theme.TextSecondary
import ir.mehdi.imposter.presentation.theme.TopBarGradientNew

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    var contentVisible by remember { mutableStateOf(false) }

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
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Top Bar ───────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TopBarGradientNew)
                        .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "بازگشت",
                                tint = TextSecondary
                            )
                        }
                        Text(
                            text = "تنظیمات",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }

                // Divider
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

                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(400)) + scaleIn(initialScale = 0.95f, animationSpec = tween(400))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // About App Card
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
                                .padding(22.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "درباره برنامه",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Cyan400
                                )

                                Spacer(modifier = Modifier.height(22.dp))

                                SettingsRow(
                                    icon = Icons.Default.Info,
                                    label = "نام برنامه",
                                    value = "Abusufyan Family"
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                SettingsRow(
                                    icon = Icons.Default.Person,
                                    label = "توسعه‌دهنده",
                                    value = "طراحی شده توسط شاپور"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "نسخه ۱.۰.۰",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = TextMuted
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
    }
}
