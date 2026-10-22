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
    val ownerKaspiNumber: String = "+7 (777) 999-11-12",
    val imageUrls: List<String> = emptyList()
) {
    private fun formatSingleUrl(url: String): String {
        if (url.isBlank()) return ""
        if (url.contains("cashauto.kz/images/venicles/")) {
            val fileName = url.substringAfter("cashauto.kz/images/venicles/")
            return "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-$fileName"
        }
        return url
    }

    val displayImageUrl: String
        get() = formatSingleUrl(imageUrl)

    val allImages: List<String>
        get() {
            if (imageUrls.isNotEmpty()) {
                return imageUrls.map { formatSingleUrl(it) }.filter { it.isNotBlank() }
            }
            val main = displayImageUrl
            return if (main.isNotBlank()) listOf(main) else emptyList()
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
    Car(51, "Toyota Land Cruiser Full", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/images/venicles/cmq4xgllg03ardkjl5s5d8scp.jpeg", listOf("CashAuto", "Внедорожник", "Lux"), "7 орын", "Автомат", "Бензин AI-95", 13000, 110000, true),
    Car(52, "Mercedes-Benz G63", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1763973756_PHOTO-2025-10-07-13-50-24.jpg", listOf("Nur Auto", "Премиум", "Lux", "Внедорожник"), "5 орын", "Автомат", "Бензин AI-98", 12000, 100000, true),
    Car(53, "Mercedes-Benz W222", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1763973821_PHOTO-2025-10-07-13-50-25%204.jpg", listOf("Nur Auto", "Премиум", "Lux", "Седан"), "4 орын", "Автомат", "Бензин AI-98", 12000, 100000, true),
    Car(54, "Lexus LX570", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761634725_PHOTO-2025-10-07-13-46-18%202.jpg", listOf("Nur Auto", "Премиум", "Lux", "Внедорожник", "SUV"), "4 орын", "Автомат", "Бензин AI-95", 10000, 90000, true),
    Car(55, "Toyota LC 200", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/whatsapp-image-2026-08-20-at-124836-6a86cdc516d19.jpeg", listOf("Nur Auto", "Бизнес", "Внедорожник", "SUV 7 орын"), "7 орын", "Автомат", "Бензин AI-95", 8000, 70000),
    Car(56, "Lexus LX570 7-seater", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761634799_PHOTO-2025-10-07-13-46-19%202.jpg", listOf("Nur Auto", "SUV", "Внедорожник", "7 орын"), "7 орын", "Автомат", "Бензин AI-95", 7000, 60000, true),
    Car(57, "Toyota LC 200 White", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761634528_PHOTO-2025-10-07-13-46-17%202.jpg", listOf("Nur Auto", "SUV", "Внедорожник", "7 орын"), "7 орын", "Автомат", "Бензин AI-95", 6000, 50000),
    Car(58, "Lexus ES250", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761634312_PHOTO-2025-10-07-13-46-16%205.jpg", listOf("Nur Auto", "Бизнес", "Седан", "Lux"), "5 орын", "Автомат", "Бензин AI-95", 5000, 45000, true),
    Car(59, "Kia K5 Nur", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761635082_PHOTO-2025-10-07-13-53-20.jpg", listOf("Nur Auto", "Комфорт", "Седан", "Бизнес"), "5 орын", "Автомат", "Бензин AI-95", 4000, 35000),
    Car(60, "Hyundai Sonata Nur", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761633963_PHOTO-2025-10-07-13-53-05%202.jpg", listOf("Nur Auto", "Комфорт", "Седан", "Бизнес"), "5 орын", "Автомат", "Бензин AI-95", 4000, 35000),
    Car(61, "Hyundai Tucson Nur", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761634209_PHOTO-2025-10-07-13-46-14%202.jpg", listOf("Nur Auto", "Комфорт", "Кроссовер", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 4000, 35000),
    Car(62, "Toyota Camry 70 Nur", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761633282_PHOTO-2025-10-07-13-46-15%203.jpg", listOf("Nur Auto", "Комфорт", "Седан", "Camry 70"), "5 орын", "Автомат", "Бензин AI-95", 3500, 30000),
    Car(63, "Hyundai Creta Nur", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761634143_PHOTO-2025-10-07-13-46-13%202.jpg", listOf("Nur Auto", "Комфорт", "Кроссовер", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 3000, 25000),
    Car(64, "Toyota Corolla Nur", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761632908_PHOTO-2025-10-07-13-46-12.jpg", listOf("Nur Auto", "Эконом", "Седан", "Қалалық"), "5 орын", "Автомат", "Бензин AI-95", 3000, 25000),
    Car(65, "Hyundai Elantra Nur", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1786700662_WhatsApp%20Image%202026-08-14%20at%2012.38.57.jpeg", listOf("Nur Auto", "Эконом", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 2500, 20000),
    Car(66, "Hyundai Accent Nur", "Nur Auto Rent", "Nur Auto Rent · Астана", "https://nurautorent.kz/images/cars/1761632791_PHOTO-2025-10-07-13-46-11.jpg", listOf("Nur Auto", "Эконом", "Седан", "Қалалық"), "5 орын", "Автомат", "Бензин AI-92", 2000, 15000),
    Car(67, "Mercedes W222", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Mercedes_W222/Mercedes_W222_1.webp", listOf("Region Rent", "Люкс", "Lux", "Седан", "Премиум"), "5 орын", "Автомат", "Бензин AI-98", 15000, 130000, true),
    Car(68, "Toyota Land Cruiser 300", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Toyota_Land_Cruiser_300/Toyota_LC_300_1.webp", listOf("Region Rent", "Джип", "Внедорожник", "SUV", "Lux"), "7 орын", "Автомат", "Бензин AI-95", 16000, 150000, true),
    Car(69, "Toyota Land Cruiser 250", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Toyota_Land_Cruiser_250/Toyota_LC_250_1_.webp", listOf("Region Rent", "Джип", "Внедорожник", "SUV"), "7 орын", "Автомат", "Бензин AI-95", 8500, 75000, true),
    Car(70, "Toyota Land Cruiser 200R", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Toyota_Land_Cruiser_200R/Toyota_LC_200R_1.webp", listOf("Region Rent", "Джип", "Внедорожник", "SUV"), "7 орын", "Автомат", "Бензин AI-95", 8500, 75000),
    Car(71, "Toyota Land Cruiser 200", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Toyota_Land_Cruiser_200/Toyota_LC_200_1.webp", listOf("Region Rent", "Джип", "Внедорожник", "SUV"), "7 орын", "Автомат", "Бензин AI-95", 7000, 60000),
    Car(72, "Toyota Land Cruiser Prado R", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Toyota_Land_Cruiser_Prado_R/Toyota_LC_Prado_R_1.webp", listOf("Region Rent", "Джип", "Внедорожник", "Prado"), "7 орын", "Автомат", "Бензин AI-95", 6500, 55000),
    Car(73, "Lixiang L7", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Lixiang_L7/Lixiang_L7_1.webp", listOf("Region Rent", "Кроссовер", "Гибрид", "Lux"), "5 орын", "Автомат", "Гибрид", 7500, 65000, true),
    Car(74, "Lixiang L6", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Lixian_L6/Lixiang_L6_1v2.webp", listOf("Region Rent", "Кроссовер", "Гибрид", "Lux"), "5 орын", "Автомат", "Гибрид", 7000, 60000, true),
    Car(75, "BYD Song Plus", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/BYD_Song_Plus/BYD_Song_Plus_1v2.webp", listOf("Region Rent", "Кроссовер", "Гибрид", "Комфорт"), "5 орын", "Автомат", "Гибрид", 3500, 30000),
    Car(76, "Hyundai Tucson Region", "Region Rent", "Region Rent · Астана", "https://region.rent/assets/images/cars/Hyundai_Tucson/Tucson_1v2.webp", listOf("Region Rent", "Кроссовер", "Комфорт", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 3500, 30000),
    Car(77, "BYD Seal 05", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F461972853e6af0d0ea1e4e21.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Гибрид"), "5 орын", "Автомат", "Бензин AI-95", 3000, 25000),
    Car(78, "BYD Seal 05 White", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F3ccdb5985af411b6781a341a.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Гибрид"), "5 орын", "Автомат", "Бензин AI-95", 3000, 25000),
    Car(79, "Changan CS55 Plus", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2Fe1eb1d80ab21ddc69e0c27ae.jpg&w=640&q=75", listOf("QazQar", "Кроссовер", "Комфорт", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 3500, 30000),
    Car(80, "EXEED LX", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2Fc6c3ce44035097d5cb73354c.jpg&w=640&q=75", listOf("QazQar", "Кроссовер", "Комфорт", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 3400, 29000),
    Car(81, "Hyundai Elantra QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2Fdd3b22893ddeed215bce93af.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 3000, 25000),
    Car(82, "Hyundai Accent QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F0d18ac4bdbd47adc73c5fc8d.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Қалалық"), "5 орын", "Автомат", "Бензин AI-92", 2500, 20000),
    Car(83, "Hyundai Elantra White QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2Fedd4ed5e3a06786344e899bf.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 3000, 25000),
    Car(84, "Hyundai Sonata QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F1790760194702-bgqdk056pti.jpg&w=640&q=75", listOf("QazQar", "Комфорт", "Седан", "Бизнес"), "5 орын", "Автомат", "Бензин AI-95", 3500, 30000),
    Car(85, "Hyundai Elantra Classic QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F1790760915490-85nc1kiy74d.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 2700, 22000),
    Car(86, "Hyundai Sonata Luxe QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F1790940668220-uruntixrk8.jpg&w=640&q=75", listOf("QazQar", "Комфорт", "Седан", "Бизнес"), "5 орын", "Автомат", "Бензин AI-95", 3800, 33000),
    Car(87, "Hyundai Tucson QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2Fe7762542182082d7e78c03e9.jpg&w=640&q=75", listOf("QazQar", "Кроссовер", "Комфорт", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 4000, 35000),
    Car(88, "Hyundai Sonata White QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2Ffdcf3134bbbb054441a37bde.jpg&w=640&q=75", listOf("QazQar", "Комфорт", "Седан", "Бизнес"), "5 орын", "Автомат", "Бензин AI-95", 3800, 33000),
    Car(89, "Hyundai Tucson Full QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2Fddb237b4081093436b9cba0d.jpg&w=640&q=75", listOf("QazQar", "Кроссовер", "Комфорт", "SUV"), "5 орын", "Автомат", "Бензин AI-95", 4200, 37000),
    Car(90, "Hyundai Sonata Premium QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2Fff31d94fd541417b05e11131.jpg&w=640&q=75", listOf("QazQar", "Комфорт", "Седан", "Бизнес"), "5 орын", "Автомат", "Бензин AI-95", 3800, 33000),
    Car(91, "Hyundai Accent Silver QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F6b5bc992378b266165e5c261.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Қалалық"), "5 орын", "Автомат", "Бензин AI-92", 2200, 17000),
    Car(92, "Hyundai Elantra Red QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F1d4e0ef7d0aa07b062c99971.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 2700, 22000)
)

fun formatTenge(amount: Int): String {
    val s = amount.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$s ₸"
}
