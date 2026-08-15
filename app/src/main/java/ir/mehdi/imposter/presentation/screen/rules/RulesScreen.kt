package ir.mehdi.imposter.presentation.screen.rules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.mehdi.imposter.presentation.theme.BackgroundGradient
import ir.mehdi.imposter.presentation.theme.Cyan400
import ir.mehdi.imposter.presentation.theme.Purple500
import ir.mehdi.imposter.presentation.theme.SurfaceDark
import ir.mehdi.imposter.presentation.theme.TextPrimary
import ir.mehdi.imposter.presentation.theme.TextSecondary
import ir.mehdi.imposter.presentation.theme.TextMuted
import ir.mehdi.imposter.presentation.theme.TopBarGradientNew

@Composable
fun RulesScreen(
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
                            text = "قوانین بازی",
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
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        RuleCard(
                            number = "۱",
                            title = "تنظیم بازی",
                            description = "تعداد بازیکنان (۳ تا ۹ نفر)، تعداد ایمپاسترها (۱ تا ۳ نفر) و نوع کلمه (عادی یا حرفه‌ای) را انتخاب کنید."
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        RuleCard(
                            number = "۲",
                            title = "انتخاب کلمه",
                            description = "یک کلمه تصادفی از نوع انتخاب شده به همه بازیکنان نشان داده می‌شود، به جز ایمپاسترها. هر ایمپاستر فقط یک راهنمای تک‌کلمه‌ای و متفاوت از سایر ایمپاسترها دریافت می‌کند."
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        RuleCard(
                            number = "۳",
                            title = "نقش‌ها",
                            description = "بازیکنان عادی کلمه را می‌بینند. ایمپاسترها فقط متوجه می‌شوند که ایمپاستر هستند و کلمه را نمی‌بینند، اما یک راهنما یا سرنخ دریافت می‌کنند که به آنها کمک می‌کند کلمه اصلی را حدس بزنند، بدون اینکه کلمه مستقیماً فاش شود."
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        RuleCard(
                            number = "۴",
                            title = "شروع بازی",
                            description = "پس از اینکه همه بازیکنان کارت خود را دیدند، بازی شروع می‌شود. همه باید بحث کنند و سعی کنند ایمپاستر را پیدا کنند."
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        RuleCard(
                            number = "۵",
                            title = "هدف ایمپاستر",
                            description = "ایمپاستر باید طوری رفتار کند که انگار کلمه را می‌داند و لو نرود."
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        RuleCard(
                            number = "۶",
                            title = "رأی‌گیری",
                            description = "پس از بحث، بازیکنان رأی می‌دهند که چه کسی ایمپاستر است. اگر ایمپاستر پیدا شود، بازیکنان عادی برنده می‌شوند."
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        RuleCard(
                            number = "۷",
                            title = "برنده شدن ایمپاستر",
                            description = "اگر ایمپاستر بتواند تا آخر بازی لو نرود و کسی به او شک نکند، ایمپاستر برنده می‌شود."
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RuleCard(
    number: String,
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Purple500.copy(alpha = 0.06f),
                spotColor = Purple500.copy(alpha = 0.08f),
                clip = false
            )
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Number badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Cyan400.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Cyan400
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )
            }
        }
    }
}
