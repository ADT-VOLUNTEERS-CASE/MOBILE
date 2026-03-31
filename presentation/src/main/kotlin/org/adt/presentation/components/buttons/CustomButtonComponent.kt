package org.adt.presentation.components.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Rounded,
    style: ButtonStyle = ButtonStyle.Filled,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    colors: ButtonColorScheme? = null,  // override
    onClick: () -> Unit,
) {
    val config = ButtonDefaultsProvider.config(variant, style)
    val defaultColors = ButtonDefaultsProvider.colors(variant, style, enabled)
    val finalColors = colors ?: defaultColors
    val finalText = if (config.uppercase) text.uppercase() else text

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .then(if (config.height != null) Modifier.height(config.height) else Modifier)
            .clip(config.shape)
            .then(
                if (finalColors.borderColor != null) {
                    Modifier.border(2.dp, finalColors.borderColor, config.shape)
                } else Modifier
            ),
        enabled = enabled,
        shape = config.shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = finalColors.containerColor,
            contentColor = finalColors.contentColor,
            disabledContainerColor = finalColors.containerColor.copy(0.5f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(config.loaderSize),
                color = finalColors.contentColor
            )
        } else {
            Text(
                text = finalText,
                style = config.textStyle.copy(finalColors.contentColor)
            )
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