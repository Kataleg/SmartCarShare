package kz.smartcarshare.app.data

data class Car(
    val id: Int,
    val name: String,
    val company: String = "CashAuto",
    val location: String = "CashAuto · Астана (24/7)",
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
) {
    val displayImageUrl: String
        get() {
            if (imageUrl.isBlank()) return ""
            if (imageUrl.contains("cashauto.kz/images/venicles/")) {
                val fileName = imageUrl.substringAfter("cashauto.kz/images/venicles/")
                return "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-$fileName"
            }
            return imageUrl
        }

    fun displaySeats(lang: Lang): String {
        if (lang == Lang.KZ) return seats
        return seats.replace("орын", "мест")
    }

    fun displayTransmission(lang: Lang): String {
        if (lang == Lang.KZ) return transmission
        return when (transmission) {
            "Редуктор" -> "Редуктор"
            "Вариатор" -> "Вариатор"
            "Автомат" -> "Автомат"
            "Механика" -> "Механика"
            "Робот" -> "Робот"
            else -> transmission
        }
    }

    fun displayFuel(lang: Lang): String {
        if (lang == Lang.KZ) return fuel
        return when (fuel) {
            "Электр" -> "Электро"
            "Бензин AI-92" -> "Бензин АИ-92"
            "Бензин AI-95" -> "Бензин АИ-95"
            "Бензин AI-98" -> "Бензин АИ-98"
            "Дизель" -> "Дизель"
            "Гибрид" -> "Гибрид"
            else -> fuel
        }
    }
}

val sampleCars = listOf(
    Car(1, "Vilimi Samurai", "CashAuto Moto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmng3kbq501rhocjl029p8ldx.jpeg", listOf("CashAuto", "Мото", "Электро"), "2 орын", "Редуктор", "Электр", 1700, 6500),
    Car(2, "Chevrolet Cobalt", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfpmmwl0unuqwjl9bol81fm.jpeg", listOf("CashAuto", "Эконом", "Седан"), "5 орын", "Автомат", "Бензин AI-92", 2000, 16000),
    Car(3, "Peda Hornet (B)", "CashAuto Moto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmqutwr8blpznpdjl0dfb0mvb.jpg", listOf("CashAuto", "Мото", "Скутер"), "2 орын", "Вариатор", "Бензин AI-92", 4500, 20000),
    Car(4, "Kia Rio", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgth68e0vfdqwjlf9la3f3c.jpeg", listOf("CashAuto", "Эконом", "Қалалық"), "5 орын", "Автомат", "Бензин AI-92", 2300, 18000),
    Car(5, "Hyundai Solaris I", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgtbt8p0vf8qwjl2rryeqpk.jpeg", listOf("CashAuto", "Комфорт", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 2500, 19000),
    Car(6, "Hyundai i20", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgtfgst0vfbqwjl27a2bknl.jpeg", listOf("CashAuto", "Эконом", "Хэтчбек"), "5 орын", "Автомат", "Бензин AI-92", 2300, 19000),
    Car(7, "Renault Duster 4WD", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfpw7930umxpojl5r14hr14.jpeg", listOf("CashAuto", "4WD", "Кроссовер"), "5 орын", "Механика", "Бензин AI-95", 2500, 22000),
    Car(8, "Hyundai Bayon", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cm8js8fei07wu28jlbrmzhs1v.jpg", listOf("CashAuto", "Кроссовер", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 2500, 22000),
    Car(9, "Hyundai Creta", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt77hwct0mk2pojl63wtau00.jpeg", listOf("CashAuto", "Кроссовер", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 2800, 24000),
    Car(10, "Hyundai i30", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmbugau1a00tcu7jl9mm8e961.jpg", listOf("CashAuto", "Комфорт", "Хэтчбек"), "5 орын", "Автомат", "Бензин AI-95", 2800, 24000),
    Car(11, "Kia Cerato", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtguujs50vfxqwjl8sw6265z.jpeg", listOf("CashAuto", "Комфорт+", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 3000, 24000),
    Car(12, "Dongfeng Minivan", "CashAuto Minivan", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmnafwy2z02ueh0jl69jl7b7e.jpeg", listOf("CashAuto", "Минивэн", "8 орын"), "8 орын", "Автомат", "Бензин AI-92", 3000, 22000),
    Car(13, "Ducati Panigale Electro", "CashAuto Moto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmpz7pwau00mbdkjlaiy1g2wk.jpeg", listOf("CashAuto", "Мото", "Lux"), "2 орын", "Редуктор", "Электр", 5000, 25000, true),
    Car(14, "Hyundai Elantra Full", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquui7zclsddpdjl74iog3gh.jpg", listOf("CashAuto", "Бизнес", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 3300, 25000),
    Car(15, "Toyota Camry I", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13na5f01lucfjlbrwk0uqa.jpeg", listOf("CashAuto", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 3000, 25000),
    Car(16, "Toyota Camry Luxe", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquyh46um8g7o8jl9mek4ift.jpg", listOf("CashAuto", "Седан", "Lux"), "5 орын", "Автомат", "Бензин AI-95", 3300, 28000, true),
    Car(17, "Hyundai Creta (2023)", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfq2tsk0un3pojl15w16a4e.jpeg", listOf("CashAuto", "Кроссовер", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 3500, 28000),
    Car(18, "Toyota Camry I (2016)", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmsh18hcb070mpojl446b8szu.jpeg", listOf("CashAuto", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 3200, 27000),
    Car(19, "Changan CS35 MAX Full", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt5o98xz0lfpqwjl6xgk1d5v.jpeg", listOf("CashAuto", "Кроссовер", "Новинка"), "5 орын", "Автомат", "Бензин AI-95", 3300, 28000),
    Car(20, "Kia Seltos Full", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cm8d023qd06k628jl2el326k3.jpeg", listOf("CashAuto", "Кроссовер", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 3500, 30000),
    Car(21, "Changan CS55 Plus", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmptwu1w900575xjl9xf20vfj.jpeg", listOf("CashAuto", "Кроссовер", "Турбо"), "5 орын", "Робот", "Бензин AI-95", 3700, 30000),
    Car(22, "Kia K5", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmiaab5oq00c1hgjlexaz2w48.jpg", listOf("CashAuto", "Бизнес", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 4000, 30000),
    Car(23, "CFMOTO CF 450CL", "CashAuto Moto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmo2tps1j000p5hjl1veohajs.jpeg", listOf("CashAuto", "Мото", "Sport"), "2 орын", "Механика", "Бензин AI-95", 6000, 30000, true),
    Car(24, "Changan CS75 Full", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmu10nc7k06zklxjldryrf3xo.jpeg", listOf("CashAuto", "Кроссовер", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 3500, 30000),
    Car(25, "Hyundai Tucson Full 4WD", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cms8islx401zkpojlhdaa5pcy.jpeg", listOf("CashAuto", "4WD", "Кроссовер"), "5 орын", "Автомат", "Бензин AI-95", 4200, 32000),
    Car(26, "Toyota RAV4 Full 4WD", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13jv4t01mjdkjlew7a7hub.jpeg", listOf("CashAuto", "4WD", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 3500, 32000),
    Car(27, "Hyundai Sonata Full I", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt897s5f0n1oqwjladfnf0gu.jpeg", listOf("CashAuto", "Бизнес", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 3800, 32000),
    Car(28, "Toyota Camry Full", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmiagfrd000dhhgjle7oyeazw.jpg", listOf("CashAuto", "Бизнес", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 4000, 33000),
    Car(29, "Kia K5 Full", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmc2cvhz600c1s1jlfkwbd90d.jpg", listOf("CashAuto", "Бизнес", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 4000, 33000),
    Car(30, "Hyundai Tucson", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtlfz1fi10npqwjl32er6vh7.jpeg", listOf("CashAuto", "Кроссовер", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 4500, 36000),
    Car(31, "Kia Sportage Full", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt8c4az60n2xqwjl7a1r8j33.jpeg", listOf("CashAuto", "Кроссовер", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 4500, 36000),
    Car(32, "Hyundai Sonata Full", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cms7aycg001f3qwjl3wwu4nym.jpeg", listOf("CashAuto", "Бизнес", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 4500, 36000),
    Car(33, "Hyundai SantaFe Full 4WD", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmdmc2pwd01a2bkjlejsm4pmt.jpeg", listOf("CashAuto", "4WD", "SUV 7 орын"), "7 орын", "Автомат", "Бензин AI-95", 4800, 40000),
    Car(34, "Dodge Charger Full", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmo2sau3k0acsn8jlhd8t87xu.jpg", listOf("CashAuto", "Muscle", "Sport"), "5 орын", "Автомат", "Бензин AI-98", 5000, 40000, true),
    Car(35, "Ford Mustang Full", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmh07qrxc06zkgfjl2aojf5yk.jpg", listOf("CashAuto", "Sport", "Lux"), "4 орын", "Автомат", "Бензин AI-98", 6000, 40000, true),
    Car(36, "Changan UNI-K Full 4WD", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmsoioz9g0baypojlhqgsex7v.jpeg", listOf("CashAuto", "4WD", "Премиум"), "5 орын", "Автомат", "Бензин AI-95", 4500, 40000),
    Car(37, "Kia Sorento Full 4WD", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquyh46xm8g8o8jlcy4ze1jg.jpeg", listOf("CashAuto", "4WD", "SUV 7 орын"), "7 орын", "Автомат", "Бензин AI-95", 5500, 43000),
    Car(38, "Toyota LC Prado Full", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmrefahj571t760jl7urdh4au.jpeg", listOf("CashAuto", "Внедорожник", "Lux"), "7 орын", "Автомат", "Бензин AI-95", 5500, 45000, true),
    Car(39, "Toyota Sienna Full Minivan", "CashAuto Minivan", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13ltny01mqdkjl9e5u2ibw.jpeg", listOf("CashAuto", "Минивэн", "8 орын"), "8 орын", "Автомат", "Бензин AI-95", 5500, 45000),
    Car(40, "BMW 428i Cabriolet", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmettvuss00evsujl0zwzfjrk.jpeg", listOf("CashAuto", "Кабриолет", "Lux"), "4 орын", "Автомат", "Бензин AI-98", 9500, 45000, true),
    Car(41, "Toyota Camry 80 Luxe", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfqj7s70undpojl42c1e78b.jpg", listOf("CashAuto", "Camry 80", "Lux"), "5 орын", "Автомат", "Бензин AI-95", 6000, 45000, true),
    Car(42, "Changan Hunter Plus 4WD", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmsg61ldq06hppojld89ahshl.jpeg", listOf("CashAuto", "Пикап", "4WD"), "5 орын", "Автомат", "Дизель", 5500, 45000),
    Car(43, "Toyota Highlander I", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtl66h2y10htpojl3vx29hyq.jpeg", listOf("CashAuto", "SUV 7 орын", "Lux"), "7 орын", "Автомат", "Бензин AI-95", 5000, 45000),
    Car(44, "Toyota Camry Hybrid I", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfqj7s70undpojl42c1e78b.jpg", listOf("CashAuto", "Гибрид", "Lux"), "5 орын", "Автомат", "Гибрид", 6500, 48000, true),
    Car(45, "Lexus LX 570", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmptwjc9x004b5xjlc3lc5g1u.jpeg", listOf("CashAuto", "Премиум", "SUV"), "7 орын", "Автомат", "Бензин AI-95", 7000, 50000, true),
    Car(46, "Toyota Land Cruiser Restyling", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmrewnwhj17xyeojl7w7lh7w5.jpeg", listOf("CashAuto", "Внедорожник", "Lux"), "7 орын", "Автомат", "Бензин AI-95", 8000, 52000, true),
    Car(47, "Hyundai Santa Fe Calligraphy", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmdmbss58019acpjlc7hqa8l6.jpeg", listOf("CashAuto", "Премиум", "6 орын"), "6 орын", "Автомат", "Бензин AI-95", 8000, 55000, true),
    Car(48, "Hyundai Staria Minivan Full", "CashAuto Minivan", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmdft8gvc01in9tjl1528hdkc.jpeg", listOf("CashAuto", "Минивэн", "11 орын"), "11 орын", "Автомат", "Дизель", 8500, 58000, true),
    Car(49, "Toyota Land Cruiser Luxe", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmptwmald004k74jl6vfwf3f5.jpeg", listOf("CashAuto", "Внедорожник", "Lux"), "5 орын", "Автомат", "Бензин AI-95", 9000, 75000, true),
    Car(50, "Cadillac Escalade Luxe", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/images/venicles/cmo2scoej0abuocjlc68j21gy.jpg", listOf("CashAuto", "SUV 7 орын", "Lux"), "7 орын", "Автомат", "Бензин AI-95", 12000, 99000, true),
    Car(51, "Toyota Land Cruiser Full", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/images/venicles/cmq4xgllg03ardkjl5s5d8scp.jpeg", listOf("CashAuto", "Внедорожник", "Lux"), "7 орын", "Автомат", "Бензин AI-95", 13000, 110000, true)
)

fun formatTenge(amount: Int): String {
    val s = amount.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$s ₸"
}
