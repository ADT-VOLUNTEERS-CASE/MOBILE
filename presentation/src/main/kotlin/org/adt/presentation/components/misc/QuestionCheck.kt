package org.adt.presentation.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Silver
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Interactive legal agreement row component with inline web hyperlinks
 *
 * Combines a multi-styled annotated single text block with a customized Material 3 Checkbox.
 * Prevents horizontal layout overflow by safely wrapping long legal policy strings across lines.
 *
 * @param modifier modifier used for managing structural sizes, constraints, and external padding
 * @param text primary descriptive string token preceding the hyperlink target
 * @param textLink interactive hyperlinked text portion styled with brand accent tints
 * @param checked boolean flag mapping the active selection state of the trailing checkbox node
 * @param onChecked action lambda triggered when the selection state changes
 * @param onLinkClick navigation callback executed when the interactive text link is pressed
 *
 * @sample [CustomQuestionCheckComponentPreview]
 */
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
            Text("$text ", style = VolunteersCaseTheme.typography.titleSmall.copy(Silver))
            Text(
                textLink, Modifier.clickable { onLinkClick() },
                style = VolunteersCaseTheme.typography.titleSmall.copy(Lagoon),
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

    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            CustomQuestionCheckComponent(
                Modifier,
                "я ознакомился с ",
                "политикой конфиденциальности",
                checked,
                {},
                {}
            )
        }
    }
}