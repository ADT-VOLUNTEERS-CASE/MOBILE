package org.adt.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.adt.core.entities.EventStatus
import org.adt.core.entities.event.Cover
import org.adt.core.entities.event.Event
import org.adt.core.entities.event.EventLocation
import org.adt.presentation.R


@Composable
fun CharityEventCard(
    event: Event,
    isParticipating: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor = when (event.status) {
        EventStatus.ONGOING -> Color(0xFFFFB300)
        EventStatus.IN_PROGRESS -> Color(0xFF4FC3F7)
        EventStatus.COMPLETED -> Color(0xFF9E9E9E)
        EventStatus.UNKNOWN -> Color(0xFFBDBDBD)
    }
    val statusText = when (event.status) {
        EventStatus.ONGOING -> stringResource(R.string.label_event_status_ongoing)
        EventStatus.IN_PROGRESS -> stringResource(R.string.label_event_status_in_progress)
        EventStatus.COMPLETED -> stringResource(R.string.label_event_status_completed)
        EventStatus.UNKNOWN -> stringResource(R.string.label_event_status_unknown)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = event.cover?.link,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )

                if (isParticipating) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.body_participating),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    color = statusColor.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = statusText,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.localizedDate,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        maxLines = 1
                    )
                    Text(
                        text = event.localizedDateTime,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = event.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = event.description,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.DarkGray,
                    maxLines = 2,
                    minLines = 2,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = event.maxCapacity.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(2f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = event.location.address,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharityEventsGrid(
    events: List<Event>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(events) { event ->
            CharityEventCard(
                event = event,
                onClick = { },
                onFavoriteClick = { },
                isParticipating = true
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F9FA)
@Composable
private fun CharityEventsGridPreview() {
    val mockEvents = listOf(
        Event(
            eventId = 1,
            name = "Очистка лесопарка",
            description = "Присоединяйтесь к ежегодной акции по уборке мусора. Мы обеспечим вас перчатками и мешками.",
            dateTimestamp = "",
            status = EventStatus.ONGOING,
            location = EventLocation(address = "Елагин остров"),
            cover = Cover(link = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fpodtepeto.com%2Fwp-content%2Fuploads%2F2023%2F03%2Flauta-2-scaled.jpg&f=1&nofb=1&ipt=bc0efd482b45b8177cd56bd8bf19922b72553da6421582312d43992c0c0ab6ad")
        ),
        Event(
            eventId = 2,
            name = "Помощь приюту",
            description = "Нужны волонтеры для выгула собак и мелкого ремонта вольеров. Подарите радость питомцам!",
            dateTimestamp = "",
            status = EventStatus.IN_PROGRESS,
            location = EventLocation(address = "Приют 'Друг'"),
            cover = Cover(link = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Frayfund.ru%2Fwp-content%2Fuploads%2F2024%2F07%2F1_zoozavr_beskuudnikovo-1.jpg&f=1&nofb=1&ipt=832a31f6856b83f70402cac85f9de759375527be3756f19d95a169a5b6b2d12d")
        ),
        Event(
            eventId = 3,
            name = "Обед для всех",
            description = "Помощь в раздаче горячих обедов людям, попавшим в трудную жизненную ситуацию.",
            dateTimestamp = "",
            status = EventStatus.ONGOING,
            location = EventLocation(address = "Лиговский 44"),
            cover = Cover(link = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fvtranse.life%2Fwp-content%2Fuploads%2F2023%2F06%2FFHCuRWNWYAM4chw-1024x576.jpg&f=1&nofb=1&ipt=59bd976280ce632315cf66ce5e3d60d6832661572526b8c75d612bd56e33c35c")
        ),
        Event(
            eventId = 4,
            name = "Сбор учебников",
            description = "Акция по сбору подержанной литературы и учебников для сельских библиотек области.",
            dateTimestamp = "",
            status = EventStatus.COMPLETED,
            location = EventLocation(address = "Библиотека №5"),
            cover = Cover(link = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Favatars.mds.yandex.net%2Fi%3Fid%3Dce4487f90aa7c31e1244bcd9baa6ba3b_l-5213474-images-thumbs%26n%3D13&f=1&nofb=1&ipt=113e3e71ee0a00100351abaa22426d063fad21028108f1b7c5c27c09b48a7e86")
        ),
        Event(
            eventId = 5,
            name = "Визит к ветеранам",
            description = "Организация досуга и помощи по дому для пожилых жителей нашего района.",
            dateTimestamp = "",
            status = EventStatus.ONGOING,
            location = EventLocation(address = "Ул. Мира 10"),
            cover = Cover(link = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fshokan.edu.kz%2Fmedia%2Fimages%2F120420241222_5.original.jpg&f=1&nofb=1&ipt=2a08061e187de66e4abf09f7f7490f63aefaf08032c06edd113bbf61c6712174")
        ),
        Event(
            eventId = 6,
            name = "Эко-лекторий",
            description = "Интерактивная лекция о раздельном сборе мусора и осознанном потреблении ресурсов.",
            dateTimestamp = "",
            status = EventStatus.IN_PROGRESS,
            location = EventLocation(address = "Хаб 'Севкабель'"),
            cover = Cover(link = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.nashe-slovo.ru%2Fwp-content%2Fuploads%2F2021%2F04%2F%25D1%2582%25D0%25BE%25D1%2587%25D0%25BA%25D0%25B0-%25D1%2580%25D0%25BE%25D1%2581%25D1%2582%25D0%25B0.jpg&f=1&nofb=1&ipt=64ad282398a4dff34a8f2df34533db2e9a200b9160f33f2a07a2c9ebb49170ef")
        ),
        Event(
            eventId = 7,
            name = "Посадка аллеи",
            description = "Высадка саженцев клена в новом сквере. Инструменты выдаем на месте.",
            dateTimestamp = "26 мая",
            status = EventStatus.ONGOING,
            location = EventLocation(address = "Парк Победы"),
            cover = Cover(link = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fbigfoto.name%2Fuploads%2Fposts%2F2021-12%2F1638443077_2-bigfoto-name-p-dizain-allei-2.jpg&f=1&nofb=1&ipt=82b60af2de76b9c5220b0f0fcf1ffebec8ba126789cfd70eb7263ceea3b222bc")
        ),
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FA)) {
        CharityEventsGrid(events = mockEvents)
    }
}