package kz.smartcarshare.app.data.model

data class FirestoreUser(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val avatarUrl: String = "",
    val isEmailVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class FirestoreRental(
    val id: String = "",
    val userId: String = "",
    val carId: Int = 0,
    val carName: String = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val totalPrice: Int = 0,
    val status: String = "ACTIVE"
)

data class FirestoreCompany(
    val id: String = "",
    val name: String = "",
    val legalName: String = "",
    val description: String = "",
    val logoUrl: String = "",
    val rating: Double = 4.9,
    val is247: Boolean = true,
    val map2GisUrl: String = "",
    val phones: List<Map<String, String>> = emptyList(),
    val locations: List<Map<String, String>> = emptyList()
)
