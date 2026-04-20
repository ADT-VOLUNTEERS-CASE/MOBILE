package org.adt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun CustomLiteRoundedButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(48.dp)),
        enabled = true,
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(Lagoon)
    ) {
        Text(text, style = VolunteersCaseTheme.typography.titleMedium.copy(Arctic))
    }
}

@Composable
fun CustomRoundedButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp)),
        enabled = true,
        shape = RoundedCornerShape(17.dp),
        colors = ButtonDefaults.buttonColors(Mint)
    ) {
        Text(
            text = text,
            style = VolunteersCaseTheme.typography.titleMedium.copy(Graphite)
        )
    }
}

@Composable
fun CustomWideButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp)),
        enabled = true,
        shape = RoundedCornerShape(7.dp),
        colors = ButtonDefaults.buttonColors(Abyss)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 17.dp),
            style = VolunteersCaseTheme.typography.titleMedium.copy(Arctic)
        )
    }
}

@Composable
fun SquaredIconButton(
    modifier: Modifier = Modifier,
    resId: Int = R.drawable.ic_main_heart_light,
    sizeDp: Dp = 45.dp,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(sizeDp)
            .clip(RoundedCornerShape(10.dp))
            .background(Mint)
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(
            painter = painterResource(resId),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            tint = Abyss
        )
    }
}

@Preview
@Composable
private fun CustomLiteRoundedButtonPreview() {
    CustomLiteRoundedButton(modifier = Modifier, text = "Смотреть все") { }
}

@Preview
@Composable
private fun CustomRoundedButtonPreview() {
    CustomRoundedButton(modifier = Modifier, text = "Создать заметку") { }
}

@Preview
@Composable
private fun CustomWideButtonPreview() {
    CustomWideButton(modifier = Modifier, text = "Добавить") { }
}

@Preview
@Composable
private fun SquaredIconButtonPreview() {
    SquaredIconButton(
        modifier = Modifier,
        resId = R.drawable.ic_trash,
        sizeDp = 45.dp,
        contentDescription = "Delete"
    ) { }
}