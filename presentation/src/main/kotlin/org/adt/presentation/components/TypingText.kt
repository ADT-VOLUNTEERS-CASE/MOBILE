package org.adt.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.adt.presentation.theme.mainTypography

@Composable
fun TypingText(
    text: String,
    charDelay: Long = 40L,
) {
    var displayedText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(text) {
        displayedText = ""
        scope.launch {
            for (char in text) {
                displayedText += char
                delay(charDelay)
            }
        }
    }

    Text(
        text = displayedText.uppercase(),
        style = mainTypography.displayLarge
    )
}