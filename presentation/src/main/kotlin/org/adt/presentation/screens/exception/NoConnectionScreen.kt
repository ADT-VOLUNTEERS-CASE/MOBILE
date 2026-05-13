package org.adt.presentation.screens.exception

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.adt.presentation.R
import org.adt.presentation.components.CustomRoundedButton
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.VolunteersCaseTheme


@Composable
fun NoConnectionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NoConnectionScreenViewModel = hiltViewModel<NoConnectionScreenViewModel>(),
) {
    BackHandler(enabled = true) {
        // Restricts manual back navigation
    }

    NoConnectionScreenContent(
        modifier = modifier,
        onClick = { viewModel.refresh { navController.popBackStack() } })
}

/**
 * The screen indicates that there is no Internet connection
 *
 * @param modifier modifier for managing screen sizes
 *
 * @param onClick function to be invoked on button click
 *
 * @sample [NoConnectionScreenContentPreview]
 */
@Composable
fun NoConnectionScreenContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Abyss)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .sizeIn(maxWidth = 400.dp, maxHeight = 400.dp)
                .fillMaxWidth()
                .background(Arctic, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Ошибка подключения",
                modifier = Modifier.padding(top = 30.dp),
                style = VolunteersCaseTheme.typography.displayMedium.copy(fontSize = 28.sp),
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(R.drawable.ic_worried_face),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .sizeIn(
                        maxWidth = 120.dp,
                        maxHeight = 120.dp
                    )
                    .fillMaxSize()
            )

        }

        Spacer(modifier = Modifier.height(64.dp))

        Column(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomRoundedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 53.dp),
                text = "Обновить!",
                onClick = onClick
            )
        }
    }

}


@Preview
@Composable
private fun NoConnectionScreenContentPreview() {
    VolunteersCaseTheme {
        NoConnectionScreenContent(Modifier) {}
    }
}
