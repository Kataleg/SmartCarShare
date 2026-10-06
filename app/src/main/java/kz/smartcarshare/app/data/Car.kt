package kz.smartcarshare.app.data

data class Car(
    val id: Int,
    val name: String,
    val company: String = "Anytime",
    val location: String = "Астана · EXPO",
    val imageUrl: String = "",
    val tags: List<String>,
    val seats: String,
    val transmission: String,
    val fuel: String,
    val pricePerHour: Int,
    val pricePerDay: Int?,
    val isLux: Boolean = false,
    val ownerEmail: String = "",
    val ownerName: String = "",
    val ownerKaspiNumber: String = "+7 (777) 999-11-12"
)

val sampleCars = listOf(
    Car(
        id = 1,
        name = "Toyota Camry 80 Luxe (2025)",
        company = "CashAuto",
        location = "CashAuto · Аэропорт Т1 (24/7)",
        imageUrl = "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=800",
        tags = listOf("CashAuto", "Camry 80", "Luxe"),
        seats = "5 орын",
        transmission = "Автомат 8AT",
        fuel = "Бензин AI-95",
        pricePerHour = 2500,
        pricePerDay = 32000,
        isLux = true,
        ownerKaspiNumber = "+7 (777) 999-11-12"
    ),
    Car(
        id = 2,
        name = "Dodge Charger Full (2023)",
        company = "CashAuto",
        location = "CashAuto · Жайдарман 2/1",
        imageUrl = "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=800",
        tags = listOf("CashAuto", "Muscle", "Sport"),
        seats = "5 орын",
        transmission = "Автомат 8AT",
        fuel = "Бензин AI-98",
        pricePerHour = 1900,
        pricePerDay = 25000,
        isLux = true,
        ownerKaspiNumber = "+7 (702) 998-11-12"
    ),
    Car(
        id = 3,
        name = "Cadillac Escalade Luxe (2020)",
        company = "CashAuto",
        location = "CashAuto · Жайдарман 2/1",
        imageUrl = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?q=80&w=800",
        tags = listOf("CashAuto", "SUV 7 seats", "Lux"),
        seats = "7 орын",
        transmission = "Автомат 10AT",
        fuel = "Бензин AI-95",
        pricePerHour = 4800,
        pricePerDay = 66000,
        isLux = true,
        ownerKaspiNumber = "+7 (702) 998-11-12"
    ),
    Car(
        id = 4,
        name = "Chery Tiggo 7 Pro",
        company = "Anytime",
        location = "Астана · EXPO / Mega Silk Way",
        imageUrl = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?q=80&w=800",
        tags = listOf("Anytime", "Кроссовер", "Комфорт"),
        seats = "5 орын",
        transmission = "Вариатор",
        fuel = "Бензин AI-95",
        pricePerHour = 1350,
        pricePerDay = 17500,
        ownerKaspiNumber = "+7 (701) 555-01-01"
    ),
    Car(
        id = 5,
        name = "Hyundai Accent",
        company = "Anytime",
        location = "Астана · Бәйтерек / Достық көшесі",
        imageUrl = "https://images.unsplash.com/photo-1590362891991-f776e747a588?q=80&w=800",
        tags = listOf("Anytime", "Эконом", "Қалалық"),
        seats = "5 орын",
        transmission = "Автомат",
        fuel = "Бензин AI-92",
        pricePerHour = 850,
        pricePerDay = 11000,
        ownerKaspiNumber = "+7 (701) 555-01-01"
    ),
    Car(
        id = 6,
        name = "Haval Jolion 4WD",
        company = "Anytime",
        location = "Астана · Аэропорт Н.Назарбаев",
        imageUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        tags = listOf("Anytime", "4WD", "Кроссовер"),
        seats = "5 орын",
        transmission = "Робот 7DCT",
        fuel = "Бензин AI-95",
        pricePerHour = 1300,
        pricePerDay = 17000,
        ownerKaspiNumber = "+7 (701) 555-01-01"
    ),
    Car(
        id = 7,
        name = "Geely Coolray",
        company = "Anytime",
        location = "Астана · Нұрлы Жол вокзалы",
        imageUrl = "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?q=80&w=800",
        tags = listOf("Anytime", "Турбо", "Комфорт"),
        seats = "5 орын",
        transmission = "Робот 7DCT",
        fuel = "Бензин AI-95",
        pricePerHour = 1400,
        pricePerDay = 18500,
        ownerKaspiNumber = "+7 (701) 555-01-01"
    ),
    Car(
        id = 8,
        name = "Chevrolet Cobalt (2024)",
        company = "CashAuto",
        location = "CashAuto · Аэропорт Т1 (24/7)",
        imageUrl = "https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?q=80&w=800",
        tags = listOf("CashAuto", "Эконом", "Қалалық"),
        seats = "5 орын",
        transmission = "Автомат",
        fuel = "Бензин AI-92",
        pricePerHour = 800,
        pricePerDay = 10000,
        ownerKaspiNumber = "+7 (777) 999-11-12"
    ),
    Car(
        id = 9,
        name = "Zeekr 001 AWD",
        company = "SmartCar Electric",
        location = "Астана · Назарбаев Университеті",
        imageUrl = "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=800",
        tags = listOf("Electric", "0-100 3.8с", "Lux"),
        seats = "5 орын",
        transmission = "Редуктор (EV)",
        fuel = "Электр (540 км)",
        pricePerHour = 3400,
        pricePerDay = 45000,
        isLux = true,
        ownerKaspiNumber = "+7 (705) 777-88-99"
    )
)

fun formatTenge(amount: Int): String {
    val s = amount.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$s ₸"
}
