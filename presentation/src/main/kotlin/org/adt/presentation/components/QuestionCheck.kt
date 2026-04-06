package org.adt.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Silver
import org.adt.presentation.theme.extendedTypography

@Composable
fun CustomQuestionCheckComponent(
    modifier: Modifier = Modifier,
    text: String,
    textLink: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    onLinkClick: () -> Unit
) {
    Row(
        modifier.fillMaxWidth(),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Row(Modifier) {
            Text("$text ", style = extendedTypography.titleSmall.copy(Silver))
            Text(
                textLink, Modifier.clickable { onLinkClick() },
                style = extendedTypography.titleSmall.copy(Lagoon),
            )
        }

        Checkbox(
            checked,
            onChecked,
            Modifier
                .size(25.dp)
                .clip(RoundedCornerShape(3.5.dp)),
            true,
            CheckboxDefaults.colors(
                uncheckedColor = Silver,
                checkedColor = Lagoon,
                checkmarkColor = Arctic
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 353535)
@Composable
private fun CustomQuestionCheckComponentPreview() {
    var checked by remember { mutableStateOf(false) }

    CustomQuestionCheckComponent(
        Modifier,
        "я ознакомился с ",
        "политикой конфиденциальности",
        checked,
        {},
        {}
    )
}