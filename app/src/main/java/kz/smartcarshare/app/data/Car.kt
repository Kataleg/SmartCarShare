package kz.smartcarshare.app.data

data class Car(
    val id: Int,
    val name: String,
    val tags: List<String>,
    val seats: String,
    val transmission: String,
    val fuel: String,
    val pricePerHour: Int,
    val pricePerDay: Int?,
    val isLux: Boolean = false
)

val sampleCars = listOf(
    Car(1, "Hyundai Tucson", listOf("Отбасылық", "5 орын"), "5 орын", "Автомат", "Бензин", 1200, 14500),
    Car(2, "Kia Rio", listOf("Үнемді", "Қала үшін"), "5 орын", "Автомат", "Бензин", 700, 9000),
    Car(3, "Toyota Camry", listOf("Бизнес", "Комфорт"), "5 орын", "Автомат", "Бензин", 1800, 21000),
    Car(4, "Mercedes S-Class", listOf("Lux", "Жүргізушімен"), "4 орын", "Автомат", "Гибрид", 6000, null, true)
)

fun formatTenge(amount: Int): String {
    val s = amount.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$s ₸"
}