package org.adt.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Black
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Grey
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.theme.mainTypography

@Composable
fun CustomLiteRoundedButton(value: String, onClick: () -> Unit) {
    Button(
        onClick,
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(48.dp)),
        true,
        RoundedCornerShape(48.dp),
        ButtonDefaults.buttonColors(Lagoon)
    ) {
        Text(value, style = mainTypography.titleMedium.copy(Arctic))
    }
}

@Composable
fun CustomRoundedButton(value: String, onClick: () -> Unit) {
    Button(
        onClick,
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp)),
        true,
        RoundedCornerShape(17.dp),
        ButtonDefaults.buttonColors(Mint)
    ) {
        Text(value, style = mainTypography.titleMedium.copy(Graphite))
    }
}

@Composable
fun CustomWideButton(value: String, onClick: () -> Unit) {
    Button(
        onClick,
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp)),
        true,
        RoundedCornerShape(7.dp),
        ButtonDefaults.buttonColors(Abyss)
    ) {
        Text(
            value,
            Modifier.padding(vertical = 17.dp),
            style = mainTypography.titleMedium.copy(Arctic)
        )
    }
}

@Composable
fun SquaredIconButton(resId: Int, sizeDp: Dp, onClick: () -> Unit) {
    IconButton(
        onClick,
        Modifier
            .size(sizeDp)
            .clip(RoundedCornerShape(10.dp))
            .background(Mint)
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(
            painterResource(resId),
            null,
            Modifier.fillMaxSize(),
            Abyss
        )
    }
}




@Preview
@Composable
private fun CustomLiteRoundedButtonPreview() {
    CustomLiteRoundedButton("Смотреть все") { }
}

@Preview
@Composable
private fun CustomRoundedButtonPreview() {
    CustomRoundedButton("Создать заметку") { }
}

@Preview
@Composable
private fun CustomWideButtonPreview() {
    CustomWideButton("Добавить") { }
}

@Preview
@Composable
private fun SquaredIconButtonPreview() {
    SquaredIconButton(R.drawable.ic_trash, 45.dp) { }
}
