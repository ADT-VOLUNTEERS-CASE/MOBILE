package org.adt.presentation.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.R
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun CustomSearchTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    verticalScale: Float = 1f,
    onValueChange: (String) -> Unit,
    onConfirm: (String) -> Unit,
    onFocused: (FocusState) -> Unit = {}
) {
    var textFieldValue by remember { mutableStateOf(value) }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        textFieldValue,
        {
            textFieldValue = it
            onValueChange(it)
        },
        modifier
            .fillMaxWidth()
            .height((48 * verticalScale).dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            .background(Milk)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .onFocusEvent(onFocused),
        singleLine = true,
        textStyle = VolunteersCaseTheme.typography.titleMedium.copy(color = Void),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onConfirm(textFieldValue)
            }
        ),
        decorationBox = { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(R.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(18.dp),
                    tint = Graphite
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box {
                    if (textFieldValue.isEmpty()) {
                        Text(
                            label,
                            style = VolunteersCaseTheme.typography.titleMedium.copy(
                                Graphite.copy(
                                    0.5f
                                )
                            )
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Preview
@Composable
private fun CustomSearchTextFieldPreview() {
    Box(
        Modifier
            .height(700.dp)
            .width(500.dp)
            .background(Arctic)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        CustomSearchTextField(Modifier, "CustomSearchTextField", "", 1f, {}, {})
    }
}