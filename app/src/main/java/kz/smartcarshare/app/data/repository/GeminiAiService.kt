package kz.smartcarshare.app.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.Lang

object GeminiAiService {

    // Free Gemini API Key from Google AI Studio (aistudio.google.com)
    // Note: If no API Key is set, the app smoothly uses the local smart matcher.
    private const val GEMINI_API_KEY = ""

    private val generativeModel by lazy {
        if (GEMINI_API_KEY.isNotBlank()) {
            GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = GEMINI_API_KEY
            )
        } else {
            null
        }
    }

    val isConfigured: Boolean
        get() = GEMINI_API_KEY.isNotBlank()

    suspend fun generateResponse(
        userQuery: String,
        cars: List<Car>,
        userName: String?,
        lang: Lang
    ): Pair<String, List<Car>>? {
        val model = generativeModel ?: return null

        val catalogSummary = cars.joinToString("\n") { car ->
            "ID:${car.id} | ${car.name} | Компания:${car.company} | Категория:${car.tags.joinToString()} | Мест:${car.seats} | Коробка:${car.transmission} | Топливо:${car.fuel} | Цена:${car.pricePerHour}₸/час"
        }

        val langPrompt = if (lang == Lang.KZ) "Отвечай на КАЗАХСКОМ языке." else "Отвечай на РУССКОМ языке."

        val systemPrompt = """
            Ты — умный ИИ-помощник сервиса Smart CarShare в Астане.
            $langPrompt
            
            Вот актуальный каталог всех доступных транспортных средств (всего ${cars.size} авто):
            $catalogSummary

            Инструкции:
            1. Понимай любые естественные вопросы пользователя (описания, опечатки, марки, цены, поездки, компанию).
            2. Отвечай вежливо, кратко и информативно.
            3. Если пользователь просит список машин конкретной марки (например, Хюндай, Тойота, Киа) или компании (CashAuto, Anytime), назови их и добавь в конец ответа строку с ID рекомендованных машин в формате: [CAR_IDS: 2, 4, 9, 17].
            4. Имя пользователя: ${userName ?: "Гость"}.
        """.trimIndent()

        return try {
            val response = model.generateContent(
                content {
                    text(systemPrompt)
                    text("Вопрос пользователя: $userQuery")
                }
            )

            val fullText = response.text ?: return null
            val carIdsMatch = Regex("""\[CAR_IDS:\s*([\d,\s]+)\]""").find(fullText)
            
            val recommendedCars = mutableListOf<Car>()
            val cleanText = if (carIdsMatch != null) {
                val idsStr = carIdsMatch.groupValues[1]
                val ids = idsStr.split(",").mapNotNull { it.trim().toIntOrNull() }
                recommendedCars.addAll(cars.filter { it.id in ids })
                fullText.replace(carIdsMatch.value, "").trim()
            } else {
                fullText.trim()
            }

            Pair(cleanText, recommendedCars)
        } catch (_: Exception) {
            null
        }
    }
}
