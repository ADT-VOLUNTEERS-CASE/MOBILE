package org.adt.presentation.components.cards


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.adt.core.entities.AllDescriptionEvent
import org.adt.core.entities.EventStatus
import org.adt.core.entities.event.CoordinatorEventSummary
import org.adt.core.entities.event.EventApplication
import org.adt.core.entities.event.EventLocation
import org.adt.presentation.R
import org.adt.presentation.components.buttons.ButtonColorScheme
import org.adt.presentation.components.buttons.ButtonStyle
import org.adt.presentation.components.buttons.ButtonVariant
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.components.buttons.SquaredIconButton
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Black
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Grey
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.Silver
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.VolunteersCaseTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatEventDate(isoString: String): Pair<String, String> {
    return try {
        val dateTime = LocalDateTime.parse(isoString)
        val time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        val date = dateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
        time to date
    } catch (e: Exception) {
        "00:00" to "00.00"
    }
}

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
            Text(
                title,
                style = VolunteersCaseTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
            )
            Text(
                date,
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    color = Silver,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        Text(time, style = VolunteersCaseTheme.typography.titleLarge.copy(fontSize = 40.sp))

        SquaredIconButton(R.drawable.ic_trash, 45.dp, "Delete") { onDeleteClick() }
    }
}

/**
 * The card consists of an icon and text and is used to view statistics
 *
 * @param modifier modifier for managing card sizes
 *
 * @param onClick function to be invoked on card click.
 *
 * @sample [StatisticsCardPreview]
 */
@Composable
fun StatisticsCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
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
                style = VolunteersCaseTheme.typography.titleMedium.copy(Black),
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

/**
 * The card consists of an icon and text and is used to view calendar
 *
 * @param modifier modifier for managing card sizes
 *
 * @param onClick function to be invoked on card click.
 *
 * @sample [CalendarCardPreview]
 */
@Composable
fun CalendarCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
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
                style = VolunteersCaseTheme.typography.titleMedium.copy(Black)
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

/**
 * The card consists of a text and is used to view the number of people you have helped.
 *
 * @param modifier modifier for managing card sizes.
 *
 * @param number number of people you have helped.
 *
 * @sample [CharityStatisticsCardPreview]
 */
@Composable
fun CharityStatisticsCard(modifier: Modifier = Modifier, number: Int) {

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
            text = number.toString(),
            style = VolunteersCaseTheme.typography.titleMedium.copy(
                color = Arctic,
                fontSize = 36.sp
            ),
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}


/**
 * The card consists of a text and is used to view the number charity.
 *
 * @param modifier modifier for managing card sizes.
 *
 * @param numberCharity number of charity.
 *
 * @sample [CharityStatisticsCardPreview]
 */

@Composable
fun OverallCharityStatisticsCard(modifier: Modifier = Modifier, numberCharity: Int) {
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
                text = numberCharity.toString(),
                style = VolunteersCaseTheme.typography.titleMedium.copy(Arctic, fontSize = 48.sp),
                modifier = Modifier.padding(start = 32.dp, end = 10.dp)
            )
            Text(
                text = "добрых дел",
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontSize = 32.sp,
                    color = Arctic
                )
            )
        }
        Text(
            text = "вы обладатель большого сердца!",
            style = VolunteersCaseTheme.typography.titleMedium.copy(fontSize = 16.sp, color = Grey),
            modifier = Modifier.padding(start = 32.dp)
        )
    }

}

/**
 * The card consists of text and is used to view the number of hours of charity.
 *
 * @param modifier modifier for managing card sizes.
 *
 * @param hoursCharity number of hours of charity.
 *
 * @sample [CharityHoursCardPreview]
 */

@Composable
fun CharityHoursCard(modifier: Modifier = Modifier, hoursCharity: Int) {
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
                text = hoursCharity.toString(),
                style = VolunteersCaseTheme.typography.titleMedium.copy(Arctic, fontSize = 48.sp),
                modifier = Modifier.padding(start = 17.dp, end = 11.dp)
            )
            Text(
                text = "добрых часов",
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontSize = 32.sp,
                    color = Arctic
                )
            )
        }
        Text(
            text = "Каждая минута, что вы отдали, стала чьим-то лучиком солнца",
            style = VolunteersCaseTheme.typography.titleMedium.copy(fontSize = 16.sp, color = Grey),
            modifier = Modifier.padding(start = 25.dp, end = 25.dp),
            textAlign = TextAlign.Center
        )
    }

}


/**
 * The card consists of text and is used to display the achievements of the week.
 *
 * @param modifier modifier for managing card sizes.
 *
 * @param achievement achievement of the week.
 *
 * @sample [AchievementOfTheWeekCardPreview]
 */
@Composable
fun AchievementOfTheWeekCard(modifier: Modifier = Modifier, achievement: String) {
    Column(
        modifier = modifier
            .width(145.dp)
            .height(124.dp)
            .background(color = Lagoon, shape = RoundedCornerShape(14.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "достижение недели",
            style = VolunteersCaseTheme.typography.titleMedium.copy(
                fontSize = 15.sp,
                color = Arctic
            ),
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
            style = VolunteersCaseTheme.typography.titleMedium.copy(
                fontSize = 13.sp,
                color = Arctic
            )
        )
    }
}

/**
 * The card consists of text and is used to add an event.
 *
 * @param modifier modifier for managing card sizes.
 *
 * @param onClick function to be invoked on card click.
 *
 * @sample [AddEventCardPreview]
 */

@Composable
fun AddEventCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
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
                style = VolunteersCaseTheme.typography.titleMedium.copy(color = Black),
                color = Black,
                modifier = Modifier.padding(
                    bottom = 3.dp,
                    top = 4.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
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

/**
 * The card consists of text and is used to view events title, time, date and image .
 *
 * @param modifier modifier for managing card sizes.
 *
 * @param allDescriptionEvent date class that stores image, title, description, time, date event.
 *
 * @sample [EventCardPreview]
 */

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    allDescriptionEvent: AllDescriptionEvent,
    isParticipating: (AllDescriptionEvent) -> Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFDBC7B5))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = allDescriptionEvent.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.White, Color.Transparent),
                            startX = 0f,
                            endX = size.width
                        ),
                        blendMode = BlendMode.DstIn
                    )
                },
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.baseimage),
            fallback = painterResource(R.drawable.baseimage)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = allDescriptionEvent.title,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                        fontSize = 14.sp,
                        color = Arctic,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = when (allDescriptionEvent.status) {
                        EventStatus.ONGOING -> "Предстоит!"
                        EventStatus.IN_PROGRESS -> "Уже идёт!"
                        EventStatus.COMPLETED -> "Завершено!"
                        else -> "Неизвестно"
                    },
                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                        fontSize = 11.sp,
                        color = Arctic.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = allDescriptionEvent.date,
                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                        fontSize = 11.sp,
                        color = Arctic.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = allDescriptionEvent.time,
                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                        fontSize = 11.sp,
                        color = Arctic.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = allDescriptionEvent.location.address,
                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                        fontSize = 11.sp,
                        color = Arctic.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        if (isParticipating(allDescriptionEvent)) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Green.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Вы участвуете!",
                    color = Color.Green,
                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}



/**
 * The card consists of text and is used to view events title, description and image .
 *
 * @param modifier modifier for managing card sizes.
 *
 * @param allDescriptionEvent date class that stores image, title, description, time, date event.
 *
 * @param onClick function to be invoked on card click.
 *
 * @sample [OverallDescriptionEventCardPreview]
 */
@Composable
fun OverallDescriptionEventCard(
    modifier: Modifier = Modifier,
    allDescriptionEvent: AllDescriptionEvent,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val buttonText = when {
        !enabled -> "Заявка подана"
        allDescriptionEvent.status == EventStatus.COMPLETED -> "Завершено"
        allDescriptionEvent.status != EventStatus.ONGOING -> "Уже началось"
        else -> "Приступить!"
    }

    val state = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
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
            AsyncImage(
                allDescriptionEvent.image, "event cover", Modifier
                    .clip(shape = RoundedCornerShape(17.dp))
                    .width(279.dp)
                    .height(259.dp),
                contentScale = ContentScale.Crop,
                fallback = painterResource(id = R.drawable.baseimage),
                error = painterResource(id = R.drawable.baseimage)
            )
        }
        Text(
            text = (if (allDescriptionEvent.title == "") stringResource(R.string.base_title) else allDescriptionEvent.title),
            style = VolunteersCaseTheme.typography.titleLarge.copy(
                fontSize = 19.sp,
                color = Black
            ),
            modifier = Modifier
                .padding(top = 15.dp, start = 10.dp)
                .fillMaxWidth()
        )
        Text(
            text = (if (allDescriptionEvent.description == "") stringResource(R.string.base_description) else allDescriptionEvent.description),
            style = VolunteersCaseTheme.typography.titleMedium.copy(
                fontSize = 15.sp,
                color = Grey
            ),
            modifier = Modifier
                .padding(top = 9.dp)
                .width(300.dp)
                .height(61.dp)
                .verticalScroll(state)
        )

        CustomButton(
            text = buttonText, modifier = Modifier.padding(
                start = 11.dp,
                end = 10.dp,
                top = 28.dp
            ),
            style = ButtonStyle.Filled,
            onClick = onClick,
            enabled = allDescriptionEvent.status == EventStatus.ONGOING && enabled
        )
    }
}

/**
 * The card consists of text and is used to monitoring activity.
 *
 * @param modifier modifier for managing card sizes.
 *
 * @param color color card.
 *
 * @param count activity.
 *
 * @param parameter monitoring parameter.
 *
 * @sample [EventMonitoringCardPreview]
 */

@Composable
fun EventMonitoringCard(
    modifier: Modifier = Modifier,
    color: Color,
    count: Int,
    parameter: String
) {
    Row(
        modifier = modifier
            .width(300.dp)
            .height(85.dp)
            .background(color = color, shape = RoundedCornerShape(14.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = count.toString(),
            style = VolunteersCaseTheme.typography.titleMedium.copy(
                fontSize = 48.sp,
                color = Arctic
            ),
            modifier = Modifier.padding(start = 35.dp, top = 14.dp)
        )
        Text(
            text = parameter,
            style = VolunteersCaseTheme.typography.titleMedium.copy(
                fontSize = 22.sp,
                color = Arctic
            ),
            modifier = Modifier.padding(end = 15.dp, top = 18.dp, start = 22.dp),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * This card consists of several text fields that display all/new/active users counters for current event.
 *
 * @param modifier card modifier.
 *
 * @param color background color.
 *
 * @param countActive active users count.
 *
 * @param countNew new users count.
 *
 * @param countAll all users count.
 *
 * @sample [EventMonitoringCardPreview]
 */

@Composable
fun UserCountCard(
    modifier: Modifier = Modifier,
    countAll: Int,
    countNew: Int,
    countActive: Int,
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
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    color = Arctic
                ),
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
            Text(
                text = countAll.toString(),
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    color = Arctic
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "Новые:",
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    color = Arctic
                ),
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
            Text(
                text = countNew.toString(),
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    color = Arctic
                )
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "Активные:",
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    color = Arctic
                ),
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
            Text(
                text = countActive.toString(),
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    color = Arctic
                )
            )
        }

    }
}

@Composable
fun EventSearchCard(
    modifier: Modifier = Modifier,
    image: String?,
    name: String,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable(onClick = onClick)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            image, null, Modifier
                .size(40.dp)
                .clip(shape = RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            fallback = painterResource(id = R.drawable.baseimage),
            error = painterResource(id = R.drawable.baseimage)
        )

        Text(
            name,
            color = Arctic,
            modifier = Modifier
        )
    }
}

@Composable
fun ApplicationCard(
    app: EventApplication,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column (
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${app.firstname} ${app.lastname}",
                style = VolunteersCaseTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Void
            )


            val dateText = app.createdAt?.substringBefore("T") ?: ""
            if (dateText.isNotBlank()) {
                Text(
                    text = dateText,
                    style = VolunteersCaseTheme.typography.labelSmall,
                    color = Graphite.copy(alpha = 0.4f)
                )
            }
        }


        Text(
            text = app.email,
            color = Lagoon,
            style = VolunteersCaseTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )


        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            CustomButton(
                modifier = Modifier.weight(1f),
                text = "Отклонить",
                style = ButtonStyle.Outlined,
                variant = ButtonVariant.LiteRounded,
                colors = ButtonColorScheme(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFD32F2F).copy(alpha = 0.7f),
                    borderColor = Color(0xFFD32F2F).copy(alpha = 0.2f)
                ),
                onClick = onReject
            )

            CustomButton(
                modifier = Modifier.weight(1.5f),
                text = "Принять",
                style = ButtonStyle.Filled,
                variant = ButtonVariant.LiteRounded,
                colors = ButtonColorScheme(
                    containerColor = Lagoon,
                    contentColor = Color.White
                ),
                onClick = onApprove
            )
        }
    }
}

@Composable
fun EventSummaryCard(
    event: CoordinatorEventSummary,
    onClick: () -> Unit
) {
    val remainingPlaces = (event.maxCapacity - event.acceptedCount).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = androidx.compose.material3.ripple(color = Lagoon.copy(alpha = 0.1f)),
                enabled = true
            ) {
                onClick()
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = event.eventName,
                style = VolunteersCaseTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Void,
                modifier = Modifier.weight(1f)
            )

            if (event.pendingCount > 0) {
                Box(
                    Modifier
                        .padding(start = 8.getClipDpIfNeeded())
                        .clip(RoundedCornerShape(8.dp))
                        .background(Mint.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+${event.pendingCount}",
                        color = Abyss,
                        style = VolunteersCaseTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }


        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            MiniStatusChip(text = "${event.acceptedCount} одобрено", containerColor = Lagoon.copy(alpha = 0.08f), contentColor = Lagoon)

            if (event.rejectedCount > 0) {
                MiniStatusChip(text = "${event.rejectedCount} отказ", containerColor = Color(0xFFD32F2F).copy(alpha = 0.06f), contentColor = Color(0xFFD32F2F).copy(alpha = 0.7f))
            }

            val placesColor = if (remainingPlaces > 0) Mint else Graphite.copy(alpha = 0.4f)
            val placesBg = if (remainingPlaces > 0) Mint.copy(alpha = 0.1f) else Graphite.copy(alpha = 0.04f)

            MiniStatusChip(
                text = if (remainingPlaces > 0) "$remainingPlaces мест" else "Мест нет",
                containerColor = placesBg,
                contentColor = placesColor
            )
        }
    }
}

@Composable
private fun MiniStatusChip(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = VolunteersCaseTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

private fun Int.getClipDpIfNeeded() = this.dp

@Preview
@Composable
private fun UserCountCardPreview() {
    UserCountCard(
        modifier = Modifier,
        countAll = 50,
        countNew = 14,
        countActive = 24,
        size = 172.dp
    )
}


@Preview
@Composable
private fun AddEventCardPreview() {
    VolunteersCaseTheme { AddEventCard { } }
}

@Preview
@Composable
private fun CalendarCardPreview() {
    VolunteersCaseTheme { CalendarCard { } }

}

@Preview
@Composable
private fun StatisticsCardPreview() {
    VolunteersCaseTheme { StatisticsCard { } }
}

@Preview
@Composable
private fun EventCardPreview() {
    EventCard(
        modifier = Modifier,
        allDescriptionEvent = AllDescriptionEvent(
            -1,
            "",
            "Соседский книжный шкаф",
            "Создай в своём дворе библиотеку для всех желающих: поставь полку, делись книгами и поддерживай в ней порядок.",
            time = "13:40",
            date = "01.01",
            EventStatus.ONGOING,
            EventLocation()
        ),
        isParticipating = {true}
    ) {}

}

@Preview
@Composable
private fun CharityStatisticsCardPreview() {
    CharityStatisticsCard(
        modifier = Modifier,
        number = 147
    )
}


@Preview
@Composable
private fun OverallCharityStatisticsCardPreview() {
    OverallCharityStatisticsCard(
        modifier = Modifier,
        numberCharity = 50
    )
}

@Preview
@Composable
private fun CharityHoursCardPreview() {
    CharityHoursCard(
        modifier = Modifier,
        hoursCharity = 70
    )
}

@Preview
@Composable
private fun AchievementOfTheWeekCardPreview() {
    AchievementOfTheWeekCard(
        modifier = Modifier,
        achievement = "эко-герой"
    )
}

@Preview
@Composable
private fun OverallDescriptionEventCardPreview() {
    VolunteersCaseTheme {
        OverallDescriptionEventCard(
            modifier = Modifier,
            allDescriptionEvent = AllDescriptionEvent(
                -1,
                "",
                "Соседский книжный шкаф",
                "Создай в своём дворе библиотеку для всех желающих: поставь полку, делись книгами и поддерживай в ней порядок.",
                time = "13:40",
                date = "01.01",
                EventStatus.ONGOING,
                location = EventLocation()
            )
        ) {}
    }

}

@Preview
@Composable
private fun EventMonitoringCardPreview() {
    EventMonitoringCard(
        modifier = Modifier,
        color = Abyss,
        count = 10,
        parameter = "подозрительных входа"
    )
}

@Preview
@Composable
private fun EventSearchCardPreview() {
    EventSearchCard(
        modifier = Modifier,
        image = "",
        name = "Example"
    ) {}
}

@Preview(showBackground = true, name = "Application Card - Active")
@Composable
private fun ApplicationCardPreview() {
    val mockApp = remember {
        EventApplication(
            eventId = 42,
            eventName = "Экологический форум",
            userId = 107,
            firstname = "Александр",
            lastname = "Верховный",
            patronymic = "Сергеевич",
            phoneNumber = "+79991234567",
            email = "alex.volunteer@adt.org",
            status = "PENDING",
            rejectReason = null,
            createdAt = "2026-05-28T12:00:00",
            rejectedAt = null,
            revokedAt = null
        )
    }

    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk) // Цвет подложки экрана
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp,
                color = Arctic // Фон самой карточки
            ) {
                ApplicationCard(
                    app = mockApp,
                    onApprove = {},
                    onReject = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Event Summary - With Invites")
@Composable
private fun EventSummaryCardWithPendingPreview() {
    val mockEvent = remember {
        CoordinatorEventSummary(
            eventId = 1L,
            eventName = "Экологический форум «Чистый город»",
            eventStatus = "ACTIVE",
            dateTimestamp = "2026-06-01T10:00:00",
            maxCapacity = 50L,
            applicationsTotal = 42,
            pendingCount = 3,
            acceptedCount = 34,
            rejectedCount = 5,
            revokedCount = 0
        )
    }

    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp,
                color = Arctic
            ) {
                EventSummaryCard(
                    event = mockEvent,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Event Summary - Full Capacity")
@Composable
private fun EventSummaryCardFullPreview() {
    val mockEvent = remember {
        CoordinatorEventSummary(
            eventId = 2L,
            eventName = "Помощь в приюте для животных «Верный друг»",
            eventStatus = "ACTIVE",
            dateTimestamp = "2026-06-05T14:00:00",
            maxCapacity = 15L,
            applicationsTotal = 27,
            pendingCount = 0,
            acceptedCount = 15,
            rejectedCount = 12,
            revokedCount = 0
        )
    }

    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp,
                color = Arctic
            ) {
                EventSummaryCard(
                    event = mockEvent,
                    onClick = {}
                )
            }
        }
    }
}