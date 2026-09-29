package kz.smartcarshare.app.data.model

data class LocalUser(
    val email: String,
    val passwordHash: String,
    val name: String,
    val phone: String
)

