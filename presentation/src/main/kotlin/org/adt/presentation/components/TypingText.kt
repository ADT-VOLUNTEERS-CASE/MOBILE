package org.adt.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import org.adt.presentation.theme.extendedTypography

@Composable
fun TypingText(
    text: String,
    align: TextAlign,
    charDelay: Long = 40L,
    delay: Long = 0
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text, charDelay) {
        delay(delay)
        displayedText = ""
        for (char in text) {
            displayedText += char
            delay(charDelay)
        }
    }

    Text(
        text = displayedText.uppercase(),
        style = extendedTypography.displayLarge,
        textAlign = align,
    )
}