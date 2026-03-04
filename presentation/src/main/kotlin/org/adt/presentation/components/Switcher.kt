package org.adt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Silver

@Composable
fun CustomSwitcher(switched: Boolean, onSwitchedChange: (Boolean) -> Unit) {
    Box(
        Modifier
            .size(width = 80.dp, height = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Abyss),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(start = 35.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (!switched) Silver else Color.Transparent)
                .clickable { onSwitchedChange(false) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(R.drawable.ic_wrong),
                null,
                Modifier.size(8.dp),
                Arctic
            )
        }

        Box(
            Modifier
                .fillMaxSize()
                .padding(end = 35.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (switched) Lagoon else Color.Transparent)
                .clickable { onSwitchedChange(true) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(R.drawable.ic_correct),
                null,
                Modifier.size(width = 10.dp, height = 8.dp),
                Arctic
            )
        }
    }
}

@Preview
@Composable
private fun CustomSwitcherPreview() {
    var switched by remember { mutableStateOf(true) }
    CustomSwitcher(switched) { switched = it }
}