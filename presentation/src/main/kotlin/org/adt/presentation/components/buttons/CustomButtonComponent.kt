package org.adt.presentation.components.buttons

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Rounded,
    style: ButtonStyle = ButtonStyle.Filled,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    colors: ButtonColorScheme? = null,
    onClick: () -> Unit,
) {
    val config = ButtonDefaultsProvider.config(variant, style)
    val isInteractable = enabled && !isLoading
    val defaultColors = ButtonDefaultsProvider.colors(variant, style, isInteractable)
    val finalColors = colors ?: defaultColors
    val finalText = if (config.uppercase) text.uppercase() else text

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val shadowElevation by animateDpAsState(
        targetValue = when {
            !isInteractable -> 0.dp
            isPressed -> 1.dp
            style == ButtonStyle.Outlined || style == ButtonStyle.Translucent -> 0.dp
            else -> 4.dp
        },
        animationSpec = tween(durationMillis = 100),
        label = "ButtonShadowAnimation"
    )

    val elegantRippleConfiguration = RippleConfiguration(
        color = Color.White.copy(alpha = 0.2f)
    )

    CompositionLocalProvider(LocalRippleConfiguration provides elegantRippleConfiguration) {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .then(if (config.height != null) Modifier.height(config.height) else Modifier)
                .shadow(elevation = shadowElevation, shape = config.shape, clip = false),
            enabled = isInteractable,
            shape = config.shape,
            interactionSource = interactionSource,
            border = finalColors.borderColor?.let { BorderStroke(2.dp, it) },
            colors = ButtonDefaults.buttonColors(
                containerColor = finalColors.containerColor,
                contentColor = finalColors.contentColor,
                disabledContainerColor = finalColors.containerColor,
                disabledContentColor = finalColors.contentColor
            ),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(config.loaderSize),
                    color = finalColors.contentColor,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = finalText,
                    style = config.textStyle.copy(
                        color = finalColors.contentColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}


@Preview
@Composable
private fun CustomButtonPreview() {
    CustomButton(
        text = "Press custom button",
        style = ButtonStyle.Translucent
    ) { }
}