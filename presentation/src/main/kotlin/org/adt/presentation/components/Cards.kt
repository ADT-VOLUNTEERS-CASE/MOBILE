package org.adt.presentation.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.savedstate.serialization.decodeFromSavedState
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Black
import org.adt.presentation.theme.Grey
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Silver
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.theme.mainTypography

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

        SquaredIconButton(R.drawable.ic_trash, 45.dp) { onDeleteClick() }
    }
}

@Composable
fun CardStatistics(onClick: () -> Unit) {
    Box(
        modifier = Modifier
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
fun CardCalendar(onClick: () -> Unit) {
    Box(
        modifier = Modifier
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
        modifier = Modifier
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
        modifier = Modifier
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
        modifier = Modifier
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
fun CardAddEvent(onClick: () -> Unit) {
    Box(
        modifier = Modifier
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
    @Composable
    fun CardEvent(image: Int, title: String, date: String, time: String) {
        Box(
            modifier = Modifier
                .width(145.dp)
                .height(190.dp)
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



                Text(text = title, modifier = Modifier.padding(top = 7.dp))
                Text(text = date)
                Text(text = time)
            }
        }
    }

}

@Composable
fun CardEvent(image: Int, title: String, date: String, time: String) {
    Box(
        modifier = Modifier
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
        modifier = Modifier
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

        CustomRoundedButton("Приступить!") { {} }

    }

}



@Preview
@Composable
private fun NoteCardPreview() {
    NoteCard("Уборка", "14 апреля", "15:30") { }
}

@Preview
@Composable
private fun CardEventPrev() {
    CardEvent(R.drawable.ic_launcher_background, "Название", "12.04.1989", "11:00")

}

@Preview
@Composable
private fun CardAddEventPreview() {
    VolunteersCaseTheme {CardAddEvent { } }
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
    CardEvent(R.drawable.ic_launcher_background, "М1", "Дата", "Время")

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
