package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

private sealed class ChatItem {
    data class BotMsg(val text: String) : ChatItem()
    data class UserMsg(val text: String) : ChatItem()
    data class RecCard(val car: Car) : ChatItem()
}

@Composable
fun ChatScreen(
    cars: List<Car>,
    onCarChosen: (Int) -> Unit
) {
    var messages by remember {
        mutableStateOf<List<ChatItem>>(
            listOf(ChatItem.BotMsg("Сәлем! Бюджетіңіз бен қажетті сипаттамаларды айтыңыз, мен сізге сай көлікті табайын"))
        )
    }
    var step by remember { mutableStateOf(0) }
    var chips by remember {
        mutableStateOf(listOf("15 000 ₸/күн, 5 орын", "Үнемді, қала үшін", "Бизнес-сапарға"))
    }
    var pendingReply by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    LaunchedEffect(pendingReply) {
        val reply = pendingReply ?: return@LaunchedEffect
        delay(900)
        when (step) {
            1 -> {
                val car = when {
                    reply.contains("Үнемді") -> cars[1]
                    reply.contains("Бизнес") -> cars[2]
                    else -> cars[0]
                }
                messages = messages +
                        ChatItem.BotMsg("Түсінікті! Осы параметрлерге сай ең қолайлы нұсқаны таптым:") +
                        ChatItem.RecCard(car)
                chips = listOf("Салонда бала креслосы бар ма?", "Басқа нұсқа ұсын")
            }
            2 -> {
                messages = if (reply.contains("креслосы")) {
                    messages + ChatItem.BotMsg("Иә, таңдалған көлікте ISOFIX бекітпесі бар, бала креслосын орнатуға болады.")
                } else {
                    messages + ChatItem.BotMsg("Әрине! Тағы бір нұсқа:") + ChatItem.RecCard(cars[1])
                }
                chips = listOf("Рахмет, осы жеткілікті")
            }
            else -> {
                messages = messages + ChatItem.BotMsg("Өтінішіңіз үшін рахмет! Көлікті таңдау үшін карточканы басыңыз.")
                chips = emptyList()
            }
        }
        pendingReply = null
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "ЖИ-көмекші",
                style = MaterialTheme.typography.titleLarge,
                color = TextHi,
                modifier = Modifier.weight(1f)
            )
            Chip("онлайн", accent = true)
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { item ->
                when (item) {
                    is ChatItem.BotMsg -> Bubble(item.text, isUser = false)
                    is ChatItem.UserMsg -> Bubble(item.text, isUser = true)
                    is ChatItem.RecCard -> RecCardRow(item.car) { onCarChosen(item.car.id) }
                }
            }
        }

        if (chips.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chips) { chipText ->
                    Box(
                        modifier = Modifier
                            .background(Surface1, RoundedCornerShape(100.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(100.dp))
                            .clickable {
                                messages = messages + ChatItem.UserMsg(chipText)
                                chips = emptyList()
                                step += 1
                                pendingReply = chipText
                            }
                            .padding(horizontal = 13.dp, vertical = 8.dp)
                    ) {
                        Text(chipText, style = MaterialTheme.typography.bodySmall, color = TextHi)
                    }
                }
            }
        }
    }
}

@Composable
private fun Bubble(text: String, isUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 260.dp)
                .background(if (isUser) Amber else Surface1, RoundedCornerShape(16.dp))
                .padding(horizontal = 13.dp, vertical = 10.dp)
        ) {
            Text(
                text,
                color = if (isUser) AmberOnDark else TextHi,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun RecCardRow(car: Car, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Navy900, RoundedCornerShape(14.dp))
            .border(1.dp, Ice.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column {
            Text(car.name, style = MaterialTheme.typography.labelLarge, color = TextHi)
            Text(
                "${formatTenge(car.pricePerHour)} / сағат",
                style = MaterialTheme.typography.bodySmall,
                color = TextMid
            )
        }
        Box(
            modifier = Modifier
                .background(Surface2, RoundedCornerShape(100.dp))
                .clickable { onSelect() }
                .padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Text("Таңдау", style = MaterialTheme.typography.bodySmall, color = TextHi)
        }
    }
}