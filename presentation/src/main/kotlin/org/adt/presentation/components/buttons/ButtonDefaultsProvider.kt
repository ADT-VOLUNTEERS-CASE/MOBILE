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
import org.adt.presentation.theme.VolunteersCaseTheme.typography

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
                textStyle = VolunteersCaseTheme.typography.titleMedium
            )
        }
    }

    @Composable
    fun colors(
        variant: ButtonVariant,
        style: ButtonStyle,
        enabled: Boolean
    ): ButtonColorScheme {
        return when (style) {
            ButtonStyle.Translucent -> ButtonColorScheme(
                containerColor = Lagoon.copy(alpha = 0.5f),
                contentColor = if (enabled) Arctic else Arctic.copy(0.5f),
                borderColor = if (enabled) Lagoon else Lagoon.copy(0.5f)
            )

            else -> {
                val base = when (variant) {
                    ButtonVariant.LiteRounded -> Mint to Graphite
                    ButtonVariant.Rounded -> Lagoon to Arctic
                    ButtonVariant.Wide -> Abyss to Arctic
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
                        borderColor = container
                    )

                    else -> error("Unhandled style")
                }
            }
        }
    }

    fun shape(variant: ButtonVariant): Shape = when (variant) {
        ButtonVariant.LiteRounded -> RoundedCornerShape(17.dp)
        ButtonVariant.Rounded -> RoundedCornerShape(48.dp)
        ButtonVariant.Wide -> RoundedCornerShape(7.dp)
    }
}