package kz.smartcarshare.app.data.model

data class Company(
    val id: String,
    val name: String,
    val legalName: String = "",
    val description: String = "",
    val logoUrl: String = "",
    val websiteUrl: String = "",
    val rating: Double = 4.9,
    val is247: Boolean = true,
    val phones: List<CompanyPhone> = emptyList(),
    val locations: List<CompanyLocation> = emptyList(),
    val map2GisUrl: String = "",
    val additionalMapUrls: List<String> = emptyList(),
    val fleetCarIds: List<Int> = emptyList(),
    val tariffsInfo: List<CompanyTariffInfo> = emptyList()
)

data class CompanyPhone(
    val number: String,
    val label: String,
    val isPrimary: Boolean = false
)

data class CompanyLocation(
    val title: String,
    val address: String,
    val city: String = "Астана",
    val mapUrl: String = "",
    val isAirport: Boolean = false
)

data class CompanyTariffInfo(
    val name: String,
    val description: String,
    val minPriceTenge: Int
)
