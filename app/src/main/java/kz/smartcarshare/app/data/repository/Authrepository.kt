package kz.smartcarshare.app.data.repository

import android.content.Context
import kz.smartcarshare.app.data.local.LocalUserStore
import kz.smartcarshare.app.data.local.PasswordHasher
import kz.smartcarshare.app.data.model.LocalUser

class AuthRepository(context: Context) {

    private val userStore = LocalUserStore(context)

    fun register(email: String, password: String, name: String, phone: String): Result<LocalUser> {
        if (userStore.findByEmail(email) != null) {
            return Result.failure(Exception("Бұл email арқылы пайдаланушы тіркеліп қойған"))
        }

        val user = LocalUser(
            email = email,
            passwordHash = PasswordHasher.hash(password),
            name = name,
            phone = phone
        )
        userStore.save(user)
        userStore.setCurrentUser(user)
        return Result.success(user)
    }

    fun login(email: String, password: String): Result<LocalUser> {
        val user = userStore.findByEmail(email)
            ?: return Result.failure(Exception("Пайдаланушы табылмады"))

        return if (user.passwordHash == PasswordHasher.hash(password)) {
            userStore.setCurrentUser(user)
            Result.success(user)
        } else {
            Result.failure(Exception("Құпия сөз қате"))
        }
    }

    fun getCurrentUser(): LocalUser? = userStore.getCurrentUser()

    fun logout() {
        userStore.clearCurrentUser()
    }
}
