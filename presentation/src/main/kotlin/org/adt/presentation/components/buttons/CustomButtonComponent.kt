package org.adt.presentation.components.buttons

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
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
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Universal highly customizable button component supporting loaders and state animations
 *
 * Wraps the standard Material 3 button to provide predefined design variations,
 * loading indicators, custom shadow elevations, and localized text transform behaviors.
 *
 * @param text string resource payload to display inside the button or loader fallback
 * @param modifier modifier used for managing button sizes, padding, and layout constraints
 * @param variant layout configuration token determining shapes, heights, and casing rules
 * @param style visual style token identifying color structures like Filled or Outlined
 * @param enabled flag indicating whether the button reacts to user interactions
 * @param isLoading flag replacing the core text label with a smooth spinning indicator
 * @param colors explicit color overrides bypassing default theme provider values
 * @param onClick function to be invoked when the interactable button is pressed
 *
 * @sample [CustomButtonAllVariantsPreview]
 */
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
                .shadow(elevation = shadowElevation, shape = config.shape, clip = false)
                .background(color = finalColors.containerColor, shape = config.shape),
            enabled = isInteractable,
            shape = config.shape,
            interactionSource = interactionSource,
            border = finalColors.borderColor?.let { BorderStroke(2.dp, it) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = finalColors.contentColor,
                disabledContainerColor = Color.Transparent,
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


@Preview(showBackground = true, name = "Custom Buttons - All Styles")
@Composable
private fun CustomButtonAllVariantsPreview() {
    VolunteersCaseTheme {
        Column(
            modifier = Modifier
                .background(Milk)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            // ----------------------------------------------------
            // 1. STYLE TRANSLUCENT
            // ----------------------------------------------------
            Text("Translucent", style = MaterialTheme.typography.titleMedium, color = Abyss)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomButton(text = "Translucent Default", style = ButtonStyle.Translucent) {}
                CustomButton(text = "Translucent Loading", style = ButtonStyle.Translucent, isLoading = true) {}
                CustomButton(text = "Translucent Disabled", style = ButtonStyle.Translucent, enabled = false) {}
            }

            HorizontalDivider(Modifier.fillMaxWidth())

            // ----------------------------------------------------
            // 2. VARIANT - ROUNDED
            // ----------------------------------------------------
            Text("Variant - Rounded (Circle Shape)", style = MaterialTheme.typography.titleMedium, color = Abyss)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Filled", style = MaterialTheme.typography.labelMedium)
                    CustomButton(text = "Active", variant = ButtonVariant.Rounded, style = ButtonStyle.Filled) {}
                    CustomButton(text = "Disabled", variant = ButtonVariant.Rounded, style = ButtonStyle.Filled, enabled = false) {}
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Outlined", style = MaterialTheme.typography.labelMedium)
                    CustomButton(text = "Active", variant = ButtonVariant.Rounded, style = ButtonStyle.Outlined) {}
                    CustomButton(text = "Loading", variant = ButtonVariant.Rounded, style = ButtonStyle.Outlined, isLoading = true) {}
                }
            }

            // ----------------------------------------------------
            // 3. VARIANT - LITE ROUNDED
            // ----------------------------------------------------
            Text("Variant - Lite Rounded (Clip 14.dp)", style = MaterialTheme.typography.titleMedium, color = Abyss)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Filled", style = MaterialTheme.typography.labelMedium)
                    CustomButton(text = "Active", variant = ButtonVariant.LiteRounded, style = ButtonStyle.Filled) {}
                    CustomButton(text = "Disabled", variant = ButtonVariant.LiteRounded, style = ButtonStyle.Filled, enabled = false) {}
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Outlined", style = MaterialTheme.typography.labelMedium)
                    CustomButton(text = "Active", variant = ButtonVariant.LiteRounded, style = ButtonStyle.Outlined) {}
                    CustomButton(text = "Loading", variant = ButtonVariant.LiteRounded, style = ButtonStyle.Outlined, isLoading = true) {}
                }
            }

            // ----------------------------------------------------
            // 4. VARIANT - ROUGH ROUNDED
            // ----------------------------------------------------
            Text("Variant - Rough Rounded (Clip 12.dp)", style = MaterialTheme.typography.titleMedium, color = Abyss)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Filled", style = MaterialTheme.typography.labelMedium)
                    CustomButton(text = "Active", variant = ButtonVariant.RoughRounded, style = ButtonStyle.Filled) {}
                    CustomButton(text = "Disabled", variant = ButtonVariant.RoughRounded, style = ButtonStyle.Filled, enabled = false) {}
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Outlined", style = MaterialTheme.typography.labelMedium)
                    CustomButton(text = "Active", variant = ButtonVariant.RoughRounded, style = ButtonStyle.Outlined) {}
                    CustomButton(text = "Loading", variant = ButtonVariant.RoughRounded, style = ButtonStyle.Outlined, isLoading = true) {}
                }
            }

            // ----------------------------------------------------
            // 5. VARIANT - WIDE
            // ----------------------------------------------------
            Text("Variant - Wide (Clip 10.dp)", style = MaterialTheme.typography.titleMedium, color = Abyss)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Filled", style = MaterialTheme.typography.labelMedium)
                    CustomButton(text = "Active", variant = ButtonVariant.Wide, style = ButtonStyle.Filled) {}
                    CustomButton(text = "Disabled", variant = ButtonVariant.Wide, style = ButtonStyle.Filled, enabled = false) {}
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Outlined", style = MaterialTheme.typography.labelMedium)
                    CustomButton(text = "Active", variant = ButtonVariant.Wide, style = ButtonStyle.Outlined) {}
                    CustomButton(text = "Loading", variant = ButtonVariant.Wide, style = ButtonStyle.Outlined, isLoading = true) {}
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
