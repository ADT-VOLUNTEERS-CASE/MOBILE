package org.adt.presentation.components

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
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.R
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Silver
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    type: String = "",
    //isError: Boolean = false,
    onValueChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(value) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    TextField(
        textFieldValue,
        {
            textFieldValue = it
            onValueChange(it)
        },
        modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(16.dp)),
        singleLine = true,
        textStyle = VolunteersCaseTheme.typography.titleMedium.copy(color = Void),
        label = {
            Text(
                label,
                style = VolunteersCaseTheme.typography.titleMedium.copy(Void, fontWeight = FontWeight.Normal)
            )
        }, colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Milk,
            focusedContainerColor = Milk,
            unfocusedTextColor = Void,
            focusedTextColor = Void,
            cursorColor = Void,
            unfocusedLabelColor = Void,
            focusedLabelColor = Void.copy(0.05f),
            unfocusedIndicatorColor = Graphite.copy(0.05f),
            focusedIndicatorColor = Graphite,
            selectionColors = TextSelectionColors(Graphite, Graphite.copy(0.2f))
        ), trailingIcon = {
//            if (type == "password") {
//                IconButton({ isPasswordVisible = !isPasswordVisible }, Modifier.size(24.dp)) {
//                    Icon(
//                        painterResource(if (isPasswordVisible) R.drawable.icon_view_hide else R.drawable.icon_view),
//                        "Open/hide password",
//                        Modifier.fillMaxSize(),
//                        Abyss
//                    )
//                }
//            }
        },
        visualTransformation = if (type == "password" && !isPasswordVisible)
            PasswordVisualTransformation('●') // ● ● ● ● ●
        else
            VisualTransformation.None
    )
}

@Composable
fun CustomSearchTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    onValueChange: (String) -> Unit,
    onConfirm: (String) -> Unit
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
            .height(35.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Milk)
            .padding(horizontal = 8.dp, vertical = 4.dp),
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
                    modifier = Modifier.size(18.dp),
                    tint = Graphite
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box {
                    if (textFieldValue.isEmpty()) {
                        Text(
                            label,
                            style = VolunteersCaseTheme.typography.titleMedium.copy(Silver)
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
private fun CustomTextFieldPreview() {
    CustomTextField(Modifier, "CustomTextField") { }
}

@Preview
@Composable
private fun CustomSearchTextFieldPreview() {
    CustomSearchTextField(Modifier, "CustomSearchTextField", "", {}, {})
}