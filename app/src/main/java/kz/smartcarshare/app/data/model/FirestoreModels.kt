package kz.smartcarshare.app.data.model

data class FirestoreCard(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val type: String = "VISA_MASTERCARD",
    val isDefault: Boolean = false
)

data class FirestoreUser(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val isEmailVerified: Boolean = false,
    val paymentMethods: List<FirestoreCard> = emptyList(),
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
