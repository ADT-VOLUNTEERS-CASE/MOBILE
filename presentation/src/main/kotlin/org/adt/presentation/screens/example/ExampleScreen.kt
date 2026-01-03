package org.adt.presentation.screens.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun ExampleScreen(innerPadding: PaddingValues = PaddingValues()) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Gray), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(140.dp))
        Text(
            "Hello world!", fontSize = 24.sp, textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExampleScreenPreview() {
    VolunteersCaseTheme {
        ExampleScreen()
    }
}