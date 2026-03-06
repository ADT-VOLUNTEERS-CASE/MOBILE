package org.adt.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import org.adt.presentation.theme.extendedTypography

@Composable
fun TypingText(
    text: String,
    charDelay: Long = 40L,
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text, charDelay) {
        displayedText = ""
        for (char in text) {
            displayedText += char
            delay(charDelay)
        }
    }

    Text(
        text = displayedText.uppercase(),
        style = extendedTypography.displayLarge
    )
}