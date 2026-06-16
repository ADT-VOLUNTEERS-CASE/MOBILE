package org.adt.presentation.components.misc

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import org.adt.presentation.theme.VolunteersCaseTheme
import kotlin.text.iterator
import kotlin.time.Duration.Companion.milliseconds

/**
 * Visual text component animating text rendering with a typewriter typing effect
 *
 * Sequentially appends characters over a configurable time frame with support for
 * initial delays, custom alignments, and global animation bypass triggers.
 *
 * @param modifier modifier used for custom positioning, sizing, or outer padding
 * @param text raw text payload to be animated and converted to uppercase styling
 * @param align alignment style determining text layout bounds inside the container
 * @param charDelay specific duration in milliseconds to pause between individual character steps
 * @param delay initial warmup duration in milliseconds to wait before starting the typing animation
 * @param animationOverride flag bypassing the typing effect to immediately draw the full string
 */
@Composable
fun TypingText(
    modifier: Modifier = Modifier,
    text: String,
    align: TextAlign = TextAlign.Start,
    charDelay: Long = 40L,
    delay: Long = 0,
    animationOverride: Boolean = false,
) {
    var displayedText by remember { mutableStateOf(if (animationOverride) text else "") }

    LaunchedEffect(text, charDelay) {
        delay(delay.milliseconds)
        displayedText = ""
        for (char in text) {
            displayedText += char
            delay(charDelay.milliseconds)
        }
    }

    Text(
        text = displayedText.uppercase(),
        style = VolunteersCaseTheme.typography.displayLarge,
        textAlign = align,
        modifier = modifier
    )
}