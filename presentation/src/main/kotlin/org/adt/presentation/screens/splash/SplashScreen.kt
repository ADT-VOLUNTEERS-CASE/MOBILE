package org.adt.presentation.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.adt.presentation.R
import org.adt.presentation.components.TypingText
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.theme.extendedColor

@Composable
fun SplashScreen(
    navController: NavHostController, viewModel: SplashViewModel = hiltViewModel<SplashViewModel>()
) {
    val scope = rememberCoroutineScope()

    SplashContent(pingAction = { viewModel.ping() }, navigateAction = {
        scope.launch {
            navController.navigate(
                viewModel.getDestination()
            ) {
                popUpTo(Destinations.Splash) { inclusive = true }
                launchSingleTop = true
            }
        }
    })
}

@Composable
fun SplashContent(
    pingAction: () -> Unit = {},
    navigateAction: () -> Unit = {},
    animationOverride: Boolean = false,
) {
    val offsetIconX = remember { Animatable(0f) }
    val offsetIconY = remember { Animatable(if (!animationOverride) -1000f else -100f) }
    val offsetTextX = remember { Animatable(0f) }
    val offsetTextY = remember { Animatable(if (!animationOverride) 1000f else 300f) }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch {
                offsetIconY.animateTo(-100f, tween(1000))
            }
            launch {
                offsetTextY.animateTo(300f, tween(1000))
            }
        }

        delay(1800)

        coroutineScope {
            launch {
                offsetTextX.animateTo(2000f, tween(700, easing = FastOutSlowInEasing))
            }
            launch {
                offsetIconX.animateTo(-2000f, tween(700))
            }
        }

        delay(200)

        pingAction.invoke()

        navigateAction.invoke()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(extendedColor.primaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(200.dp)
                .offset { IntOffset(offsetIconX.value.toInt(), offsetIconY.value.toInt()) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(R.drawable.ic_main), null, tint = Arctic
            )
        }

        Box(
            Modifier
                .offset { IntOffset(offsetTextX.value.toInt(), offsetTextY.value.toInt()) }
                .padding(horizontal = 40.dp), contentAlignment = Alignment.Center) {
            TypingText(
                modifier = Modifier,
                text = "Твоё следующее доброе дело ждёт своего момента",
                align = TextAlign.Center,
                charDelay = if (!animationOverride) 40L else 0L,
                animationOverride = animationOverride
            )
        }
    }
}

@Preview
@Composable
private fun SplashContentPreview() {
    VolunteersCaseTheme {
        SplashContent(animationOverride = true)
    }
}