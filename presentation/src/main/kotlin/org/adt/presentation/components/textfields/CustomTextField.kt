package org.adt.presentation.components.textfields

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Universal text input field built on top of BasicTextField with password masking and focus indicator
 *
 * Provides built-in support for password toggles, custom cursor behaviors,
 * selection colors, and an animated border that reacts to focus events.
 *
 * @param modifier modifier used for custom positioning, sizing, or outer padding
 * @param label placeholder text displayed inside the field when the input value is empty
 * @param value initial state text value of the input field
 * @param type input layout behavior flag, uses "password" to activate masking and toggle icons
 * @param onValueChange function invoked on every character change or text modification
 *
 * @sample [CustomTextFieldPreview]
 */
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    type: String = "",
    onValueChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(value) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val customTextSelectionColors = remember {
        TextSelectionColors(
            handleColor = Graphite,
            backgroundColor = Graphite.copy(alpha = 0.2f)
        )
    }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) Graphite.copy(alpha = 0.4f) else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "BorderColorAnimation"
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onValueChange(it)
            },
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp))
                .background(Milk, RoundedCornerShape(14.dp))
                .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                .onFocusEvent { isFocused = it.isFocused },
            singleLine = true,
            cursorBrush = SolidColor(Void),
            textStyle = VolunteersCaseTheme.typography.titleMedium.copy(
                color = Void,
                fontSize = 15.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (type == "password") KeyboardType.Password else KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (type == "password" && !isPasswordVisible) {
                PasswordVisualTransformation('●')
            } else {
                VisualTransformation.None
            },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textFieldValue.isEmpty()) {
                            Text(
                                text = label,
                                style = VolunteersCaseTheme.typography.titleMedium.copy(
                                    color = Graphite.copy(alpha = 0.4f),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            )
                        }
                        innerTextField()
                    }

                    if (type == "password") {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = "Показать/скрыть пароль",
                            tint = Graphite.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    isPasswordVisible = !isPasswordVisible
                                }
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun CustomTextFieldPreview() {
    Box(
        Modifier
            .height(700.dp)
            .width(500.dp)
            .background(Arctic)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        CustomTextField(Modifier, "CustomTextField") { }
    }
}

