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
import org.adt.presentation.theme.extendedTypography

@Composable
fun CustomLiteRoundedButton(modifier: Modifier= Modifier, value: String, onClick: () -> Unit) {
    Button(
        onClick,
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(48.dp)),
        true,
        RoundedCornerShape(48.dp),
        ButtonDefaults.buttonColors(Lagoon)
    ) {
        Text(value, style = extendedTypography.titleMedium.copy(Arctic))
    }
}

@Composable
fun CustomRoundedButton(modifier: Modifier= Modifier, value: String, onClick: () -> Unit) {
    Button(
        onClick,
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp)),
        true,
        RoundedCornerShape(17.dp),
        ButtonDefaults.buttonColors(Mint)
    ) {
        Text(value, style = extendedTypography.titleMedium.copy(Graphite))
    }
}

@Composable
fun CustomWideButton(modifier: Modifier= Modifier, value: String, onClick: () -> Unit) {
    Button(
        onClick,
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp)),
        true,
        RoundedCornerShape(7.dp),
        ButtonDefaults.buttonColors(Abyss)
    ) {
        Text(
            value,
            Modifier.padding(vertical = 17.dp),
            style = extendedTypography.titleMedium.copy(Arctic)
        )
    }
}

@Composable
fun SquaredIconButton(modifier: Modifier= Modifier, resId: Int, sizeDp: Dp, contentDescription: String, onClick: () -> Unit) {
    IconButton(
        onClick,
        modifier
            .size(sizeDp)
            .clip(RoundedCornerShape(10.dp))
            .background(Mint)
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(
            painterResource(resId),
            contentDescription,
            Modifier.fillMaxSize(),
            Abyss
        )
    }
}

@Preview
@Composable
private fun CustomLiteRoundedButtonPreview() {
    CustomLiteRoundedButton(modifier = Modifier,"Смотреть все") { }
}

@Preview
@Composable
private fun CustomRoundedButtonPreview() {
    CustomRoundedButton(modifier = Modifier,"Создать заметку") { }
}

@Preview
@Composable
private fun CustomWideButtonPreview() {
    CustomWideButton(modifier = Modifier,"Добавить") { }
}

@Preview
@Composable
private fun SquaredIconButtonPreview() {
    SquaredIconButton(modifier = Modifier,R.drawable.ic_trash, 45.dp, "Delete") { }
}