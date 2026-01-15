package org.adt.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import dagger.hilt.android.AndroidEntryPoint
import org.adt.presentation.screens.example.ExampleScreen
import org.adt.presentation.theme.VolunteersCaseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Example model
            val model = hiltViewModel<ExampleViewModel>()

            VolunteersCaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExampleScreen(innerPadding)
                }
            }
        }
    }
}