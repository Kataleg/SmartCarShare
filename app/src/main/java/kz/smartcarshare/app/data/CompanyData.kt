package kz.smartcarshare.app.data

import kz.smartcarshare.app.data.model.Company
import kz.smartcarshare.app.data.model.CompanyLocation
import kz.smartcarshare.app.data.model.CompanyPhone
import kz.smartcarshare.app.data.model.CompanyTariffInfo

object CompanyData {
    val cashAutoRent = Company(
        id = "cash_auto_rent",
        name = "Cash Auto Rent",
        legalName = "Cash Auto Rent (24/7)",
        description = "Крупнейший каршеринг и автопрокат в Астане. Автопарк из 50+ авто: эконом, комфорт, бизнес, внедорожники, минивэны и мотоциклы. Круглосуточная выдача в Аэропорту Т1 и филиале на Жайдарман 2/1.",
        logoUrl = "https://cashauto.kz/uploads/uploads/images/cache/venicles/medium_default-cmtfpmmwl0unuqwjl9bol81fm.jpeg",
        websiteUrl = "https://cashauto.kz/ru/",
        rating = 4.95,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (777) 999-11-12",
                label = "Аэропорт Т1 (24/7)",
                isPrimary = true
            ),
            CompanyPhone(
                number = "+7 (702) 998-11-12",
                label = "ул. Жайдарман 2/1 (09:00–21:00)",
                isPrimary = false
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Аэропорт Т1",
                address = "Международный аэропорт Нурсултан Назарбаев, Терминал 1",
                city = "Астана",
                mapUrl = "https://2gis.kz/astana/geo/70000001077229590",
                isAirport = true
            ),
            CompanyLocation(
                title = "Филиал Жайдарман",
                address = "ул. Жайдарман 2/1",
                city = "Астана",
                mapUrl = "https://2gis.kz/astana/geo/70000001054775158",
                isAirport = false
            )
        ),
        map2GisUrl = "https://2gis.kz/astana/geo/70000001077229590",
        additionalMapUrls = listOf(
            "https://2gis.kz/astana/geo/70000001077229590",
            "https://2gis.kz/astana/geo/70000001054775158"
        ),
        fleetCarIds = (1..51).toList(),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда с почасовой оплатой от 1 700 ₸/час",
                minPriceTenge = 1700
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Выгодный тариф на 24 часа от 6 500 ₸/сутки",
                minPriceTenge = 6500
            ),
            CompanyTariffInfo(
                name = "B2B Корпоративный",
                description = "Абонементы для компаний от 280 000 ₸/месяц",
                minPriceTenge = 280000
            )
        )
    )

    val nurAutoRent = Company(
        id = "nur_auto_rent",
        name = "Nur Auto Rent",
        legalName = "Nur Auto Rent (Астана)",
        description = "Аренда и автопрокат комфортабельных автомобилей в Астане. Надежные машины для бизнеса, поездок по городу и межгороду.",
        logoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        websiteUrl = "https://nurautorent.kz/#contacts",
        rating = 4.85,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (775) 437-47-74",
                label = "Nur Auto Rent (Основной)",
                isPrimary = true
            ),
            CompanyPhone(
                number = "+7 (775) 888-08-96",
                label = "Диспетчер",
                isPrimary = false
            ),
            CompanyPhone(
                number = "+7 (778) 120-02-27",
                label = "Отдел бронирования",
                isPrimary = false
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Филиал Nur Auto Rent",
                address = "Астана, Nur Auto Rent",
                city = "Астана",
                mapUrl = "https://go.2gis.com/0U92Q",
                isAirport = false
            )
        ),
        map2GisUrl = "https://go.2gis.com/0U92Q",
        additionalMapUrls = listOf("https://go.2gis.com/0U92Q"),
        fleetCarIds = listOf(52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда от 2 000 ₸/час",
                minPriceTenge = 2000
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Суточная аренда от 12 000 ₸/сутки",
                minPriceTenge = 12000
            )
        )
    )

    val regionRent = Company(
        id = "region_rent",
        name = "Region Rent",
        legalName = "Region Rent (Астана)",
        description = "Аренда и прокат автомобилей любого класса в Астане. Надежные машины, прозрачные условия и оперативная доставка.",
        logoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        websiteUrl = "https://region.rent/astana",
        rating = 4.90,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (701) 909-90-09",
                label = "Region Rent (Основной)",
                isPrimary = true
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Филиал Region Rent",
                address = "Астана, Region Rent",
                city = "Астана",
                mapUrl = "https://go.2gis.com/nYybI",
                isAirport = false
            )
        ),
        map2GisUrl = "https://go.2gis.com/nYybI",
        additionalMapUrls = listOf("https://go.2gis.com/nYybI"),
        fleetCarIds = listOf(67, 68, 69, 70, 71, 72, 73, 74, 75, 76),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда от 2 500 ₸/час",
                minPriceTenge = 2500
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Суточная аренда от 15 000 ₸/сутки",
                minPriceTenge = 15000
            )
        )
    )

    val qazqar = Company(
        id = "qazqar",
        name = "QazQar",
        legalName = "QazQar (Астана)",
        description = "Современный автопрокат и каршеринг автомобилей в Астане. Большой выбор авто от эконома до бизнес-класса.",
        logoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        websiteUrl = "https://qazqar.kz/",
        rating = 4.88,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (705) 924-66-44",
                label = "QazQar (Основной)",
                isPrimary = true
            ),
            CompanyPhone(
                number = "+7 (776) 350-41-41",
                label = "Диспетчер",
                isPrimary = false
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Филиал QazQar",
                address = "Астана, QazQar",
                city = "Астана",
                mapUrl = "https://go.2gis.com/2luPM",
                isAirport = false
            )
        ),
        map2GisUrl = "https://go.2gis.com/2luPM",
        additionalMapUrls = listOf("https://go.2gis.com/2luPM"),
        fleetCarIds = listOf(77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда от 2 000 ₸/час",
                minPriceTenge = 2000
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Суточная аренда от 15 000 ₸/сутки",
                minPriceTenge = 15000
            )
        )
    )

    val azvMotors = Company(
        id = "azv_motors",
        name = "AZV MOTORS",
        legalName = "AZV MOTORS (Алматы)",
        description = "Прокат и аренда автомобилей в Алматы от AZV MOTORS. Надежный сервис, широкий модельный ряд и выгодные условия.",
        logoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        websiteUrl = "https://azvmotors.kz/",
        rating = 4.90,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (706) 607-00-06",
                label = "AZV MOTORS (Основной)",
                isPrimary = true
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Филиал AZV MOTORS",
                address = "Алматы, AZV MOTORS",
                city = "Алматы",
                mapUrl = "https://2gis.kz/almaty/geo/70000001103541428",
                isAirport = false
            )
        ),
        map2GisUrl = "https://2gis.kz/almaty/geo/70000001103541428",
        additionalMapUrls = listOf("https://2gis.kz/almaty/geo/70000001103541428"),
        fleetCarIds = (93..162).toList(),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда от 2 500 ₸/час",
                minPriceTenge = 2500
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Суточная аренда от 20 000 ₸/сутки",
                minPriceTenge = 20000
            )
        )
    )

    val sodex = Company(
        id = "sodex",
        name = "Sodex",
        legalName = "Sodex (Алматы)",
        description = "Аренда и автопрокат комфортабельных автомобилей в Алматы от компании Sodex. Прозрачные условия и отличный выбор автомобилей.",
        logoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        websiteUrl = "http://sodex.kz/",
        rating = 4.89,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (701) 527-50-05",
                label = "Sodex (Основной)",
                isPrimary = true
            ),
            CompanyPhone(
                number = "+7 (747) 527-50-05",
                label = "Диспетчер",
                isPrimary = false
            ),
            CompanyPhone(
                number = "+7 (707) 700-81-61",
                label = "Бронирование",
                isPrimary = false
            ),
            CompanyPhone(
                number = "+7 (701) 707-81-61",
                label = "Поддержка",
                isPrimary = false
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Филиал Sodex",
                address = "Алматы, Sodex",
                city = "Алматы",
                mapUrl = "https://2gis.kz/almaty/geo/70000001040231623",
                isAirport = false
            )
        ),
        map2GisUrl = "https://2gis.kz/almaty/geo/70000001040231623",
        additionalMapUrls = listOf("https://2gis.kz/almaty/geo/70000001040231623"),
        fleetCarIds = listOf(163, 164, 165, 166, 167, 168),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда от 2 000 ₸/час",
                minPriceTenge = 2000
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Суточная аренда от 15 000 ₸/сутки",
                minPriceTenge = 15000
            )
        )
    )

    val rentCarsKz = Company(
        id = "rent_cars_kz",
        name = "Rent Cars Kz",
        legalName = "Rent Cars Kz (Алматы)",
        description = "Аренда и автопрокат автомобилей любого класса в Алматы от компании Rent Cars Kz. Качественные автомобили и выгодные цены.",
        logoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        websiteUrl = "http://rent-cars.kz/",
        rating = 4.91,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (707) 834-40-75",
                label = "Rent Cars Kz (Основной)",
                isPrimary = true
            ),
            CompanyPhone(
                number = "+7 (707) 834-40-76",
                label = "Диспетчер",
                isPrimary = false
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Филиал Rent Cars Kz",
                address = "Алматы, Rent Cars Kz",
                city = "Алматы",
                mapUrl = "https://2gis.kz/almaty/geo/70000001026536568",
                isAirport = false
            )
        ),
        map2GisUrl = "https://2gis.kz/almaty/geo/70000001026536568",
        additionalMapUrls = listOf("https://2gis.kz/almaty/geo/70000001026536568"),
        fleetCarIds = (169..182).toList(),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда от 2 000 ₸/час",
                minPriceTenge = 2000
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Суточная аренда от 15 000 ₸/сутки",
                minPriceTenge = 15000
            )
        )
    )

    val kazprokat = Company(
        id = "kazprokat",
        name = "Kazprokat",
        legalName = "Kazprokat (Алматы)",
        description = "Автопрокат и аренда автомобилей в Алматы от компании Kazprokat. Надежные машины, прозрачные условия и отличный сервис.",
        logoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        websiteUrl = "http://www.kazprokat.kz/",
        rating = 4.88,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (777) 888-08-88",
                label = "Kazprokat (Основной)",
                isPrimary = true
            ),
            CompanyPhone(
                number = "+7 (707) 773-77-73",
                label = "Диспетчер",
                isPrimary = false
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Филиал Kazprokat",
                address = "Алматы, Kazprokat",
                city = "Алматы",
                mapUrl = "https://2gis.kz/almaty/geo/9429940001203296",
                isAirport = false
            )
        ),
        map2GisUrl = "https://2gis.kz/almaty/geo/9429940001203296",
        additionalMapUrls = listOf("https://2gis.kz/almaty/geo/9429940001203296"),
        fleetCarIds = (183..199).toList(),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда от 2 000 ₸/час",
                minPriceTenge = 2000
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Суточная аренда от 15 000 ₸/сутки",
                minPriceTenge = 15000
            )
        )
    )

    val silkWayCarRent = Company(
        id = "silk_way_car_rent",
        name = "Silk Way Car Rent",
        legalName = "Silk Way Car Rent (Алматы)",
        description = "Аренда и автопрокат премиальных и комфортабельных автомобилей в Алматы от Silk Way Car Rent. Комфортный сервис и гибкие условия.",
        logoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=800",
        websiteUrl = "http://www.silkway-carrent.kz/",
        rating = 4.90,
        is247 = true,
        phones = listOf(
            CompanyPhone(
                number = "+7 (747) 301-72-70",
                label = "Silk Way (Основной)",
                isPrimary = true
            ),
            CompanyPhone(
                number = "+7 (707) 711-22-33",
                label = "Диспетчер",
                isPrimary = false
            )
        ),
        locations = listOf(
            CompanyLocation(
                title = "Филиал Silk Way Car Rent",
                address = "Алматы, Silk Way Car Rent",
                city = "Алматы",
                mapUrl = "https://2gis.kz/almaty/geo/70000001077509396",
                isAirport = false
            )
        ),
        map2GisUrl = "https://2gis.kz/almaty/geo/70000001077509396",
        additionalMapUrls = listOf("https://2gis.kz/almaty/geo/70000001077509396"),
        fleetCarIds = (200..225).toList(),
        tariffsInfo = listOf(
            CompanyTariffInfo(
                name = "Почасовой",
                description = "Аренда от 2 000 ₸/час",
                minPriceTenge = 2000
            ),
            CompanyTariffInfo(
                name = "Суточный",
                description = "Суточная аренда от 15 000 ₸/сутки",
                minPriceTenge = 15000
            )
        )
    )

    val cashAutoRentAstana = cashAutoRent

    val allCompanies = listOf(cashAutoRent, nurAutoRent, regionRent, qazqar, azvMotors, sodex, rentCarsKz, kazprokat, silkWayCarRent)

    fun getCompanyById(id: String): Company? {
        return allCompanies.find { it.id == id }
    }
}
