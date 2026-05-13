package org.adt.presentation.screens.exception

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.adt.presentation.R
import org.adt.presentation.components.CustomRoundedButton
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.VolunteersCaseTheme


/**
 * The screen indicates that there is no Internet connection
 *
 * @param modifier modifier for managing screen sizes
 *
 * @param onClick function to be invoked on button click
 *
 * @param navController controller for switching between screens.
 *
 * @sample [NoConnectionPreview]
 */
@Composable
fun NoConnectionScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Abyss),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = modifier
                .width(300.dp)
                .height(300.dp)
                .background(Arctic, shape = RoundedCornerShape(20.dp)),
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
                    .padding(top = 30.dp)
                    .size(120.dp)
            )

        }
        CustomRoundedButton(
            modifier = Modifier
                .padding(top = 30.dp)
                .width(300.dp)
                .height(53.dp), text = "Попробовать еще раз", onClick = onClick )

        CustomRoundedButton(
            modifier = Modifier
                .padding(top = 10.dp)
                .width(300.dp)
                .height(53.dp), text = "Назад", onClick = { navController.popBackStack() })
    }

}


@Preview
@Composable
private fun NoConnectionPreview() {
    VolunteersCaseTheme {
        NoConnectionScreen(Modifier, {}, navController = rememberNavController())
    }
}
