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
    val imageUrls: List<String> = emptyList(),
    val year: Int = 2023,
    val engineCapacity: String = "",
    val powerHp: String = "",
    val driveType: String = "",
    val fuelConsumption: String = "",
    val bodyType: String = "",
    val color: String = "",
    val depositTenge: Int = 0
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

    val displayYear: Int
        get() {
            if (year != 2023) return year
            val yearFromTags = tags.firstOrNull { it.matches(Regex("20[012]\\d")) }?.toIntOrNull()
            if (yearFromTags != null) return yearFromTags
            val yearFromName = Regex("20[012]\\d").find(name)?.value?.toIntOrNull()
            return yearFromName ?: 2023
        }

    val displayEngineCapacity: String
        get() {
            if (engineCapacity.isNotBlank()) return engineCapacity
            val lower = name.lowercase()
            return when {
                lower.contains("g63") || lower.contains("g550") -> "4.0 л Biturbo"
                lower.contains("maybach") || lower.contains("s580") -> "4.0 л V8"
                lower.contains("e63") || lower.contains("rs6") || lower.contains("rs7") || lower.contains("rsq8") -> "4.0 л V8"
                lower.contains("panamera") || lower.contains("cayenne") -> "3.0 л V6"
                lower.contains("lx 600") || lower.contains("lc 300") -> "3.5 л Twin-Turbo"
                lower.contains("escalade") || lower.contains("corvette") -> "6.2 л V8"
                lower.contains("hellcat") -> "6.2 л V8 Supercharged"
                lower.contains("scat pack") -> "6.4 л V8"
                lower.contains("mustang") || lower.contains("camaro") -> "2.3 л Турбо"
                lower.contains("lc 200") || lower.contains("patrol") -> "5.6 л V8"
                lower.contains("prado 4.0") -> "4.0 л V6"
                lower.contains("prado 2.7") || lower.contains("fortuner") || lower.contains("lc prado") -> "2.7 л"
                lower.contains("camry 80") || lower.contains("camry 75") || lower.contains("camry 70") || lower.contains("tucson 2.5") -> "2.5 л"
                lower.contains("m5") || lower.contains("m4") || lower.contains("x5m") || lower.contains("x6m") || lower.contains("x7") -> "4.4 л V8"
                lower.contains("zeekr") || lower.contains("taycan") || lower.contains("byd han") || lower.contains("id.4") || lower.contains("e:ns1") -> "Электропривод"
                lower.contains("l9") || lower.contains("l8") || lower.contains("l7") || lower.contains("l6") || lower.contains("song plus") -> "1.5 л Турбо-Гибрид"
                lower.contains("elantra") || lower.contains("creta") || lower.contains("sonata 2.0") -> "2.0 л"
                lower.contains("accent") || lower.contains("solaris") || lower.contains("rio") || lower.contains("cobalt") || lower.contains("polo") || lower.contains("rapid") -> "1.6 л"
                lower.contains("bayon") || lower.contains("baleno") -> "1.4 л"
                lower.contains("atlas") || lower.contains("coolray") || lower.contains("omoda") || lower.contains("mufasa") -> "1.5 л Турбо"
                lower.contains("leopard") -> "Электро-Гибрид 1.5T"
                else -> "2.0 л"
            }
        }

    val displayPowerHp: String
        get() {
            if (powerHp.isNotBlank()) return powerHp
            val lower = name.lowercase()
            return when {
                lower.contains("hellcat") -> "717 л.с."
                lower.contains("corvette") -> "495 л.с."
                lower.contains("g63") -> "585 л.с."
                lower.contains("taycan") || lower.contains("zeekr 001 fr") -> "761 л.с."
                lower.contains("maybach") || lower.contains("s580") -> "503 л.с."
                lower.contains("m5") || lower.contains("x5m") || lower.contains("x6m") -> "625 л.с."
                lower.contains("m4") -> "510 л.с."
                lower.contains("e63") || lower.contains("rs6") || lower.contains("rs7") || lower.contains("rsq8") -> "600 л.с."
                lower.contains("l9") || lower.contains("l8") || lower.contains("l7") -> "449 л.с."
                lower.contains("zeekr 001") || lower.contains("zeekr 009") || lower.contains("yangwang") -> "544 л.с."
                lower.contains("lx 600") || lower.contains("lc 300") -> "415 л.с."
                lower.contains("escalade") -> "426 л.с."
                lower.contains("camaro") || lower.contains("mustang") -> "310 л.с."
                lower.contains("camry 80") -> "206 л.с."
                lower.contains("camry 75") || lower.contains("camry 70") -> "181 л.с."
                lower.contains("lc 200") || lower.contains("patrol") -> "381 л.с."
                lower.contains("prado 4.0") -> "249 л.с."
                lower.contains("prado 2.7") || lower.contains("fortuner") -> "166 л.с."
                lower.contains("tucson") || lower.contains("sportage") || lower.contains("sonata") || lower.contains("k5") -> "180 л.с."
                lower.contains("creta") || lower.contains("mufasa") || lower.contains("bayon") -> "150 л.с."
                lower.contains("elantra") -> "128 л.с."
                lower.contains("accent") || lower.contains("solaris") || lower.contains("rio") || lower.contains("cobalt") || lower.contains("polo") -> "123 л.с."
                else -> "150 л.с."
            }
        }

    val displayDriveType: String
        get() {
            if (driveType.isNotBlank()) return driveType
            val lower = name.lowercase()
            return when {
                tags.contains("4WD") || lower.contains("4wd") || lower.contains("xdrive") || lower.contains("quattro") ||
                lower.contains("g63") || lower.contains("g550") || lower.contains("lc") || lower.contains("prado") || lower.contains("patrol") ||
                lower.contains("l9") || lower.contains("l8") || lower.contains("l7") || lower.contains("zeekr 001") ||
                lower.contains("gls") || lower.contains("gle") || lower.contains("x5m") || lower.contains("x6m") || lower.contains("x7") ||
                lower.contains("pajero") || lower.contains("fortuner") || lower.contains("pathfinder") || lower.contains("escalade") -> "Полный (4WD / AWD)"
                lower.contains("mustang") || lower.contains("camaro") || lower.contains("corvette") || lower.contains("m5") || lower.contains("m4") -> "Задний (RWD)"
                else -> "Передний (FWD)"
            }
        }

    val displayFuelConsumption: String
        get() {
            if (fuelConsumption.isNotBlank()) return fuelConsumption
            val lower = name.lowercase()
            return when {
                lower.contains("zeekr") || lower.contains("taycan") || lower.contains("byd han") || lower.contains("id.4") || lower.contains("e:ns1") -> "18.5 кВтч / 100 км"
                lower.contains("l9") || lower.contains("l8") || lower.contains("l7") || lower.contains("l6") || lower.contains("song plus") -> "7.8 л / 100 км (Гибрид)"
                lower.contains("g63") || lower.contains("escalade") || lower.contains("hellcat") || lower.contains("lc 200") -> "14.5 л / 100 км"
                lower.contains("lc 300") || lower.contains("prado") || lower.contains("lx 600") || lower.contains("m5") || lower.contains("x7") -> "11.8 л / 100 км"
                lower.contains("camry") || lower.contains("sonata") || lower.contains("k5") || lower.contains("tucson") || lower.contains("sportage") -> "7.8 л / 100 км"
                lower.contains("accent") || lower.contains("solaris") || lower.contains("rio") || lower.contains("cobalt") || lower.contains("polo") || lower.contains("rapid") -> "6.5 л / 100 км"
                else -> "8.2 л / 100 км"
            }
        }

    val displayBodyType: String
        get() {
            if (bodyType.isNotBlank()) return bodyType
            val lower = name.lowercase()
            return when {
                tags.contains("Внедорожник") || lower.contains("g63") || lower.contains("prado") || lower.contains("lc") || lower.contains("patrol") || lower.contains("fortuner") || lower.contains("escalade") || lower.contains("pajero") -> "Внедорожник"
                tags.contains("Кроссовер") || lower.contains("tucson") || lower.contains("creta") || lower.contains("rav4") || lower.contains("sportage") || lower.contains("mufasa") || lower.contains("coolray") || lower.contains("atlas") || lower.contains("omoda") || lower.contains("l9") || lower.contains("l8") || lower.contains("l7") || lower.contains("zeekr 001") -> "Кроссовер"
                tags.contains("Минивэн") || lower.contains("staria") || lower.contains("custin") || lower.contains("carnival") || lower.contains("zeekr 009") -> "Минивэн"
                tags.contains("Купе") || lower.contains("m4") || lower.contains("challenger") -> "Купе"
                tags.contains("Кабриолет") || lower.contains("convertible") || lower.contains("corvette") -> "Кабриолет"
                tags.contains("Хэтчбек") || lower.contains("baleno") -> "Хэтчбек"
                else -> "Седан"
            }
        }

    val displayColor: String
        get() {
            if (color.isNotBlank()) return color
            val lower = name.lowercase()
            return when {
                lower.contains("white") || lower.contains("белый") -> "Белый перламутр"
                lower.contains("red") || lower.contains("красный") -> "Красный металлик"
                lower.contains("silver") || lower.contains("серебристый") -> "Серебристый"
                lower.contains("blue") || lower.contains("синий") -> "Синий глубокий"
                else -> "Черный металлик"
            }
        }

    val displayDeposit: String
        get() {
            if (depositTenge > 0) return "$depositTenge ₸"
            return if (isLux) "50 000 ₸" else "0 ₸ (Без залога)"
        }
}

val sampleCars = listOf(
    Car(1, "Vilimi Samurai", "CashAuto Moto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmng3kbq501rhocjl029p8ldx.jpeg", listOf("CashAuto", "Мото", "Электро"), "2 орын", "Редуктор", "Электр", 1700, 6500),
    Car(
        2,
        "Chevrolet Cobalt",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfpmmwl0unuqwjl9bol81fm.jpeg",
        listOf("CashAuto", "Эконом", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2000,
        16000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfpmmwl0unuqwjl9bol81fm.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmi7d4mrl01kbokjl9xv87k02.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmi7d4mrj01kaokjl9f9s6a8y.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmi7ccm7401kenfjlbd176ga8.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmi7ccm7501kfnfjlehkt9ive.jpg"
        )
    ),
    Car(3, "Peda Hornet (B)", "CashAuto Moto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmqutwr8blpznpdjl0dfb0mvb.jpg", listOf("CashAuto", "Мото", "Скутер"), "2 орын", "Вариатор", "Бензин AI-92", 4500, 20000),
    Car(
        4,
        "Kia Rio",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgth68e0vfdqwjlf9la3f3c.jpeg",
        listOf("CashAuto", "Эконом", "Қалалық"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2300,
        18000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgth68e0vfdqwjlf9la3f3c.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmi7dkwl001kookjl9jea5mh5.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmi7dkwkx01kmokjle40l0fzy.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmi7dkwkz01knokjl8un1fbbv.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmi7dkwl101kpokjl7m8h7utt.jpeg"
        )
    ),
    Car(
        5,
        "Hyundai Solaris I",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgtbt8p0vf8qwjl2rryeqpk.jpeg",
        listOf("CashAuto", "Комфорт", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        2500,
        19000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgtbt8p0vf8qwjl2rryeqpk.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cl5umqaab009fp2jlgv787av4.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cl5umqsgc009oomjlc689evjz.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cl5umqaa7009ep2jl9ie55sv5.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cl5umrfim009ip2jlgmckddid.jpg"
        )
    ),
    Car(
        6,
        "Hyundai i20",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgtfgst0vfbqwjl27a2bknl.jpeg",
        listOf("CashAuto", "Эконом", "Хэтчбек"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2300,
        19000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtgtfgst0vfbqwjl27a2bknl.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clyygde8n00c03zjlb8gb8m6z.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clyygde8m00bz3zjl8dj5a298.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clyygde8l00by3zjlh3l3bs4u.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clyygde8k00bx3zjlauca8zn1.jpg"
        )
    ),
    Car(
        7,
        "Renault Duster 4WD",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfpw7930umxpojl5r14hr14.jpeg",
        listOf("CashAuto", "4WD", "Кроссовер"),
        "5 орын",
        "Механика",
        "Бензин AI-95",
        2500,
        22000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfpw7930umxpojl5r14hr14.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clewqawp300ecffjl4bhbe6uj.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clewqb0rg00ejdzjlgkwi9bwq.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clewqb0rd00eidzjl2k76gj73.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clewqawow00ebffjlelpm3c0u.jpg"
        )
    ),
    Car(
        8,
        "Hyundai Bayon",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cm8js8fei07wu28jlbrmzhs1v.jpg",
        listOf("CashAuto", "Кроссовер", "Комфорт"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        2500,
        22000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cm8js8fei07wu28jlbrmzhs1v.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cm8js8fei07wt28jlh7dj2gvy.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cm8js8feh07ws28jl9h7yb9o0.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cm8js8fef07wr28jl7w5e7fbz.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cm8js8fec07wq28jl9yqn3sg4.jpg"
        )
    ),
    Car(
        9,
        "Hyundai Creta",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt77hwct0mk2pojl63wtau00.jpeg",
        listOf("CashAuto", "Кроссовер", "SUV"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        2800,
        24000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt77hwct0mk2pojl63wtau00.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clm4p5d9g03buzbjl5w0o85y3.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clm4p5g2p03byzbjlbxlz5xan.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clm4p5g2q03bzzbjlffj73grw.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clm4p5d9h03bvzbjl05468fy2.jpeg"
        )
    ),
    Car(10, "Hyundai i30", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmbugau1a00tcu7jl9mm8e961.jpg", listOf("CashAuto", "Комфорт", "Хэтчбек"), "5 орын", "Автомат", "Бензин AI-95", 2800, 24000),
    Car(
        11,
        "Kia Cerato",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtguujs50vfxqwjl8sw6265z.jpeg",
        listOf("CashAuto", "Комфорт+", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3000,
        24000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtguujs50vfxqwjl8sw6265z.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmlwnccch003k61jlc0l6a0dh.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmlwncfis003e4sjl48je84jl.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmlwncp04003m61jl8xokc03c.jpg"
        )
    ),
    Car(
        12,
        "Dongfeng Minivan",
        "CashAuto Minivan",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmnafwy2z02ueh0jl69jl7b7e.jpeg",
        listOf("CashAuto", "Минивэн", "8 орын"),
        "8 орын",
        "Автомат",
        "Бензин AI-92",
        3000,
        22000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmnafwy2z02ueh0jl69jl7b7e.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmnafwy3e02uih0jl7zqjflop.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmnafwy3d02uhh0jle39qeu7r.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmnafwy3f02ujh0jle9sv09ei.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmnafwy3902ugh0jle6p29tv8.jpeg"
        )
    ),
    Car(
        13,
        "Ducati Panigale Electro",
        "CashAuto Moto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmpz7pwau00mbdkjlaiy1g2wk.jpeg",
        listOf("CashAuto", "Мото", "Lux"),
        "2 орын",
        "Редуктор",
        "Электр",
        5000,
        25000,
        true,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmpz7pwau00mbdkjlaiy1g2wk.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmpz7ptmt00m8dkjl4flt4xwz.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmpz7ptmy00m9dkjl755ndjqq.jpeg"
        )
    ),
    Car(
        14,
        "Hyundai Elantra Full",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquui7zclsddpdjl74iog3gh.jpg",
        listOf("CashAuto", "Бизнес", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3300,
        25000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquui7zclsddpdjl74iog3gh.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquslbimlkz3pdjl8ewp85ku.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquslvo4ll19pdjl7gov4bgc.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquslbijlkz2pdjl7ntt0109.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquslbiglkz1pdjlgw915rwo.jpeg"
        )
    ),
    Car(
        15,
        "Toyota Camry I",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13na5f01lucfjlbrwk0uqa.jpeg",
        listOf("CashAuto", "Седан", "Комфорт"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3000,
        25000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13na5f01lucfjlbrwk0uqa.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13na5e01ltcfjlbt0uhw97.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cl445nn2x064lj7jl2ihee2bs.jpg"
        )
    ),
    Car(
        16,
        "Toyota Camry Luxe",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquyh46um8g7o8jl9mek4ift.jpg",
        listOf("CashAuto", "Седан", "Lux"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3300,
        28000,
        true,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquyh46um8g7o8jl9mek4ift.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquso02qll9mpdjl5ip3bep5.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cl85px4ky019ko2jle2dperzr.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cl85px8zw019zoijl6ylv8k1k.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cl85px8zt019yoijlbx4le2jv.jpg"
        )
    ),
    Car(
        17,
        "Hyundai Creta (2023)",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfq2tsk0un3pojl15w16a4e.jpeg",
        listOf("CashAuto", "Кроссовер", "SUV"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3500,
        28000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfq2tsk0un3pojl15w16a4e.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clj1i4fv2007nifjlhji2h9v8.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clj1i45ju007xgzjldwxo2bqq.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clj1i45jr007wgzjl31dabyhu.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clj1i40v1007tgzjle78henf5.jpeg"
        )
    ),
    Car(18, "Toyota Camry I (2016)", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmsh18hcb070mpojl446b8szu.jpeg", listOf("CashAuto", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 3200, 27000),
    Car(
        19,
        "Changan CS35 MAX Full",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt5o98xz0lfpqwjl6xgk1d5v.jpeg",
        listOf("CashAuto", "Кроссовер", "Новинка"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3300,
        28000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt5o98xz0lfpqwjl6xgk1d5v.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt5o98xx0lfoqwjlgazlc9jf.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt5o98xv0lfnqwjl30s6eblz.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt5o98xl0lflqwjlce2rbm3x.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt5o98xt0lfmqwjle3ln9yv0.jpeg"
        )
    ),
    Car(
        20,
        "Kia Seltos Full",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cm8d023qd06k628jl2el326k3.jpeg",
        listOf("CashAuto", "Кроссовер", "SUV"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3500,
        30000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cm8d023qd06k628jl2el326k3.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cm8d023qc06k528jle45tdds1.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cm8jsdipp07wy28jl3vx1d0ki.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cm8d023q906k428jl91gn5myo.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cm8jsdipm07wx28jlc00rdcu2.jpeg"
        )
    ),
    Car(
        21,
        "Changan CS55 Plus",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmptwu1w900575xjl9xf20vfj.jpeg",
        listOf("CashAuto", "Кроссовер", "Турбо"),
        "5 орын",
        "Робот",
        "Бензин AI-95",
        3700,
        30000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmptwu1w900575xjl9xf20vfj.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmptwu1w700565xjlbymkfh0p.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmptwu1w600555xjlb9ih0m6n.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmptwu1w500545xjlhcw4147x.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmptwu1w300535xjl64mr17vw.jpeg"
        )
    ),
    Car(22, "Kia K5", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmiaab5oq00c1hgjlexaz2w48.jpg", listOf("CashAuto", "Бизнес", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 4000, 30000),
    Car(
        23,
        "CFMOTO CF 450CL",
        "CashAuto Moto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmo2tps1j000p5hjl1veohajs.jpeg",
        listOf("CashAuto", "Мото", "Sport"),
        "2 орын",
        "Механика",
        "Бензин AI-95",
        6000,
        30000,
        true,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmo2tps1j000p5hjl1veohajs.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmo2tps1k000q5hjl1h7y4rw5.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmo2tps1h000o5hjlfveg2g1y.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmpo6tigp03h6m0jlhv5m86o9.jpg"
        )
    ),
    Car(
        24,
        "Changan CS75 Full",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmu10nc7k06zklxjldryrf3xo.jpeg",
        listOf("CashAuto", "Кроссовер", "SUV"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3500,
        30000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmu10nc7k06zklxjldryrf3xo.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmson35hj0bcepojl9hfe24ud.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmson35hd0bccpojl63j3c8zu.jpeg"
        )
    ),
    Car(
        25,
        "Hyundai Tucson Full 4WD",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cms8islx401zkpojlhdaa5pcy.jpeg",
        listOf("CashAuto", "4WD", "Кроссовер"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4200,
        32000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cms8islx401zkpojlhdaa5pcy.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cms8ispn501zspojl0vov83an.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cms8ispn201zqpojl2pyy0ek3.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cms8ispn001zppojlaa633gfx.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cms8ispn301zrpojl16wn2mvc.jpeg"
        )
    ),
    Car(
        26,
        "Toyota RAV4 Full 4WD",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13jv4t01mjdkjlew7a7hub.jpeg",
        listOf("CashAuto", "4WD", "SUV"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3500,
        32000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13jv4t01mjdkjlew7a7hub.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13jv4o01midkjlavit5nns.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq0yslsr01kndkjlhxfbdf6m.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq0yslss01kodkjlhlyqfc8i.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq0yslsj01kmdkjl7in6c6ag.jpeg"
        )
    ),
    Car(
        27,
        "Hyundai Sonata Full I",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt897s5f0n1oqwjladfnf0gu.jpeg",
        listOf("CashAuto", "Бизнес", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3800,
        32000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt897s5f0n1oqwjladfnf0gu.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt897s5d0n1nqwjl1l8p03jh.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt897s5b0n1mqwjla8p31aww.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt897s570n1lqwjl2x042plu.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt897s4y0n1kqwjlaellauea.jpeg"
        )
    ),
    Car(
        28,
        "Toyota Camry Full",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmiagfrd000dhhgjle7oyeazw.jpg",
        listOf("CashAuto", "Бизнес", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        33000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmiagfrd000dhhgjle7oyeazw.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmia9u8rc00bohgjlba4jhz9n.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmia9u8r700bmhgjl9soibhf8.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmiagfrd700djhgjlfp8kb3ti.JPG",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmiagfrd500dihgjl8d77hil3.JPG"
        )
    ),
    Car(
        29,
        "Kia K5 Full",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmc2cvhz600c1s1jlfkwbd90d.jpg",
        listOf("CashAuto", "Бизнес", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        33000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmc2cvhz600c1s1jlfkwbd90d.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmbugj7k800u0u7jlbhwscqeg.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmbugj7k500tzu7jl4ptu7hnv.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmbugj7kf00u2u7jl8i806e2n.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmbugj7k100tyu7jlg40l1tpv.jpg"
        )
    ),
    Car(
        30,
        "Hyundai Tucson",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtlfz1fi10npqwjl32er6vh7.jpeg",
        listOf("CashAuto", "Кроссовер", "SUV"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4500,
        36000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtlfz1fi10npqwjl32er6vh7.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmtlfz1f710nlqwjl13ykf1np.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmtlfz1fd10nmqwjl0a5h0hy6.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmtlfz1fe10nnqwjl78xa00pf.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmtlfz1fg10noqwjl3jnfgpjt.jpeg"
        )
    ),
    Car(
        31,
        "Kia Sportage Full",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt8c4az60n2xqwjl7a1r8j33.jpeg",
        listOf("CashAuto", "Кроссовер", "SUV"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4500,
        36000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmt8c4az60n2xqwjl7a1r8j33.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt8c4az80n2yqwjlbvwqh39p.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt8c4az40n2wqwjleiipbd8m.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt8c4az10n2vqwjl56091w02.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmt8c4ays0n2uqwjlgxdb02sq.jpeg"
        )
    ),
    Car(32, "Hyundai Sonata Full", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cms7aycg001f3qwjl3wwu4nym.jpeg", listOf("CashAuto", "Бизнес", "Седан"), "5 орын", "Автомат", "Бензин AI-95", 4500, 36000),
    Car(
        33,
        "Hyundai SantaFe Full 4WD",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmdmc2pwd01a2bkjlejsm4pmt.jpeg",
        listOf("CashAuto", "4WD", "SUV 7 орын"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        4800,
        40000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmdmc2pwd01a2bkjlejsm4pmt.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmdmc2pwf01a3bkjl0o7h6l76.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmdmbyuzz019ocpjl5h3kfkxk.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmdmbyv0m019pcpjl1kfe0bv6.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmdmbz643019xbkjlgow0cnr2.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmdmbz644019ybkjl2xzpbdu4.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clutujr3700j67bjle3hubgih.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cluv9kc6f03tx7bjl1jrs8ue7.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmdmbz63z019wbkjl3vcx5ymt.jpeg"
        )
    ),
    Car(
        34,
        "Dodge Charger Full",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmo2sau3k0acsn8jlhd8t87xu.jpg",
        listOf("CashAuto", "Muscle", "Sport"),
        "5 орын",
        "Автомат",
        "Бензин AI-98",
        5000,
        40000,
        true,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmo2sau3k0acsn8jlhd8t87xu.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmia9muve00behgjlgnar20bu.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmia9muvd00bdhgjl6rpsa8ne.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmia9muva00bchgjl778dg11a.jpg"
        )
    ),
    Car(35, "Ford Mustang Full", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmh07qrxc06zkgfjl2aojf5yk.jpg", listOf("CashAuto", "Sport", "Lux"), "4 орын", "Автомат", "Бензин AI-98", 6000, 40000, true),
    Car(
        36,
        "Changan UNI-K Full 4WD",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmsoioz9g0baypojlhqgsex7v.jpeg",
        listOf("CashAuto", "4WD", "Премиум"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4500,
        40000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmsoioz9g0baypojlhqgsex7v.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsoioz8i0bavpojl82qtgahe.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsoioz9d0baxpojl9r5ag0ye.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsoioz910bawpojl7icsctq7.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsojbmfb0ba8qwjl3nwe8lll.jpeg"
        )
    ),
    Car(
        37,
        "Kia Sorento Full 4WD",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquyh46xm8g8o8jlcy4ze1jg.jpeg",
        listOf("CashAuto", "4WD", "SUV 7 орын"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        5500,
        43000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmquyh46xm8g8o8jlcy4ze1jg.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13kt7n01lmcfjlemqfdj28.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13kt7v01locfjl8jjf8mpg.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13kt7x01lqcfjl2d8zgcec.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clhq3su9b00huc1jlddcv6ge8.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13kt7w01lpcfjl9zj739sz.jpeg"
        )
    ),
    Car(
        38,
        "Toyota LC Prado Full",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmrefahj571t760jl7urdh4au.jpeg",
        listOf("CashAuto", "Внедорожник", "Lux"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        5500,
        45000,
        true,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmrefahj571t760jl7urdh4au.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmrdgxztf24p49zjlco1u4uun.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmrdgxz3n24os9zjlhrqxe6un.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmrdgxzd624ow9zjl1cpbfj58.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmia9e05f00b4hgjl0zyg9fts.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/clsa00wrk0k1df8jlhywe8554.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmrdgy04324p79zjle47qdzcp.jpeg"
        )
    ),
    Car(
        39,
        "Toyota Sienna Full Minivan",
        "CashAuto Minivan",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13ltny01mqdkjl9e5u2ibw.jpeg",
        listOf("CashAuto", "Минивэн", "8 орын"),
        "8 орын",
        "Автомат",
        "Бензин AI-95",
        5500,
        45000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmq13ltny01mqdkjl9e5u2ibw.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13ltnw01mpdkjl50y3985d.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13ltnz01mrdkjl8nlw9jqi.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13lto001msdkjl7tnz20h3.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmq13lto401mtdkjlbsrj0mcb.jpeg"
        )
    ),
    Car(40, "BMW 428i Cabriolet", "CashAuto", "CashAuto · Жайдарман 2/1", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmettvuss00evsujl0zwzfjrk.jpeg", listOf("CashAuto", "Кабриолет", "Lux"), "4 орын", "Автомат", "Бензин AI-98", 9500, 45000, true),
    Car(
        41,
        "Toyota Camry 80 Luxe",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfqj7s70undpojl42c1e78b.jpg",
        listOf("CashAuto", "Camry 80", "Lux"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        6000,
        45000,
        true,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfqj7s70undpojl42c1e78b.jpg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmqw51a8wuouno8jlbrpbd9xg.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmqw51a8muoujo8jl20df9u5e.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmqw51a8quouko8jlh068fkf2.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmqw51a8vuoumo8jl9vabcqyy.jpeg"
        )
    ),
    Car(
        42,
        "Changan Hunter Plus 4WD",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmsg61ldq06hppojld89ahshl.jpeg",
        listOf("CashAuto", "Пикап", "4WD"),
        "5 орын",
        "Автомат",
        "Дизель",
        5500,
        45000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmsg61ldq06hppojld89ahshl.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsg62pee06iuqwjlfpoea5vj.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsg5hn4f06hbpojl67039cdn.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsg5hn4q06hepojl1wf72vuh.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsg5hn4o06hdpojl9x9a6f7w.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmsg5hn3h06h9pojl6fuu6g8s.jpeg"
        )
    ),
    Car(
        43,
        "Toyota Highlander I",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtl66h2y10htpojl3vx29hyq.jpeg",
        listOf("CashAuto", "SUV 7 орын", "Lux"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        5000,
        45000,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtl66h2y10htpojl3vx29hyq.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmtl66h2v10hspojl8q7f21y9.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmtl66h2t10hrpojlb07bg949.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmtl66h2r10hqpojle53r7aon.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmtl66h2m10hppojl2r0xd1cm.jpeg"
        )
    ),
    Car(44, "Toyota Camry Hybrid I", "CashAuto", "CashAuto · Аэропорт Т1 (24/7)", "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfqj7s70undpojl42c1e78b.jpg", listOf("CashAuto", "Гибрид", "Lux"), "5 орын", "Автомат", "Гибрид", 6500, 48000, true),
    Car(
        45,
        "Lexus LX 570",
        "CashAuto",
        "CashAuto · Жайдарман 2/1",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmptwjc9x004b5xjlc3lc5g1u.jpeg",
        listOf("CashAuto", "Премиум", "SUV"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        7000,
        50000,
        true,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmptwjc9x004b5xjlc3lc5g1u.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmptwjhja004a74jl25vxgry0.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmptwjqvu004j5xjl9da3dlfd.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmptwjqvs004i5xjl7f0n8nxg.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmptwjqvp004g5xjla4bfdb7p.jpeg"
        )
    ),
    Car(
        46,
        "Toyota Land Cruiser Restyling",
        "CashAuto",
        "CashAuto · Аэропорт Т1 (24/7)",
        "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmrewnwhj17xyeojl7w7lh7w5.jpeg",
        listOf("CashAuto", "Внедорожник", "Lux"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        8000,
        52000,
        true,
        imageUrls = listOf(
            "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmrewnwhj17xyeojl7w7lh7w5.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquso039ll9spdjl87vv4rsi.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquso036ll9ppdjlduhe5wx6.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquso035ll9opdjl1uovdpwp.jpeg",
            "https://cashauto.kz/uploads/uploads/images/venicles/cmquso037ll9qpdjlbqle3eiz.jpeg"
        )
    ),
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
    Car(92, "Hyundai Elantra Red QazQar", "QazQar", "QazQar · Астана", "https://qazqar.kz/_next/image?url=https%3A%2F%2Fqazqar.d3v.kz%2Fqazqar-images%2Fcars%2F1d4e0ef7d0aa07b062c99971.jpg&w=640&q=75", listOf("QazQar", "Эконом", "Седан", "Комфорт"), "5 орын", "Автомат", "Бензин AI-95", 2700, 22000),
    Car(93, "Mercedes-Benz G63 AMG", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=800", listOf("AZV MOTORS", "Внедорожник", "Lux", "2021"), "5 орын", "Автомат", "Бензин AI-98", 28460, 349990, true),
    Car(94, "Porsche Panamera Turbo", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=800", listOf("AZV MOTORS", "Седан", "Lux", "2022"), "4 орын", "Автомат", "Бензин AI-98", 16240, 199990, true),
    Car(95, "Mercedes-Benz E63s AMG", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=800", listOf("AZV MOTORS", "Седан", "Lux", "2018"), "5 орын", "Автомат", "Бензин AI-98", 16240, 199990, true),
    Car(96, "Mercedes-Benz E200", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=800", listOf("AZV MOTORS", "Седан", "Комфорт", "2024"), "5 орын", "Автомат", "Бензин AI-95", 7340, 89990),
    Car(97, "Range Rover Sport", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=800", listOf("AZV MOTORS", "Внедорожник", "SUV", "2015"), "5 орын", "Автомат", "Бензин AI-95", 7310, 89990, true),
    Car(98, "Toyota Camry 75 AZV", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=800", listOf("AZV MOTORS", "Седан", "Комфорт", "2023"), "5 орын", "Автомат", "Бензин AI-95", 4500, 45000),
    Car(99, "Li L7 Ultra", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800", listOf("AZV MOTORS", "Кроссовер", "Гибрид", "Lux", "2024"), "5 орын", "Автомат", "Гибрид", 12500, 150000, true),
    Car(100, "BMW 530i AZV", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1555215695-3004980ad54e?q=80&w=800", listOf("AZV MOTORS", "Бизнес", "Седан", "2023"), "5 орын", "Автомат", "Бензин AI-95", 8500, 95000, true),
    Car(101, "BYD Han EV AZV", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=800", listOf("AZV MOTORS", "Электр", "Седан", "2024"), "5 орын", "Автомат", "Электр", 7500, 85000, true),
    Car(102, "Mercedes-Benz CLA 45s", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8000", listOf("AZV MOTORS", "Sport", "Седан", "2023"), "5 орын", "Робот", "Бензин AI-98", 14000, 160000, true),
    Car(103, "Mercedes-Benz S580 Maybach", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8001", listOf("AZV MOTORS", "Lux", "Седан", "2023"), "4 орын", "Автомат", "Бензин AI-98", 35000, 420000, true),
    Car(104, "Mercedes-Benz GLS 600 Maybach", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8002", listOf("AZV MOTORS", "Lux", "Внедорожник", "2023"), "4 орын", "Автомат", "Бензин AI-98", 38000, 450000, true),
    Car(105, "Mercedes-Benz GLE 53 Coupe", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8003", listOf("AZV MOTORS", "Кроссовер", "Lux", "2022"), "5 орын", "Автомат", "Бензин AI-98", 18000, 220000, true),
    Car(106, "Mercedes-Benz C300 AMG", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8004", listOf("AZV MOTORS", "Седан", "Комфорт", "2023"), "5 орын", "Автомат", "Бензин AI-95", 9500, 110000),
    Car(107, "Porsche Cayenne Coupe", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8005", listOf("AZV MOTORS", "Кроссовер", "Lux", "2022"), "5 орын", "Автомат", "Бензин AI-98", 22000, 260000, true),
    Car(108, "Porsche Taycan Turbo S", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8006", listOf("AZV MOTORS", "Электр", "Sport", "2023"), "4 орын", "Редуктор", "Электр", 25000, 290000, true),
    Car(109, "Porsche Macan GTS", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8007", listOf("AZV MOTORS", "Кроссовер", "Sport", "2021"), "5 орын", "Робот", "Бензин AI-98", 15000, 180000, true),
    Car(110, "BMW M5 Competition", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8008", listOf("AZV MOTORS", "Sport", "Седан", "2022"), "5 орын", "Автомат", "Бензин AI-98", 22000, 270000, true),
    Car(111, "BMW M4 Competition", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1520031441872-265e4ff76c66?q=80&w=8009", listOf("AZV MOTORS", "Sport", "Купе", "2023"), "4 орын", "Автомат", "Бензин AI-98", 20000, 240000, true),
    Car(112, "BMW X5M Competition", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8000", listOf("AZV MOTORS", "Внедорожник", "Lux", "2022"), "5 орын", "Автомат", "Бензин AI-98", 21000, 250000, true),
    Car(113, "BMW X6M Competition", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8001", listOf("AZV MOTORS", "Кроссовер", "Lux", "2022"), "5 орын", "Автомат", "Бензин AI-98", 23000, 270000, true),
    Car(114, "BMW X7 M50i", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8002", listOf("AZV MOTORS", "Внедорожник", "7 орын", "2023"), "7 орын", "Автомат", "Бензин AI-98", 24000, 280000, true),
    Car(115, "BMW 750i xDrive", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8003", listOf("AZV MOTORS", "Lux", "Седан", "2023"), "5 орын", "Автомат", "Бензин AI-98", 26000, 310000, true),
    Car(116, "Audi RS6 Avant", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8004", listOf("AZV MOTORS", "Sport", "Универсал", "2022"), "5 орын", "Автомат", "Бензин AI-98", 22000, 260000, true),
    Car(117, "Audi RS7 Sportback", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8005", listOf("AZV MOTORS", "Sport", "Lux", "2022"), "5 орын", "Автомат", "Бензин AI-98", 23000, 270000, true),
    Car(118, "Audi RS Q8", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8006", listOf("AZV MOTORS", "Кроссовер", "Lux", "2022"), "5 орын", "Автомат", "Бензин AI-98", 24000, 280000, true),
    Car(119, "Li L9 Max", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8007", listOf("AZV MOTORS", "Кроссовер", "Гибрид", "Lux", "2024"), "6 орын", "Автомат", "Гибрид", 15000, 180000, true),
    Car(120, "Li L8 Max", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8008", listOf("AZV MOTORS", "Кроссовер", "Гибрид", "2024"), "6 орын", "Автомат", "Гибрид", 13500, 160000, true),
    Car(121, "Li L6 Max", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?q=80&w=8009", listOf("AZV MOTORS", "Кроссовер", "Гибрид", "2024"), "5 орын", "Автомат", "Гибрид", 11000, 130000, true),
    Car(122, "Zeekr 001 FR", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8000", listOf("AZV MOTORS", "Электр", "Sport", "2024"), "5 орын", "Редуктор", "Электр", 18000, 210000, true),
    Car(123, "Zeekr 009 MPV", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8001", listOf("AZV MOTORS", "Минивэн", "Lux", "2024"), "6 орын", "Редуктор", "Электр", 20000, 240000, true),
    Car(124, "Zeekr X", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8002", listOf("AZV MOTORS", "Электр", "Кроссовер", "2024"), "5 орын", "Редуктор", "Электр", 9000, 100000),
    Car(125, "BYD Yangwang U8", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8003", listOf("AZV MOTORS", "Внедорожник", "Lux", "2024"), "5 орын", "Автомат", "Гибрид", 32000, 390000, true),
    Car(126, "BYD Seal Performance", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8004", listOf("AZV MOTORS", "Электр", "Седан", "2024"), "5 орын", "Редуктор", "Электр", 8500, 95000),
    Car(127, "BYD Song Plus Champion", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8005", listOf("AZV MOTORS", "Кроссовер", "Гибрид", "2024"), "5 орын", "Автомат", "Гибрид", 7000, 80000),
    Car(128, "Range Rover Autobiography", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8006", listOf("AZV MOTORS", "Lux", "Внедорожник", "2023"), "5 орын", "Автомат", "Бензин AI-98", 35000, 430000, true),
    Car(129, "Range Rover Defender 110", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8007", listOf("AZV MOTORS", "Внедорожник", "2023"), "5 орын", "Автомат", "Бензин AI-95", 22000, 260000, true),
    Car(130, "Range Rover Sport SVR", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8008", listOf("AZV MOTORS", "Sport", "Внедорожник", "2021"), "5 орын", "Автомат", "Бензин AI-98", 19000, 230000, true),
    Car(131, "Cadillac Escalade ESV", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?q=80&w=8009", listOf("AZV MOTORS", "Lux", "7 орын", "2023"), "7 орын", "Автомат", "Бензин AI-98", 30000, 360000, true),
    Car(132, "Chevrolet Corvette C8", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8000", listOf("AZV MOTORS", "Sport", "Кабриолет", "2023"), "2 орын", "Робот", "Бензин AI-98", 25000, 300000, true),
    Car(133, "Chevrolet Camaro SS", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8001", listOf("AZV MOTORS", "Muscle", "Sport", "2022"), "4 орын", "Автомат", "Бензин AI-98", 14000, 160000, true),
    Car(134, "Dodge Charger SRT Hellcat", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8002", listOf("AZV MOTORS", "Muscle", "Sport", "2022"), "5 орын", "Автомат", "Бензин AI-98", 18000, 220000, true),
    Car(135, "Dodge Challenger Scat Pack", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8003", listOf("AZV MOTORS", "Muscle", "Sport", "2022"), "4 орын", "Автомат", "Бензин AI-98", 16000, 190000, true),
    Car(136, "Ford Mustang GT Convertible", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8004", listOf("AZV MOTORS", "Sport", "Кабриолет", "2023"), "4 орын", "Автомат", "Бензин AI-98", 15000, 180000, true),
    Car(137, "Toyota Camry 80 Executive", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8005", listOf("AZV MOTORS", "Седан", "Бизнес", "2024"), "5 орын", "Автомат", "Бензин AI-95", 8000, 90000, true),
    Car(138, "Toyota Land Cruiser 300 GR", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8006", listOf("AZV MOTORS", "Внедорожник", "Lux", "2023"), "7 орын", "Автомат", "Бензин AI-95", 25000, 300000, true),
    Car(139, "Toyota LC Prado 250", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8007", listOf("AZV MOTORS", "Внедорожник", "2024"), "7 орын", "Автомат", "Бензин AI-95", 18000, 210000, true),
    Car(140, "Lexus LX 600 VIP", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8008", listOf("AZV MOTORS", "Lux", "Внедорожник", "2023"), "4 орын", "Автомат", "Бензин AI-98", 36000, 440000, true),
    Car(141, "Lexus RX 500h F Sport", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1563720223185-11003d516935?q=80&w=8009", listOf("AZV MOTORS", "Гибрид", "Кроссовер", "2023"), "5 орын", "Автомат", "Гибрид", 16000, 190000, true),
    Car(142, "Lexus ES 350 Luxury", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8000", listOf("AZV MOTORS", "Седан", "Бизнес", "2023"), "5 орын", "Автомат", "Бензин AI-95", 10000, 120000, true),
    Car(143, "Genesis G90 Long", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8001", listOf("AZV MOTORS", "Lux", "Седан", "2023"), "4 орын", "Автомат", "Бензин AI-98", 28000, 340000, true),
    Car(144, "Genesis GV80 Coupe", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8002", listOf("AZV MOTORS", "Кроссовер", "2024"), "5 орын", "Автомат", "Бензин AI-95", 17000, 200000, true),
    Car(145, "Hyundai Palisade Calligraphy", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8003", listOf("AZV MOTORS", "SUV 7 орын", "2023"), "7 орын", "Автомат", "Бензин AI-95", 12000, 140000),
    Car(146, "Hyundai Tucson N-Line", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8004", listOf("AZV MOTORS", "Кроссовер", "2024"), "5 орын", "Автомат", "Бензин AI-95", 6500, 75000),
    Car(147, "Hyundai Elantra N", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8005", listOf("AZV MOTORS", "Sport", "Седан", "2023"), "5 орын", "Робот", "Бензин AI-98", 7500, 85000),
    Car(148, "Kia Carnival Hi-Limousine", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8006", listOf("AZV MOTORS", "Минивэн", "Lux", "2024"), "7 орын", "Автомат", "Бензин AI-95", 18000, 220000, true),
    Car(149, "Kia K8 Signature", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8007", listOf("AZV MOTORS", "Седан", "Бизнес", "2023"), "5 орын", "Автомат", "Бензин AI-95", 9000, 105000),
    Car(150, "Kia K5 GT-Line", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8008", listOf("AZV MOTORS", "Седан", "2024"), "5 орын", "Автомат", "Бензин AI-95", 7000, 80000),
    Car(151, "Kia Sorento GT-Line", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=8009", listOf("AZV MOTORS", "Кроссовер", "2024"), "7 орын", "Автомат", "Бензин AI-95", 11000, 130000),
    Car(152, "Changan UNI-K Tech", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8000", listOf("AZV MOTORS", "Кроссовер", "2024"), "5 орын", "Автомат", "Бензин AI-95", 8500, 95000),
    Car(153, "Changan Hunter Plus 4WD AZV", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8001", listOf("AZV MOTORS", "Пикап", "4WD", "2024"), "5 орын", "Автомат", "Дизель", 9000, 100000),
    Car(154, "Tank 500 Premium", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8002", listOf("AZV MOTORS", "Внедорожник", "Lux", "2024"), "7 орын", "Автомат", "Бензин AI-95", 18000, 210000, true),
    Car(155, "Tank 300 Off-Road", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8003", listOf("AZV MOTORS", "Внедорожник", "2023"), "5 орын", "Автомат", "Бензин AI-95", 12000, 140000),
    Car(156, "EXEED VX Flagship", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8004", listOf("AZV MOTORS", "SUV 7 орын", "2024"), "7 орын", "Автомат", "Бензин AI-95", 14000, 160000),
    Car(157, "EXEED RX Coupe", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8005", listOf("AZV MOTORS", "Кроссовер", "2024"), "5 орын", "Робот", "Бензин AI-95", 13000, 150000),
    Car(158, "Aito Seres M9", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8006", listOf("AZV MOTORS", "Lux", "Кроссовер", "2024"), "6 орын", "Автомат", "Гибрид", 25000, 300000, true),
    Car(159, "Aito Seres M7", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8007", listOf("AZV MOTORS", "Кроссовер", "2024"), "6 орын", "Автомат", "Гибрид", 16000, 190000),
    Car(160, "Voyah Free Ultra", "AZV MOTORS", "AZV MOTORS · Алматы", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=8008", listOf("AZV MOTORS", "Электр", "Кроссовер", "2024"), "5 орын", "Редуктор", "Электр", 15000, 180000, true),
    Car(161, "Bentley Flying Spur", "AZV MOTORS", "AZV MOTORS · Алматы", "", listOf("AZV MOTORS", "Lux", "Седан", "2018"), "5 орын", "Автомат", "Бензин AI-98", 32000, 390000, true),
    Car(162, "Hyundai Custin", "AZV MOTORS", "AZV MOTORS · Алматы", "", listOf("AZV MOTORS", "Минивэн", "2026"), "7 орын", "Автомат", "Бензин AI-95", 11000, 130000),
    Car(163, "Hyundai Accent", "Sodex", "Sodex · Алматы", "https://sodex.kz/cars/accent.webp", listOf("Sodex", "Эконом", "Седан", "2022"), "5 орын", "Автомат", "Бензин AI-92", 2200, 17000),
    Car(164, "Hyundai Elantra", "Sodex", "Sodex · Алматы", "https://sodex.kz/cars/elantra-16.webp", listOf("Sodex", "Комфорт", "Седан", "2022"), "5 орын", "Автомат", "Бензин AI-95", 2700, 22000),
    Car(165, "Hyundai Elantra Plus", "Sodex", "Sodex · Алматы", "https://sodex.kz/cars/elantra-24.webp", listOf("Sodex", "Комфорт+", "Седан", "2023"), "5 орын", "Автомат", "Бензин AI-95", 2700, 22000),
    Car(166, "Hyundai Bayon", "Sodex", "Sodex · Алматы", "https://sodex.kz/cars/bayon.webp", listOf("Sodex", "Кроссовер", "2023"), "5 орын", "Автомат", "Бензин AI-95", 2400, 18000),
    Car(167, "Hyundai Mufasa", "Sodex", "Sodex · Алматы", "https://sodex.kz/cars/mufasa.webp", listOf("Sodex", "Кроссовер", "2025"), "5 орын", "Автомат", "Бензин AI-95", 4000, 35000),
    Car(168, "Jetour T1", "Sodex", "Sodex · Алматы", "https://sodex.kz/cars/jetour-t1.webp", listOf("Sodex", "Внедорожник", "4WD", "2024"), "5 орын", "Автомат", "Бензин AI-95", 4500, 40000, true),
    Car(
        169,
        "Geely Atlas",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/atlas.png",
        listOf("Rent Cars Kz", "Кроссовер", "Турбо", "2024"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://rent-cars.kz/img/atlas.png")
    ),
    Car(
        170,
        "Geely Coolray",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/coolray.png",
        listOf("Rent Cars Kz", "Кроссовер", "2024"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3000,
        25000,
        imageUrls = listOf("https://rent-cars.kz/img/coolray.png")
    ),
    Car(
        171,
        "Nissan Pathfinder",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/pathfinder.png",
        listOf("Rent Cars Kz", "Кроссовер", "7 орын", "2022"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        7000,
        60000,
        imageUrls = listOf("https://rent-cars.kz/img/pathfinder.png")
    ),
    Car(
        172,
        "Nissan Patrol",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/patrol.png",
        listOf("Rent Cars Kz", "Кроссовер", "Внедорожник", "7 орын", "2010"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        5500,
        45000,
        imageUrls = listOf("https://rent-cars.kz/img/patrol.png")
    ),
    Car(
        173,
        "Hyundai Elantra",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/elantra.png",
        listOf("Rent Cars Kz", "Комфорт", "Седан", "2024"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3000,
        25000,
        imageUrls = listOf("https://rent-cars.kz/img/elantra.png")
    ),
    Car(
        174,
        "Omoda C5",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/omoda.png",
        listOf("Rent Cars Kz", "Кроссовер", "Турбо", "2026"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3300,
        28000,
        imageUrls = listOf("https://rent-cars.kz/img/omoda.png")
    ),
    Car(
        175,
        "Chevrolet Malibu",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/malibu.png",
        listOf("Rent Cars Kz", "Комфорт", "Седан", "Турбо", "2017"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        2800,
        23000,
        imageUrls = listOf("https://rent-cars.kz/img/malibu.png")
    ),
    Car(
        176,
        "Ford Mustang",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/ford.png",
        listOf("Rent Cars Kz", "Спорт", "Lux", "2022"),
        "4 орын",
        "Автомат",
        "Бензин AI-98",
        11000,
        95000,
        true,
        imageUrls = listOf("https://rent-cars.kz/img/ford.png")
    ),
    Car(
        177,
        "Hyundai Solaris",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/solaris.png",
        listOf("Rent Cars Kz", "Эконом", "Седан", "2020"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2200,
        19000,
        imageUrls = listOf("https://rent-cars.kz/img/solaris.png")
    ),
    Car(
        178,
        "Kia Rio",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/rio2.png",
        listOf("Rent Cars Kz", "Эконом", "Седан", "2020"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2200,
        19000,
        imageUrls = listOf("https://rent-cars.kz/img/rio2.png")
    ),
    Car(
        179,
        "Skoda Rapid",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/rapid.png",
        listOf("Rent Cars Kz", "Эконом", "Седан", "2020"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2100,
        18000,
        imageUrls = listOf("https://rent-cars.kz/img/rapid.png")
    ),
    Car(
        180,
        "Volkswagen Polo",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/polo.png",
        listOf("Rent Cars Kz", "Эконом", "Седан", "2018"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2000,
        17000,
        imageUrls = listOf("https://rent-cars.kz/img/polo.png")
    ),
    Car(
        181,
        "Chevrolet Cobalt",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/cobalt.png",
        listOf("Rent Cars Kz", "Эконом", "Седан", "2024"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2000,
        16000,
        imageUrls = listOf("https://rent-cars.kz/img/cobalt.png")
    ),
    Car(
        182,
        "Kia Rio (2015)",
        "Rent Cars Kz",
        "Rent Cars Kz · Алматы",
        "https://rent-cars.kz/img/rio.png",
        listOf("Rent Cars Kz", "Эконом", "Седан", "2015"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        1800,
        15000,
        imageUrls = listOf("https://rent-cars.kz/img/rio.png")
    ),
    Car(
        183,
        "BYD Leopard 2025 - EV",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-143.png",
        listOf("Kazprokat", "Кроссовер", "Электр", "2025"),
        "5 орын",
        "Редуктор",
        "Электр",
        4000,
        35000,
        true,
        imageUrls = listOf("https://kazprokat.kz/assets/image-143.png")
    ),
    Car(
        184,
        "Hyundai Tucson 2.5 2023",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-147.png",
        listOf("Kazprokat", "Кроссовер", "2023"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-147.png")
    ),
    Car(
        185,
        "Hyundai Creta 2.0 2022",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-150.png",
        listOf("Kazprokat", "Кроссовер", "2022"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3000,
        25000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-150.png")
    ),
    Car(
        186,
        "Toyota Camry 80 2025",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-142.png",
        listOf("Kazprokat", "Седан", "Camry 80", "Lux", "2025"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        6000,
        50000,
        true,
        imageUrls = listOf("https://kazprokat.kz/assets/image-142.png")
    ),
    Car(
        187,
        "Toyota Camry 75 2025",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-146.png",
        listOf("Kazprokat", "Седан", "Camry 75", "Комфорт", "2025"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-146.png")
    ),
    Car(
        188,
        "Hyundai Sonata 2.0 2022",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-149.png",
        listOf("Kazprokat", "Седан", "Комфорт", "2022"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3500,
        30000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-149.png")
    ),
    Car(
        189,
        "Hyundai Elantra 1.6 22/24",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-151.png",
        listOf("Kazprokat", "Эконом", "Седан", "2024"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3000,
        25000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-151.png")
    ),
    Car(
        190,
        "Hyundai Accent 1.6 2022",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-153.png",
        listOf("Kazprokat", "Эконом", "Седан", "2022"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2400,
        20000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-153.png")
    ),
    Car(
        191,
        "Hyundai Accent 1.6 2019",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-152.png",
        listOf("Kazprokat", "Эконом", "Седан", "2019"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2000,
        17000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-152.png")
    ),
    Car(
        192,
        "Hyundai Staria 2025",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/staria-3.png",
        listOf("Kazprokat", "Минивэн", "Lux", "2025"),
        "11 орын",
        "Автомат",
        "Дизель",
        8500,
        70000,
        true,
        imageUrls = listOf("https://kazprokat.kz/assets/staria-3.png")
    ),
    Car(
        193,
        "Mercedes-Benz G550 2015",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/gelen-2.png",
        listOf("Kazprokat", "Внедорожник", "Lux", "2015"),
        "5 орын",
        "Автомат",
        "Бензин AI-98",
        13000,
        110000,
        true,
        imageUrls = listOf("https://kazprokat.kz/assets/gelen-2.png")
    ),
    Car(
        194,
        "Toyota LC 200 2017",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/tlc200-1.png",
        listOf("Kazprokat", "Внедорожник", "Lux", "2017"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        9500,
        80000,
        true,
        imageUrls = listOf("https://kazprokat.kz/assets/tlc200-1.png")
    ),
    Car(
        195,
        "Toyota Prado 4.0 2020",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-144.png",
        listOf("Kazprokat", "Внедорожник", "2020"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        8500,
        70000,
        true,
        imageUrls = listOf("https://kazprokat.kz/assets/image-144.png")
    ),
    Car(
        196,
        "Toyota Prado 2.7 2022",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-145.png",
        listOf("Kazprokat", "Внедорожник", "2022"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        7000,
        60000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-145.png")
    ),
    Car(
        197,
        "Toyota Prado 2.7 2020",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/prado2020-1.png",
        listOf("Kazprokat", "Внедорожник", "2020"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        6000,
        50000,
        imageUrls = listOf("https://kazprokat.kz/assets/prado2020-1.png")
    ),
    Car(
        198,
        "Toyota Fortuner 2.7 2023",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-154.png",
        listOf("Kazprokat", "Внедорожник", "2023"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        5500,
        45000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-154.png")
    ),
    Car(
        199,
        "Toyota Fortuner 2.7 2015",
        "Kazprokat",
        "Kazprokat · Алматы",
        "https://kazprokat.kz/assets/image-148.png",
        listOf("Kazprokat", "Внедорожник", "2015"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://kazprokat.kz/assets/image-148.png")
    ),
    Car(
        200,
        "Zeekr 001",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/zeekr-001-1.oU3ZLPnq_2wE31p.avif",
        listOf("Silk Way", "Электр", "Кроссовер", "Sport"),
        "5 орын",
        "Редуктор",
        "Электр",
        6500,
        55000,
        true,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/zeekr-001-1.oU3ZLPnq_2wE31p.avif")
    ),
    Car(
        201,
        "Zeekr 009",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/zeekr-009-1.BeVVjoSm_WRnWl.avif",
        listOf("Silk Way", "Минивэн", "Lux", "Электр"),
        "6 орын",
        "Редуктор",
        "Электр",
        10000,
        90000,
        true,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/zeekr-009-1.BeVVjoSm_WRnWl.avif")
    ),
    Car(
        202,
        "Lixiang L9",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/lixiang-l9-1.DXHsRk1i_MDBqr.avif",
        listOf("Silk Way", "Кроссовер", "Гибрид", "Lux"),
        "6 орын",
        "Автомат",
        "Гибрид",
        10000,
        90000,
        true,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/lixiang-l9-1.DXHsRk1i_MDBqr.avif")
    ),
    Car(
        203,
        "Toyota Land Cruiser 200",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/toyota-land-cruiser-200-1.BaNHcKWj_Z1754TQ.avif",
        listOf("Silk Way", "Внедорожник", "7 орын", "Lux"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        7500,
        65000,
        true,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/toyota-land-cruiser-200-1.BaNHcKWj_Z1754TQ.avif")
    ),
    Car(
        204,
        "Chevrolet Cobalt",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/chevrolet-cobalt-1.xnPXM7Wu_1jTwfz.avif",
        listOf("Silk Way", "Эконом", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2000,
        16000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/chevrolet-cobalt-1.xnPXM7Wu_1jTwfz.avif")
    ),
    Car(
        205,
        "Kia Soluto",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/kia-soluto-1.B226iLb__MDcg6.avif",
        listOf("Silk Way", "Эконом", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2200,
        18000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/kia-soluto-1.B226iLb__MDcg6.avif")
    ),
    Car(
        206,
        "Hyundai Elantra",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/hyundai-elantra-1.C2qxIZEB_k3rev.avif",
        listOf("Silk Way", "Комфорт", "Седан"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        2800,
        24000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/hyundai-elantra-1.C2qxIZEB_k3rev.avif")
    ),
    Car(
        207,
        "Suzuki Baleno",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/suzuki-baleno-1.fu6soBt1_ZveqKL.avif",
        listOf("Silk Way", "Эконом", "Хэтчбек"),
        "5 орын",
        "Автомат",
        "Бензин AI-92",
        2800,
        24000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/suzuki-baleno-1.fu6soBt1_ZveqKL.avif")
    ),
    Car(
        208,
        "Chery Arrizo 8",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/chery-arrizo-8-1.DOZfMe3O_Z1D8s2m.avif",
        listOf("Silk Way", "Седан", "Бизнес", "Турбо"),
        "5 орын",
        "Робот",
        "Бензин AI-95",
        3300,
        28000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/chery-arrizo-8-1.DOZfMe3O_Z1D8s2m.avif")
    ),
    Car(
        209,
        "Chery Tiggo 4 Pro",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/chery-tiggo-4-pro-1.BdA7WzAm_1s3tS.avif",
        listOf("Silk Way", "Кроссовер"),
        "5 орын",
        "Вариатор",
        "Бензин AI-95",
        3000,
        26000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/chery-tiggo-4-pro-1.BdA7WzAm_1s3tS.avif")
    ),
    Car(
        210,
        "Chery Tiggo 7",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/chery-tiggo-7-1.BBm1lgZb_Z2n7e0G.avif",
        listOf("Silk Way", "Кроссовер"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/chery-tiggo-7-1.BBm1lgZb_Z2n7e0G.avif")
    ),
    Car(
        211,
        "Haval M6",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/haval-m6-1.DBQCRZc6_2km2Df.avif",
        listOf("Silk Way", "Кроссовер", "Турбо"),
        "5 орын",
        "Робот",
        "Бензин AI-95",
        3300,
        28000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/haval-m6-1.DBQCRZc6_2km2Df.avif")
    ),
    Car(
        212,
        "JAC JS6",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/jac-js6-1.CzzmdiJb_ykeBN.avif",
        listOf("Silk Way", "Кроссовер"),
        "5 орын",
        "Робот",
        "Бензин AI-95",
        3300,
        28000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/jac-js6-1.CzzmdiJb_ykeBN.avif")
    ),
    Car(
        213,
        "Jetour X70",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/jetour-x70-1.BEhnXw0m_Z2noxWP.avif",
        listOf("Silk Way", "Кроссовер", "7 орын"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/jetour-x70-1.BEhnXw0m_Z2noxWP.avif")
    ),
    Car(
        214,
        "Hyundai Tucson",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/hyundai-tucson-1.rak2nQfi_ttiNv.avif",
        listOf("Silk Way", "Кроссовер", "4WD"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/hyundai-tucson-1.rak2nQfi_ttiNv.avif")
    ),
    Car(
        215,
        "Hyundai Mufasa",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/hyundai-mufasa-1.D4Mo1COi_8Ul6y.avif",
        listOf("Silk Way", "Кроссовер"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/hyundai-mufasa-1.D4Mo1COi_8Ul6y.avif")
    ),
    Car(
        216,
        "Kia Sorento",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/kia-sorento-1.JEhQvd7N_Z24pmFm.avif",
        listOf("Silk Way", "Кроссовер", "Турбо"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/kia-sorento-1.JEhQvd7N_Z24pmFm.avif")
    ),
    Car(
        217,
        "Hyundai Palisade",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/hyundai-palisade-1.Ci6KxIED_TUHKm.avif",
        listOf("Silk Way", "Внедорожник", "7 орын", "4WD", "Lux"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        7000,
        60000,
        true,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/hyundai-palisade-1.Ci6KxIED_TUHKm.avif")
    ),
    Car(
        218,
        "Mitsubishi Pajero Sport",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/mitsubishi-pajero-sport-1.CIjI2fSo_26RUMB.avif",
        listOf("Silk Way", "Внедорожник", "4WD"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        3500,
        30000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/mitsubishi-pajero-sport-1.CIjI2fSo_26RUMB.avif")
    ),
    Car(
        219,
        "Zeekr X",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/zeekr-x-1.CWwhUu4t_1lRbLc.avif",
        listOf("Silk Way", "Электр", "Кроссовер", "4WD"),
        "5 орын",
        "Редуктор",
        "Электр",
        4000,
        35000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/zeekr-x-1.CWwhUu4t_1lRbLc.avif")
    ),
    Car(
        220,
        "Volkswagen ID.4",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/volkswagen-id4-1.zbPzEDLN_Z1cpE6J.avif",
        listOf("Silk Way", "Электр", "Кроссовер"),
        "5 орын",
        "Редуктор",
        "Электр",
        3500,
        30000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/volkswagen-id4-1.zbPzEDLN_Z1cpE6J.avif")
    ),
    Car(
        221,
        "Honda e:NS1",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/honda-e-ns1-1.DsimCv1j_Z2ajMI2.avif",
        listOf("Silk Way", "Электр", "Кроссовер"),
        "5 орын",
        "Редуктор",
        "Электр",
        3300,
        28000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/honda-e-ns1-1.DsimCv1j_Z2ajMI2.avif")
    ),
    Car(
        222,
        "BYD Song Plus",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/byd-song-plus-1.CouzXRxu_2dQlpk.avif",
        listOf("Silk Way", "Гибрид", "Кроссовер"),
        "5 орын",
        "Автомат",
        "Гибрид",
        4000,
        35000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/byd-song-plus-1.CouzXRxu_2dQlpk.avif")
    ),
    Car(
        223,
        "Deepal S07",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/deepal-s07-1.3W1LHAr-_1YQPuY.avif",
        listOf("Silk Way", "Гибрид", "Кроссовер"),
        "5 орын",
        "Автомат",
        "Гибрид",
        4500,
        39000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/deepal-s07-1.3W1LHAr-_1YQPuY.avif")
    ),
    Car(
        224,
        "Jetour X90 Plus",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/jetour-x90-plus-1.CdxMqCmF_Z2i4Yp1.avif",
        listOf("Silk Way", "Кроссовер", "7 орын"),
        "7 орын",
        "Автомат",
        "Бензин AI-95",
        4500,
        39000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/jetour-x90-plus-1.CdxMqCmF_Z2i4Yp1.avif")
    ),
    Car(
        225,
        "Changan CS55",
        "Silk Way Car Rent",
        "Silk Way Car Rent · Алматы",
        "https://silkway-carrent.kz/_astro/changan-cs55-1.Ckjp8f3I_1o3SKe.avif",
        listOf("Silk Way", "Кроссовер", "Турбо"),
        "5 орын",
        "Автомат",
        "Бензин AI-95",
        4000,
        35000,
        imageUrls = listOf("https://silkway-carrent.kz/_astro/changan-cs55-1.Ckjp8f3I_1o3SKe.avif")
    )
)

fun formatTenge(amount: Int): String {
    val s = amount.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$s ₸"
}
