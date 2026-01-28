package org.adt.presentation.screens.example

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.isActive
import me.trishiraj.shadowglow.ShadowBlurStyle
import me.trishiraj.shadowglow.shadowGlow
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun ExampleScreen(innerPadding: PaddingValues = PaddingValues()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFB552FA), Color(0xFFAEBAF8)
                    )
                )
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val infiniteTransition = rememberInfiniteTransition()

        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween<Float>(
                    durationMillis = 3000,
                    easing = FastOutLinearInEasing,
                ),
            )
        )


        Spacer(modifier = Modifier.height(140.dp))
        Card(
            modifier = Modifier
                .rotate(rotation)
                .shadowGlow(
                    color = Color.Green.copy(alpha = 0.7f),
                    borderRadius = 18.dp,
                    blurRadius = 26.dp,
                    spread = 4.dp,
                    enableBreathingEffect = true,
                    breathingEffectIntensity = 5.dp,
                    breathingDurationMillis = 2000,
                    enableGlowTrail = true,
                    glowTrailWidth = 12.dp,
                    glowTrailBlurRadius = 26.dp,
                    glowTrailLengthDegrees = 90f,
                    glowTrailDurationMillis = 2000,
                    glowTrailClockwise = false,
                    glowTrailAlpha = 0.8f
                )
                .fillMaxWidth(0.8f)
                .height(180.dp),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Shadowed Card", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExampleScreenPreview() {
    VolunteersCaseTheme {
        ExampleScreen()
    }
}