package org.adt.presentation.components.buttons

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

object ButtonDefaultsProvider {

    @Composable
    fun config(
        variant: ButtonVariant,
        style: ButtonStyle
    ): ButtonConfig {
        return when (style) {
            ButtonStyle.Translucent -> ButtonConfig(
                shape = CircleShape,
                height = 55.dp,
                textStyle = VolunteersCaseTheme.typography.displayLarge,
                uppercase = true,
                loaderSize = 35.dp
            )

            else -> ButtonConfig(
                shape = shape(variant),
                height = 50.dp,
                textStyle = VolunteersCaseTheme.typography.titleMedium,
                loaderSize = 22.dp
            )
        }
    }

    @Composable
    fun colors(
        variant: ButtonVariant,
        style: ButtonStyle,
        enabled: Boolean
    ): ButtonColorScheme {
        if (!enabled) {
            return when (style) {
                ButtonStyle.Translucent -> ButtonColorScheme(
                    containerColor = Abyss.copy(alpha = 0.5f),
                    contentColor = Arctic.copy(alpha = 0.5f),
                    borderColor = Lagoon.copy(alpha = 0.5f)
                )
                else -> ButtonColorScheme(
                    containerColor = Graphite.copy(alpha = 0.04f),
                    contentColor = Graphite.copy(alpha = 0.3f),
                    borderColor = if (style == ButtonStyle.Outlined) Graphite.copy(alpha = 0.1f) else null
                )
            }
        }

        return when (style) {
            ButtonStyle.Translucent -> ButtonColorScheme(
                containerColor = Lagoon.copy(alpha = 0.5f),
                contentColor = Arctic,
                borderColor = Lagoon
            )

            else -> {
                val base = when (variant) {
                    ButtonVariant.LiteRounded -> Mint to Abyss
                    ButtonVariant.Rounded -> Lagoon to Color.White
                    ButtonVariant.Wide -> Abyss to Color.White
                    ButtonVariant.RoughRounded -> Lagoon to Color.White
                }

                val (container, content) = base

                when (style) {
                    ButtonStyle.Filled -> ButtonColorScheme(
                        containerColor = container,
                        contentColor = content
                    )

                    ButtonStyle.Outlined -> ButtonColorScheme(
                        containerColor = Color.Transparent,
                        contentColor = container,
                        borderColor = container.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }

    fun shape(variant: ButtonVariant): Shape = when (variant) {
        ButtonVariant.LiteRounded -> RoundedCornerShape(14.dp)
        ButtonVariant.RoughRounded -> RoundedCornerShape(12.dp)
        ButtonVariant.Rounded -> CircleShape
        ButtonVariant.Wide -> RoundedCornerShape(10.dp)
    }
}