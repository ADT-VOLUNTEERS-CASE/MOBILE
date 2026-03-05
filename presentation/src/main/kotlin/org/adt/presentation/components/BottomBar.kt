package org.adt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.core.entities.UserRole
import org.adt.presentation.R
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Lagoon

@Composable
fun CustomBottomBar(role: UserRole) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Lagoon)
            .padding(horizontal = 32.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        when (role) {
            UserRole.ADMIN -> {
                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_home), 
                        null,
                        Modifier.height(24.dp), 
                        Arctic)
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_group),
                        null,
                        Modifier.height(24.dp),
                        Color.Unspecified)
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_calendar),
                        null,
                        Modifier.height(24.dp),
                        Arctic
                    )
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_settings),
                        null,
                        Modifier.height(24.dp),
                        Arctic)
                }
            }
            UserRole.VOLUNTEER -> {
                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_home),
                        null,
                        Modifier.height(24.dp),
                        Arctic)
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_search_bold),
                        null,
                        Modifier.height(24.dp),
                        Color.Unspecified)
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_schedule),
                        null,
                        Modifier.height(24.dp),
                        Arctic
                    )
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_single),
                        null,
                        Modifier.height(24.dp),
                        Arctic)
                }
            }
            UserRole.COORDINATOR -> {
                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_home),
                        null,
                        Modifier.height(24.dp),
                        Arctic)
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_pencil),
                        null,
                        Modifier.height(24.dp),
                        Color.Unspecified)
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_schedule),
                        null,
                        Modifier.height(24.dp),
                        Arctic
                    )
                }

                IconButton({ TODO() }, shape = RectangleShape) {
                    Icon(
                        painterResource(R.drawable.ic_single),
                        null,
                        Modifier.height(24.dp),
                        Arctic)
                }
            }
        }
    }
}

@Preview
@Composable
private fun CustomBottomBarPreview() {
    CustomBottomBar(UserRole.ADMIN)
}