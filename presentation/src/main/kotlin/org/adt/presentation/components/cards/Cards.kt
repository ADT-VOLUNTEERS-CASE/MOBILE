package org.adt.presentation.components.cards


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.data.model.AllDescriptionEvent
import org.adt.presentation.R
import org.adt.presentation.components.CustomLiteRoundedButton
import org.adt.presentation.components.CustomRoundedButton
import org.adt.presentation.components.SquaredIconButton
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Black
import org.adt.presentation.theme.Grey
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Silver
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.theme.mainTypography

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    time: String,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Arctic)
            .border(1.dp, Silver, RoundedCornerShape(20.dp))
            .padding(16.dp),
        Arrangement.SpaceBetween, Alignment.CenterVertically
    ) {
        Column(Modifier, Arrangement.spacedBy(8.dp)) {
            Text(title, style = mainTypography.titleLarge.copy(fontWeight = FontWeight.Normal))
            Text(
                date,
                style = mainTypography.titleMedium.copy(
                    color = Silver,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        Text(time, style = mainTypography.titleLarge.copy(fontSize = 40.sp))

        SquaredIconButton(Modifier, R.drawable.ic_trash, 45.dp) { onDeleteClick() }
    }
}

@Composable
fun CardStatistics(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .width(155.dp)
            .height(120.dp)
            .background(color = Arctic, shape = RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "статистика",
                color = Black,
                modifier = Modifier.padding(bottom = 4.dp, top = 4.dp),
                style = mainTypography.titleMedium.copy(Black),
            )
            Icon(
                painter = painterResource(R.drawable.ic_chart),
                contentDescription = null,
                tint = Abyss,
                modifier = Modifier
                    .padding(top = 13.dp)
                    .width(70.dp)
                    .height(57.dp)
            )

        }
    }

}


@Composable
fun CardCalendar(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .width(155.dp)
            .height(120.dp)
            .background(color = Arctic, shape = RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "календарь",
                modifier = Modifier.padding(bottom = 14.dp, top = 4.dp),
                style = mainTypography.titleMedium.copy(Black)
            )
            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = null,
                tint = Abyss,
                modifier = Modifier
                    .width(50.dp)
                    .height(57.dp)
            )

        }
    }

}


@Composable
fun CardStatisticsCountGoodWork(modifier: Modifier = Modifier, count: String) {

    Column(
        modifier = modifier
            .width(145.dp)
            .height(124.dp)
            .background(color = Lagoon, shape = RoundedCornerShape(14.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Количество благополучателей",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
            color = Arctic
        )

        Text(
            text = count,
            style = mainTypography.titleMedium.copy(color = Arctic, fontSize = 36.sp),
            modifier = Modifier.padding(top = 10.dp)
        )
    }


}


@Composable
fun CardAllCountGoodWork(modifier: Modifier = Modifier, countGoodWork: String) {
    Column(
        modifier = modifier
            .width(300.dp)
            .height(123.dp)
            .background(color = Lagoon, shape = RoundedCornerShape(14.dp))
    ) {
        Row(

            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = countGoodWork,
                style = mainTypography.titleMedium.copy(Arctic, fontSize = 48.sp),
                modifier = Modifier.padding(start = 32.dp, end = 10.dp)
            )
            Text(
                text = "добрых дел",
                style = mainTypography.titleMedium.copy(fontSize = 32.sp, color = Arctic)
            )
        }
        Text(
            text = "вы обладатель большого сердца!",
            style = mainTypography.titleMedium.copy(fontSize = 16.sp, color = Grey),
            modifier = Modifier.padding(start = 32.dp)
        )
    }

}

@Composable
fun CardHoursGoodWork(modifier: Modifier = Modifier, hoursGoodWork: String) {
    Column(
        modifier = modifier
            .width(300.dp)
            .height(133.dp)
            .background(
                color = Abyss, shape = RoundedCornerShape(14.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = hoursGoodWork,
                style = mainTypography.titleMedium.copy(Arctic, fontSize = 48.sp),
                modifier = Modifier.padding(start = 17.dp, end = 11.dp)
            )
            Text(
                text = "добрых часов",
                style = mainTypography.titleMedium.copy(fontSize = 32.sp, color = Arctic)
            )
        }
        Text(
            text = "Каждая минута, что вы отдали, стала чьим-то лучиком солнца",
            style = mainTypography.titleMedium.copy(fontSize = 16.sp, color = Grey),
            modifier = Modifier.padding(start = 25.dp, end = 25.dp),
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun CardAchievementOfTheWeek(modifier: Modifier = Modifier, achievement: String) {
    Column(
        modifier = modifier
            .width(145.dp)
            .height(124.dp)
            .background(color = Lagoon, shape = RoundedCornerShape(14.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "достижение недели",
            style = mainTypography.titleMedium.copy(fontSize = 15.sp, color = Arctic),
            modifier = Modifier.padding(top = 10.dp, start = 11.dp, end = 11.dp),
            textAlign = TextAlign.Center
        )
        Icon(
            painter = painterResource(R.drawable.ic_leaf),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 12.dp, bottom = 4.dp)
                .width(48.dp)
                .height(38.dp),
            tint = Abyss
        )
        Text(
            text = achievement,
            style = mainTypography.titleMedium.copy(fontSize = 13.sp, color = Arctic)
        )
    }
}

@Composable
fun CardAddEvent(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .width(155.dp)
            .height(120.dp)
            .background(color = Arctic, shape = RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "создать мероприятие",
                style = mainTypography.titleMedium.copy(color = Black),
                color = Black,
                modifier = Modifier.padding(bottom = 3.dp, top = 4.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .padding(top = 9.dp)
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(color = Abyss), contentAlignment = Alignment.Center

            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = null,
                    tint = Arctic,
                    modifier = Modifier
                        .width(21.dp)
                        .height(21.dp)
                )
            }


        }
    }
}

@Composable
fun CardEvent(
    modifier: Modifier = Modifier,
    image: Int,
    title: String,
    date: String,
    time: String
) {
    Box(
        modifier = modifier
            .height(190.dp)
            .width(145.dp)
            .background(color = Abyss, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .width(134.dp)
                    .height(125.dp)
                    .padding(top = 6.dp)
                    .clip(shape = RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop
            )



            Text(
                text = title,
                modifier = Modifier.padding(top = 9.dp),
                style = mainTypography.titleLarge.copy(
                    fontSize = 11.sp,
                    color = Arctic,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = date,
                style = mainTypography.titleLarge.copy(
                    fontSize = 8.sp, color = Arctic,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
            )
            Text(
                text = time,
                style = mainTypography.titleLarge.copy(
                    fontSize = 8.sp, color = Arctic,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun CardDescriptionEvent(
    modifier: Modifier = Modifier,
    title: String,
    image: Int,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(start = 39.dp, end = 40.dp, top = 219.dp, bottom = 149.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(300.dp)
                .height(281.dp)
                .background(color = Abyss), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .width(279.dp)
                    .height(259.dp),
                contentScale = ContentScale.Fit
            )
        }
        Text(text = title)
        Text(text = description)
        CustomRoundedButton(modifier = Modifier, "Приступить!") { {} }
    }
}

@Composable
fun CardAllDescriptionEvent(
    modifier: Modifier = Modifier,
    allDescriptionEvent: AllDescriptionEvent,
    onClick: () -> Unit
) {
    val state = rememberScrollState()
    Column(
        modifier = modifier
            .width(321.dp)
            .height(492.dp)
            .background(color = Arctic, RoundedCornerShape(17.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(300.dp)
                .height(281.dp)
                .background(color = Abyss, shape = RoundedCornerShape(17.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = if (allDescriptionEvent.image == 0) R.drawable.baseimage else allDescriptionEvent.image),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(17.dp))
                    .width(279.dp)
                    .height(259.dp),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = (if (allDescriptionEvent.title == "") stringResource(R.string.base_title) else allDescriptionEvent.title),
            style = mainTypography.titleLarge.copy(fontSize = 19.sp, color = Black),
            modifier = Modifier
                .padding(top = 15.dp, start = 10.dp)
                .fillMaxWidth()
        )
        Text(
            text = (if (allDescriptionEvent.description == "") stringResource(R.string.base_description) else allDescriptionEvent.description),
            style = mainTypography.titleMedium.copy(fontSize = 15.sp, color = Grey),
            modifier = Modifier
                .padding(top = 9.dp)
                .width(300.dp)
                .height(61.dp)
                .verticalScroll(state)
        )
        CustomLiteRoundedButton(
            modifier = Modifier.padding(
                start = 11.dp,
                end = 10.dp,
                top = 28.dp
            ), "Приступить!"
        ) { }
    }

}

@Composable
fun CardEventMonitoring(modifier: Modifier = Modifier, color: Color, count: String, text: String) {
    Row(
        modifier = modifier
            .width(300.dp)
            .height(85.dp)
            .background(color = color, shape = RoundedCornerShape(14.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = count,
            style = mainTypography.titleMedium.copy(fontSize = 48.sp, color = Arctic),
            modifier = Modifier.padding(start = 35.dp, top = 14.dp)
        )
        Text(
            text = text,
            style = mainTypography.titleMedium.copy(fontSize = 22.sp, color = Arctic),
            modifier = Modifier.padding(end = 15.dp, top = 18.dp, start = 22.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CardCountUser(
    modifier: Modifier = Modifier,
    countAll: String,
    countNew: String,
    countActive: String,
    size: Dp
) {
    Column(
        modifier = modifier
            .width(size)
            .height(100.dp)
            .background(color = Abyss, shape = RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 13.dp)
        ) {
            Text(
                text = "Всего:",
                style = mainTypography.titleMedium.copy(fontSize = 15.sp, color = Arctic),
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
            Text(
                text = countAll,
                style = mainTypography.titleMedium.copy(fontSize = 15.sp, color = Arctic)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "Новые:",
                style = mainTypography.titleMedium.copy(fontSize = 15.sp, color = Arctic),
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
            Text(
                text = countNew,
                style = mainTypography.titleMedium.copy(fontSize = 15.sp, color = Arctic)
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "Активные:",
                style = mainTypography.titleMedium.copy(fontSize = 15.sp, color = Arctic),
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
            Text(
                text = countActive,
                style = mainTypography.titleMedium.copy(fontSize = 15.sp, color = Arctic)
            )
        }

    }

}


@Preview
@Composable
private fun CardCountUserPreview() {
    CardCountUser(modifier = Modifier, "150", "14", "24", 172.dp)
}

@Preview
@Composable
private fun NoteCardPreview() {
    NoteCard(Modifier, "Уборка", "14 апреля", "15:30") { }
}

@Preview
@Composable
private fun CardEventPrev() {
    CardEvent(Modifier, R.drawable.ic_launcher_background, "Название", "12.04.1989", "11:00")

}

@Preview
@Composable
private fun CardAddEventPreview() {
    VolunteersCaseTheme { CardAddEvent { } }
}

@Preview
@Composable
private fun CardCalendarPreview() {
    VolunteersCaseTheme { CardCalendar { } }

}

@Preview
@Composable
private fun CardStatisticsPreview() {
    VolunteersCaseTheme { CardStatistics { } }
}

@Preview
@Composable
private fun CardEventPreview() {
    CardEvent(Modifier, image = R.drawable.ic_launcher_background, "М1", "Дата", "Время")

}

@Preview
@Composable
private fun CardCardStatisticsCountGoodWorkPreview() {
    CardStatisticsCountGoodWork(modifier = Modifier, "147")
}


@Preview
@Composable
private fun CardAllCountGoodWorkPreview() {
    CardAllCountGoodWork(modifier = Modifier, "50")
}

@Preview
@Composable
private fun CardHoursGoodWorkPreview() {
    CardHoursGoodWork(modifier = Modifier, "70")
}

@Preview
@Composable
private fun CardAchievementOfTheWeekPreview() {
    CardAchievementOfTheWeek(modifier = Modifier, "эко-герой")
}

@Preview
@Composable
private fun CardAllDescriptionEventPrev() {
    VolunteersCaseTheme {
        CardAllDescriptionEvent(
            modifier = Modifier,
            AllDescriptionEvent(
                R.drawable.ic_launcher_background,
                "Соседский книжный шкаф",
                "Создай в своём дворе библиотеку для всех желающих: поставь полку, делись книгами и поддерживай в ней порядок.",
                time = "13:40",
                date = "01.01"
            ),
            {}
        )
    }

}

@Preview
@Composable
private fun CardEventMonitoringPreview() {
    CardEventMonitoring(modifier = Modifier, Abyss, "10", "подозрительных входа")
}