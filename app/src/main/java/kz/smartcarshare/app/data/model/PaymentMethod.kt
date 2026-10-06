package kz.smartcarshare.app.data.model

enum class PaymentType {
    KASPI_PAY,
    VISA_MASTERCARD,
    HALYK_BANK,
    APPLE_PAY,
    GOOGLE_PAY
}

data class PaymentMethod(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: PaymentType,
    val isDefault: Boolean = false
)
