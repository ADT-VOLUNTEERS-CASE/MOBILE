package org.adt.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.extendedTypography

@Composable
fun CustomTextField(
    label: String,
    value: String = "",
    type: String = "",
    isError: Boolean = false,
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
        Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(16.dp)),
        singleLine = true,
        label = {
            Text(
                label,
                style = extendedTypography.titleMedium.copy(Void, fontWeight = FontWeight.Normal)
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
            focusedIndicatorColor = Graphite
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