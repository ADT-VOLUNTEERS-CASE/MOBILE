package org.adt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.presentation.R
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Silver
import org.adt.presentation.theme.extendedTypography

@Composable
fun NoteCard(title: String, date: String, time: String, onDeleteClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Arctic)
            .border(1.dp, Silver, RoundedCornerShape(20.dp))
            .padding(16.dp),
        Arrangement.SpaceBetween, Alignment.CenterVertically
    ) {
        Column(Modifier, Arrangement.spacedBy(8.dp)) {
            Text(title, style = extendedTypography.titleLarge.copy(fontWeight = FontWeight.Normal))
            Text(date, style = extendedTypography.titleMedium.copy(color = Silver, fontWeight = FontWeight.Normal))
        }

        Text(time, style = extendedTypography.titleLarge.copy(fontSize = 40.sp))

        SquaredIconButton(R.drawable.ic_trash, 45.dp) { onDeleteClick() }
    }
}

@Preview
@Composable
private fun NoteCardPreview() {
    NoteCard("Уборка", "14 апреля", "15:30") { }
}