package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.LocalUser
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
    currentUser: LocalUser? = null,
    lang: Lang = Lang.KZ,
    onCarChosen: (Int) -> Unit
) {
    val initialGreeting = if (lang == Lang.KZ) {
        "Сәлем! Мен Smart CarShare ЖИ көмекшісімін. Профиліңіз, CashAuto, Anytime немесе көліктер туралы кез келген сұрақ қоя аласыз."
    } else {
        "Привет! Я AI-помощник Smart CarShare. Вы можете задать любые вопросы о профиле, компаниях CashAuto, Anytime или автомобилях."
    }

    var messages by remember {
        mutableStateOf<List<ChatItem>>(listOf(ChatItem.BotMsg(initialGreeting)))
    }

    var inputText by remember { mutableStateOf("") }

    var chips by remember {
        mutableStateOf(
            if (lang == Lang.KZ) {
                listOf("CashAuto", "Anytime", "Менің профилім", "Camry 80", "Тарифтер")
            } else {
                listOf("CashAuto", "Anytime", "Мой профиль", "Camry 80", "Тарифы")
            }
        )
    }

    var pendingReply by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Surface1,
        unfocusedContainerColor = Surface1,
        focusedBorderColor = Ice,
        unfocusedBorderColor = BorderColor,
        focusedLabelColor = Ice,
        unfocusedLabelColor = TextMid,
        focusedTextColor = TextHi,
        unfocusedTextColor = TextHi
    )

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val userText = text.trim()
        messages = messages + ChatItem.UserMsg(userText)
        inputText = ""
        pendingReply = userText
    }

    fun generateSmartBotResponses(query: String, cars: List<Car>, user: LocalUser?, lang: Lang): List<ChatItem> {
        val q = query.lowercase()
        val results = mutableListOf<ChatItem>()

        val matchedCars = cars.filter { car ->
            q.contains(car.name.lowercase()) ||
            car.tags.any { tag -> q.contains(tag.lowercase()) }
        }

        when {
            q.contains("профиль") || q.contains("менің") || q.contains("почта") || q.contains("телефон") || q.contains("аты") -> {
                val name = user?.name ?: (if (lang == Lang.KZ) "Тіркелмеген" else "Не авторизован")
                val phone = user?.phone ?: "-"
                val email = user?.email ?: "-"
                results.add(
                    ChatItem.BotMsg(
                        if (lang == Lang.KZ) {
                            "Сіздің профиль деректеріңіз:\n• Аты-жөні: $name\n• Телефон: $phone\n• Email: $email\n• Статус: Firestore-мен синхрондалған"
                        } else {
                            "Данные вашего профиля:\n• Имя: $name\n• Телефон: $phone\n• Email: $email\n• Статус: Синхронизировано с Firestore"
                        }
                    )
                )
            }
            q.contains("cashauto") || q.contains("кашавто") -> {
                results.add(
                    ChatItem.BotMsg(
                        if (lang == Lang.KZ) {
                            "🏢 CashAuto компаниясы (Астана):\n• Филиал 1: Аэропорт Т1 (24/7)\n• Филиал 2: Жайдарман көшесі, 2/1\n• Байланыс телефондары: +7 (777) 999-11-12, +7 (702) 998-11-12\n• Қызметтері: Тәулік бойы авто/мото жалға беру, СТО, автожуу және экспресс май ауыстыру."
                        } else {
                            "🏢 Компания CashAuto (Астана):\n• Филиал 1: Аэропорт Т1 (24/7)\n• Филиал 2: ул. Жайдарман, 2/1\n• Телефоны: +7 (777) 999-11-12, +7 (702) 998-11-12\n• Услуги: Аренда авто/мото 24/7, СТО, мойка и экспресс замена масла."
                        }
                    )
                )
                cars.filter { it.company.contains("CashAuto", ignoreCase = true) }.forEach {
                    results.add(ChatItem.RecCard(it))
                }
            }
            q.contains("anytime") || q.contains("энимайм") -> {
                results.add(
                    ChatItem.BotMsg(
                        if (lang == Lang.KZ) {
                            "🚗 Anytime Kazakhstan (Астана):\n• Астана қаласы бойынша кең автопарк (Chery Tiggo 7 Pro, Hyundai Accent, Haval Jolion, Geely Coolray).\n• Сол жағалау, EXPO, Бәйтерек, вокзал және әуежай маңында көптеген қолжетімді көліктер бар."
                        } else {
                            "🚗 Anytime Kazakhstan (Астана):\n• Широкий автопарк по всему городу (Chery Tiggo 7 Pro, Hyundai Accent, Haval Jolion, Geely Coolray).\n• Много свободных авто на Левом берегу, у EXPO, Байтерека, вокзала и аэропорта."
                        }
                    )
                )
                cars.filter { it.company.contains("Anytime", ignoreCase = true) }.forEach {
                    results.add(ChatItem.RecCard(it))
                }
            }
            matchedCars.isNotEmpty() -> {
                results.add(ChatItem.BotMsg(if (lang == Lang.KZ) "Сіздің сұранысыңызға сай табылды:" else "Найдено по вашему запросу:"))
                matchedCars.take(3).forEach { results.add(ChatItem.RecCard(it)) }
            }
            q.contains("цена") || q.contains("баға") || q.contains("теңге") || q.contains("тг") || q.contains("құны") || q.contains("сколько") -> {
                results.add(ChatItem.BotMsg(if (lang == Lang.KZ) "Тарифтер сағатына 800 ₸-ден (Chevrolet Cobalt) басталады. Тәуліктік тарифтер 10 000 ₸ мен 66 000 ₸ (Cadillac Escalade) аралығында." else "Тарифы начинаются от 800 ₸/час (Chevrolet Cobalt). Посуточные тарифы — от 10 000 ₸ до 66 000 ₸."))
                results.add(ChatItem.RecCard(cars.first()))
            }
            q.contains("құжат") || q.contains("права") || q.contains("воден") || q.contains("стаж") || q.contains("жас") || q.contains("возраст") -> {
                results.add(ChatItem.BotMsg(if (lang == Lang.KZ) "Аренда үшін талаптар: жасы 21-ден жоғары және жүргізуші куәлігі (стаж 3 жылдан бастап)." else "Требования для аренды: возраст от 21 года и водительское удостоверение (стаж от 3 лет)."))
            }
            q.contains("көлік") || q.contains("қосам") || q.contains("сдать") || q.contains("доход") -> {
                results.add(ChatItem.BotMsg(if (lang == Lang.KZ) "Өз көлігіңізді жалға беру үшін Профиль бөліміне өтіп, «Өз көлігімді жалға беру» батырмасын басыңыз. Табыс тікелей сіздің Kaspi номеріңізге түседі!" else "Чтобы сдать свое авто в аренду, перейдите в раздел Профиль и нажмите кнопку «Сдать свое авто в аренду». Доход будет поступать напрямую на ваш Kaspi номер!"))
            }
            else -> {
                results.add(ChatItem.BotMsg(if (lang == Lang.KZ) "Міне, сізге Астанадағы тамаша көлік ұсынысы:" else "Вот отличное предложение автомобиля в Астане:"))
                results.add(ChatItem.RecCard(cars.randomOrNull() ?: cars[0]))
            }
        }
        return results
    }

    LaunchedEffect(pendingReply) {
        val reply = pendingReply ?: return@LaunchedEffect
        delay(600)
        val botResponses = generateSmartBotResponses(reply, cars, currentUser, lang)
        messages = messages + botResponses
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
            Icon(Icons.Filled.SmartToy, contentDescription = null, tint = Amber, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(10.dp))
            Text(
                if (lang == Lang.KZ) "Smart AI Көмекші" else "Smart AI Помощник",
                style = MaterialTheme.typography.titleLarge,
                color = TextHi,
                modifier = Modifier.weight(1f)
            )
            Chip("Firestore & Fleet", accent = true)
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
                    is ChatItem.RecCard -> RecCardRow(item.car, lang) { onCarChosen(item.car.id) }
                }
            }
        }

        if (chips.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chips) { chipText ->
                    Box(
                        modifier = Modifier
                            .background(Surface1, RoundedCornerShape(100.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(100.dp))
                            .clickable { sendMessage(chipText) }
                            .padding(horizontal = 13.dp, vertical = 8.dp)
                    ) {
                        Text(chipText, style = MaterialTheme.typography.bodySmall, color = TextHi)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy800)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text(if (lang == Lang.KZ) "Хабарлама жазу..." else "Введите сообщение...", color = TextMid) },
                colors = textFieldColors,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            IconButton(
                onClick = { sendMessage(inputText) },
                modifier = Modifier
                    .size(48.dp)
                    .background(Amber, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Жіберу", tint = AmberOnDark, modifier = Modifier.size(20.dp))
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
                .widthIn(max = 280.dp)
                .background(if (isUser) Amber else Surface1, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
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
private fun RecCardRow(car: Car, lang: Lang, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface1, RoundedCornerShape(16.dp))
            .border(1.dp, Ice.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clickable { onSelect() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(car.name, style = MaterialTheme.typography.titleMedium, color = TextHi)
            Text(car.location, style = MaterialTheme.typography.bodySmall, color = Ice)
            Spacer(Modifier.height(4.dp))
            Text(
                "${formatTenge(car.pricePerHour)} " + if (lang == Lang.KZ) "/ сағат" else "/ час",
                style = MaterialTheme.typography.labelLarge,
                color = Amber
            )
        }
        Box(
            modifier = Modifier
                .background(Amber, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                if (lang == Lang.KZ) "Таңдау" else "Выбрать",
                style = MaterialTheme.typography.labelLarge,
                color = AmberOnDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
