package kz.smartcarshare.app.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.data.repository.GeminiAiService
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

private sealed class ChatItem {
    data class BotMsg(val text: String) : ChatItem()
    data class UserMsg(val text: String) : ChatItem()
    data class RecCard(val car: Car) : ChatItem()
    object TypingIndicator : ChatItem()
}

@Composable
fun ChatScreen(
    cars: List<Car>,
    currentUser: LocalUser? = null,
    lang: Lang = Lang.KZ,
    onCarChosen: (Int) -> Unit
) {
    val initialGreeting = if (lang == Lang.KZ) {
        "Сәлем! Мен Smart CarShare ЖИ көмекшісімін. Көлік таңдау, CashAuto автопаркі, тарифтер, орналасу жері немесе профиль бойынша кез келген сұрағыңызға жауап беремін!"
    } else {
        "Привет! Я AI-помощник Smart CarShare. Помогу выбрать авто, расскажу про автопарк CashAuto, тарифы, контакты или ваш профиль!"
    }

    var messages by remember(lang) {
        mutableStateOf<List<ChatItem>>(listOf(ChatItem.BotMsg(initialGreeting)))
    }

    var inputText by remember { mutableStateOf("") }

    val chips = remember(lang) {
        if (lang == Lang.KZ) {
            listOf("🚗 3000₸ дейінгі көліктер", "⭐ Эконом класс", "🏍️ Мотоциклдер", "📍 Контакты & 2GIS", "👤 Профиль", "💎 Lux көліктер")
        } else {
            listOf("🚗 Машины до 3000₸", "⭐ Эконом класс", "🏍️ Мотоциклы", "📍 Контакты & 2GIS", "👤 Мой профиль", "💎 Люкс авто")
        }
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
        messages = messages + ChatItem.UserMsg(userText) + ChatItem.TypingIndicator
        inputText = ""
        pendingReply = userText
    }

    fun generateSmartBotResponses(query: String, cars: List<Car>, user: LocalUser?, lang: Lang): List<ChatItem> {
        val q = query.lowercase().trim()
        val results = mutableListOf<ChatItem>()

        // 1. Brand Matching (Prioritized! Handles Cyrillic transliterations like "Хюндай", "Тойота", "Киа", etc.)
        val brandMap = mapOf(
            "хюндай" to "hyundai", "хёндай" to "hyundai", "хендай" to "hyundai", "хундай" to "hyundai", "хюндаи" to "hyundai",
            "тойота" to "toyota", "тайота" to "toyota",
            "киа" to "kia", "кия" to "kia",
            "шевроле" to "chevrolet", "шевролет" to "chevrolet", "кобальт" to "cobalt",
            "чанган" to "changan", "чанань" to "changan",
            "рено" to "renault",
            "бмв" to "bmw",
            "кадиллак" to "cadillac", "кадилак" to "cadillac",
            "форд" to "ford",
            "додж" to "dodge",
            "лексус" to "lexus",
            "донгфенг" to "dongfeng", "донфенг" to "dongfeng",
            "дукати" to "ducati",
            "педа" to "peda",
            "вилими" to "vilimi",
            "сфмото" to "cfmoto"
        )

        val brandKeys = listOf("hyundai", "toyota", "kia", "chevrolet", "changan", "renault", "bmw", "cadillac", "ford", "dodge", "lexus", "dongfeng", "ducati", "peda", "vilimi", "cfmoto")

        val matchedBrandName = brandMap.entries.firstOrNull { (cyr, _) -> q.contains(cyr) }?.value
            ?: brandKeys.firstOrNull { eng -> q.contains(eng) }

        if (matchedBrandName != null) {
            val brandCars = cars.filter { car ->
                car.name.lowercase().contains(matchedBrandName)
            }
            if (brandCars.isNotEmpty()) {
                val displayName = matchedBrandName.replaceFirstChar { it.uppercase() }
                val headerText = if (lang == Lang.KZ) {
                    "🚗 «$displayName» брендінің барлық қолжетімді көліктері (барлығы ${brandCars.size} авто):"
                } else {
                    "🚗 Все доступные автомобили бренда $displayName (всего ${brandCars.size} авто):"
                }
                results.add(ChatItem.BotMsg(headerText))
                brandCars.forEach { results.add(ChatItem.RecCard(it)) }
                return results
            }
        }

        // Model Matching (e.g. "Camry", "Камри", "Tucson", "Туксон", "Mustang", etc.)
        val modelMap = mapOf(
            "камри" to "camry", "camry" to "camry",
            "кобальт" to "cobalt", "cobalt" to "cobalt",
            "рио" to "rio", "rio" to "rio",
            "солярис" to "solaris", "solaris" to "solaris",
            "туксон" to "tucson", "тусон" to "tucson", "tucson" to "tucson",
            "крета" to "creta", "creta" to "creta",
            "элантра" to "elantra", "elantra" to "elantra",
            "к5" to "k5", "k5" to "k5",
            "соната" to "sonata", "sonata" to "sonata",
            "эскалейд" to "escalade", "escalade" to "escalade",
            "мустанг" to "mustang", "mustang" to "mustang",
            "чарджер" to "charger", "charger" to "charger",
            "прадо" to "prado", "prado" to "prado",
            "крузер" to "land cruiser", "крузак" to "land cruiser",
            "самурай" to "samurai", "самураи" to "samurai"
        )

        val matchedModel = modelMap.entries.firstOrNull { (cyr, _) -> q.contains(cyr) }?.value
        if (matchedModel != null) {
            val modelCars = cars.filter { car -> car.name.lowercase().contains(matchedModel) }
            if (modelCars.isNotEmpty()) {
                val headerText = if (lang == Lang.KZ) {
                    "🔍 «$matchedModel» моделі бойынша табылған көліктер (барлығы ${modelCars.size} авто):"
                } else {
                    "🔍 Найденные автомобили по запросу «$matchedModel» (всего ${modelCars.size} авто):"
                }
                results.add(ChatItem.BotMsg(headerText))
                modelCars.forEach { results.add(ChatItem.RecCard(it)) }
                return results
            }
        }

        // 2. Company Fleet List Matching
        val companyMatch = when {
            q.contains("cashauto moto") || q.contains("кашавто мото") -> "CashAuto Moto"
            q.contains("cashauto minivan") || q.contains("кашавто минивэн") -> "CashAuto Minivan"
            q.contains("cashauto") || q.contains("кашавто") || q.contains("кэшавто") -> "CashAuto"
            q.contains("anytime") || q.contains("энимайм") || q.contains("энитайм") -> "Anytime"
            q.contains("жеке") || q.contains("частн") || q.contains("иесі") || q.contains("p2p") -> "Жеке иесі"
            else -> null
        }

        if (companyMatch != null) {
            val companyCars = cars.filter { car ->
                if (companyMatch == "CashAuto") {
                    car.company.contains("CashAuto", ignoreCase = true)
                } else {
                    car.company.contains(companyMatch, ignoreCase = true)
                }
            }

            if (companyCars.isNotEmpty()) {
                val headerText = if (lang == Lang.KZ) {
                    "🏢 «$companyMatch» компаниясының барлық қолжетімді көліктер тізімі (барлығы ${companyCars.size} авто):"
                } else {
                    "🏢 Список всех доступных автомобилей компании «$companyMatch» (всего ${companyCars.size} авто):"
                }
                results.add(ChatItem.BotMsg(headerText))
                companyCars.take(10).forEach { results.add(ChatItem.RecCard(it)) }
                return results
            }
        }

        // 3. Budget Limit (e.g. "до 2500", "до 3000", "3000₸")
        val numberInQuery = Regex("""\d+""").find(q)?.value?.toIntOrNull()
        if (numberInQuery != null && numberInQuery in 1000..150000) {
            val budgetLimit = numberInQuery
            val budgetCars = cars.filter { it.pricePerHour <= budgetLimit }.sortedBy { it.pricePerHour }
            if (budgetCars.isNotEmpty()) {
                val headerText = if (lang == Lang.KZ) {
                    "🚗 Сағатына $budgetLimit ₸-ге дейінгі барлық көліктер (барлығы ${budgetCars.size} авто):"
                } else {
                    "🚗 Все доступные автомобили до $budgetLimit ₸ в час (всего ${budgetCars.size} авто):"
                }
                results.add(ChatItem.BotMsg(headerText))
                budgetCars.take(8).forEach { results.add(ChatItem.RecCard(it)) }
                return results
            }
        }

        // 4. Body type / Class / Fuel matching
        val bodyTypeMatch = when {
            q.contains("кроссовер") || q.contains("suv") -> "Кроссовер"
            q.contains("седан") -> "Седан"
            q.contains("хэтчбек") -> "Хэтчбек"
            q.contains("внедорожник") || q.contains("джип") -> "Внедорожник"
            q.contains("кабриолет") -> "Кабриолет"
            q.contains("пикап") -> "Пикап"
            q.contains("мотоцикл") || q.contains("мото") || q.contains("скутер") -> "Мото"
            q.contains("электр") || q.contains("электро") -> "Электр"
            q.contains("гибрид") -> "Гибрид"
            q.contains("бизнес") -> "Бизнес"
            q.contains("эконом") -> "Эконом"
            q.contains("комфорт") -> "Комфорт"
            q.contains("люкс") || q.contains("lux") || q.contains("премиум") -> "Lux"
            else -> null
        }

        if (bodyTypeMatch != null) {
            val categoryCars = cars.filter { car ->
                if (bodyTypeMatch == "Lux") car.isLux || car.tags.contains("Lux")
                else if (bodyTypeMatch == "Электр") car.fuel.contains("Электр", ignoreCase = true) || car.fuel.contains("Электро", ignoreCase = true)
                else car.tags.any { tag -> tag.contains(bodyTypeMatch, ignoreCase = true) } || car.fuel.contains(bodyTypeMatch, ignoreCase = true)
            }
            if (categoryCars.isNotEmpty()) {
                val headerText = if (lang == Lang.KZ) {
                    "⭐ «$bodyTypeMatch» санатындағы барлық көліктер (барлығы ${categoryCars.size} авто):"
                } else {
                    "⭐ Все автомобили категории «$bodyTypeMatch» (всего ${categoryCars.size} авто):"
                }
                results.add(ChatItem.BotMsg(headerText))
                categoryCars.take(8).forEach { results.add(ChatItem.RecCard(it)) }
                return results
            }
        }

        // 5. User Profile
        if (q.contains("профиль") || q.contains("менің") || q.contains("почта") || q.contains("телефон") || q.contains("аккаунт") || q.contains("аты")) {
            val name = user?.name ?: (if (lang == Lang.KZ) "Тіркелмеген пайдаланушы" else "Не авторизован")
            val phone = user?.phone ?: "-"
            val email = user?.email ?: "-"
            results.add(
                ChatItem.BotMsg(
                    if (lang == Lang.KZ) {
                        "👤 Сіздің профиль деректеріңіз:\n• Аты-жөні: $name\n• Телефон: $phone\n• Email: $email\n• Статус: Белсенді, Firestore деректер қорымен синхрондалған."
                    } else {
                        "👤 Данные вашего профиля:\n• Имя: $name\n• Телефон: $phone\n• Email: $email\n• Статус: Активен, синхронизирован с Firestore."
                    }
                )
            )
            return results
        }

        // 6. Contacts / 2GIS
        if (q.contains("2gis") || q.contains("карта") || q.contains("где") || q.contains("адрес") || q.contains("контакт") || q.contains("мекенжай") || q.contains("байланыс")) {
            results.add(
                ChatItem.BotMsg(
                    if (lang == Lang.KZ) {
                        "📍 CashAuto Rent Астана байланыс деректері:\n" +
                        "• 🛫 Аэропорт Т1 (24/7): +7 (777) 999-11-12\n" +
                        "• 🏢 Жайдарман көшесі 2/1: +7 (702) 998-11-12\n" +
                        "• 🗺 2GIS карта сілтемесі: https://2gis.kz/astana/geo/70000001054775158"
                    } else {
                        "📍 Контакты CashAuto Rent Астана:\n" +
                        "• 🛫 Аэропорт Т1 (24/7): +7 (777) 999-11-12\n" +
                        "• 🏢 ул. Жайдарман 2/1: +7 (702) 998-11-12\n" +
                        "• 🗺 Карты 2GIS: https://2gis.kz/astana/geo/70000001054775158"
                    }
                )
            )
            return results
        }

        // 7. General Fleet Request
        if (q.contains("все") || q.contains("барлық") || q.contains("список") || q.contains("автопарк")) {
            results.add(
                ChatItem.BotMsg(
                    if (lang == Lang.KZ) {
                        "🚗 Қолжетімді көліктер тізімі (барлығы 51 авто):"
                    } else {
                        "🚗 Список всех доступных автомобилей в автопарке (всего 51 авто):"
                    }
                )
            )
            cars.take(6).forEach { results.add(ChatItem.RecCard(it)) }
            return results
        }

        // 8. Clear, helpful fallback
        results.add(
            ChatItem.BotMsg(
                if (lang == Lang.KZ) {
                    "🔍 Сұранысыңызды нақтылаңызшы. Мысалы:\n• «Хюндай көліктерінің тізімі»\n• «Тойота Камри»\n• «CashAuto компаниясының автолары»\n• «3000₸ дейінгі көліктер»"
                } else {
                    "🔍 Уточните ваш запрос, например:\n• «Список машин Хюндай»\n• «Тойота Камри»\n• «Все машины компании CashAuto»\n• «Автомобили до 3000₸»"
                }
            )
        )
        return results
    }

    LaunchedEffect(pendingReply) {
        val reply = pendingReply ?: return@LaunchedEffect
        delay(600)

        // Try Gemini 1.5 Flash AI Service
        val geminiResult = GeminiAiService.generateResponse(
            userQuery = reply,
            cars = cars,
            userName = currentUser?.name,
            lang = lang
        )

        messages = messages.filter { it !is ChatItem.TypingIndicator }

        if (geminiResult != null) {
            val (botText, recommendedCars) = geminiResult
            val geminiResponses = mutableListOf<ChatItem>()
            geminiResponses.add(ChatItem.BotMsg(botText))
            recommendedCars.forEach { geminiResponses.add(ChatItem.RecCard(it)) }
            messages = messages + geminiResponses
        } else {
            // Local Smart NLU Engine fallback
            val localResponses = generateSmartBotResponses(reply, cars, currentUser, lang)
            messages = messages + localResponses
        }

        pendingReply = null
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBadge(Icons.Filled.SmartToy, bg = Amber, tint = AmberOnDark, sizeDp = 42)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (lang == Lang.KZ) "Smart AI Көмекші" else "Smart AI Помощник",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextHi,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (lang == Lang.KZ) "Онлайн 24/7 · Автопарк & Көмек" else "Онлайн 24/7 · Автопарк & Помощь",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMid
                )
            }
            IconButton(
                onClick = {
                    messages = listOf(ChatItem.BotMsg(initialGreeting))
                },
                modifier = Modifier
                    .background(Surface1, RoundedCornerShape(12.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Filled.DeleteSweep, contentDescription = "Тазалау", tint = TextMid)
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { item ->
                when (item) {
                    is ChatItem.BotMsg -> Bubble(item.text, isUser = false)
                    is ChatItem.UserMsg -> Bubble(item.text, isUser = true)
                    is ChatItem.RecCard -> RecCardRow(item.car, lang) { onCarChosen(item.car.id) }
                    ChatItem.TypingIndicator -> TypingBubble(lang)
                }
            }
        }

        // Quick Suggestion Chips
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
                        Text(chipText, style = MaterialTheme.typography.bodySmall, color = TextHi, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // Input Field
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
                placeholder = { Text(if (lang == Lang.KZ) "Көлік немесе сұрақ жазыңыз..." else "Напишите авто или вопрос...", color = TextMid) },
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
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .background(
                    if (isUser) Amber else Surface1,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .border(
                    1.dp,
                    if (isUser) Amber else BorderColor,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            val isLink = text.contains("2gis.kz")
            if (isLink) {
                Column {
                    Text(
                        text = text,
                        color = if (isUser) AmberOnDark else TextHi,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "👉 Open 2GIS Map",
                        color = Ice,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://2gis.kz/astana/geo/70000001054775158"))
                            context.startActivity(intent)
                        }
                    )
                }
            } else {
                Text(
                    text = text,
                    color = if (isUser) AmberOnDark else TextHi,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun TypingBubble(lang: Lang) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(Surface1, RoundedCornerShape(16.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    color = Ice,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = if (lang == Lang.KZ) "ЖИ жауап дайындап жатыр..." else "ИИ печатает ответ...",
                    color = TextMid,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun RecCardRow(car: Car, lang: Lang, onSelect: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface1, RoundedCornerShape(18.dp))
            .border(1.dp, Ice.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .clickable { onSelect() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (car.displayImageUrl.isNotBlank()) {
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 58.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Surface2, RoundedCornerShape(12.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(car.displayImageUrl)
                        .crossfade(true)
                        .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android)")
                        .build(),
                    contentDescription = car.name,
                    contentScale = ContentScale.Crop,
                    error = rememberVectorPainter(if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar),
                    placeholder = rememberVectorPainter(if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(car.name, style = MaterialTheme.typography.titleMedium, color = TextHi, fontWeight = FontWeight.Bold)
            Text(car.location, style = MaterialTheme.typography.bodySmall, color = TextMid)
            Spacer(Modifier.height(4.dp))
            Text(
                "${formatTenge(car.pricePerHour)} " + if (lang == Lang.KZ) "/ сағат" else "/ час",
                style = MaterialTheme.typography.labelLarge,
                color = Amber,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onSelect,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                if (lang == Lang.KZ) "Таңдау" else "Выбрать",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
