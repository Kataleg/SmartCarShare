package kz.smartcarshare.app.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import kz.smartcarshare.app.data.local.LocalUserStore
import kz.smartcarshare.app.data.model.FirestoreUser
import kz.smartcarshare.app.data.model.LocalUser

class AuthRepository(private val context: Context) {

    private val localUserStore = LocalUserStore(context)

    private val isFirebaseAvailable: Boolean
        get() = try {
            FirebaseApp.initializeApp(context)
            FirebaseAuth.getInstance()
            true
        } catch (e: Exception) {
            false
        }

    suspend fun register(
        email: String,
        password: String,
        name: String,
        phone: String
    ): Result<LocalUser> {
        if (!isFirebaseAvailable) {
            return registerLocal(email, password, name, phone)
        }

        return try {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("Пайдаланушы жасау сәтсіз аяқталды"))

            try {
                firebaseUser.sendEmailVerification().await()
            } catch (e: Exception) {
                Log.e("AuthRepository", "Failed to send email verification: ${e.message}")
            }

            val firestoreUser = FirestoreUser(
                uid = firebaseUser.uid,
                email = email,
                name = name,
                phone = phone,
                isEmailVerified = firebaseUser.isEmailVerified
            )

            withTimeoutOrNull(2500) {
                try {
                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .set(firestoreUser)
                        .await()
                } catch (e: Exception) {
                    Log.e("AuthRepository", "Failed to save user to Firestore: ${e.message}")
                }
            }

            val localUser = LocalUser(
                email = email,
                passwordHash = "",
                name = name,
                phone = phone
            )
            localUserStore.save(localUser)
            Result.success(localUser)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Firebase register error: ${e.message}")
            Result.failure(Exception(e.localizedMessage ?: e.message ?: "Firebase тіркелу қатесі"))
        }
    }

    suspend fun login(email: String, password: String): Result<LocalUser> {
        if (!isFirebaseAvailable) {
            return loginLocal(email, password)
        }

        return try {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()

            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("Жүйеге кіру сәтсіз аяқталды"))

            withTimeoutOrNull(2000) {
                try {
                    firebaseUser.reload().await()
                } catch (_: Exception) {}
            }

            var name = ""
            var phone = ""

            withTimeoutOrNull(2500) {
                try {
                    val doc = firestore.collection("users").document(firebaseUser.uid).get().await()
                    if (doc.exists()) {
                        name = doc.getString("name") ?: ""
                        phone = doc.getString("phone") ?: ""
                    }
                } catch (_: Exception) {}
            }

            val localUser = LocalUser(
                email = email,
                passwordHash = "",
                name = name.ifBlank { firebaseUser.displayName ?: email.substringBefore("@") },
                phone = phone
            )
            localUserStore.save(localUser)
            Result.success(localUser)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Firebase login error: ${e.message}")
            Result.failure(Exception(e.localizedMessage ?: e.message ?: "Firebase кіру қатесі"))
        }
    }

    suspend fun updateUserProfile(
        name: String,
        phone: String,
        email: String
    ): Result<LocalUser> {
        val currentUser = localUserStore.getCurrentUser()
            ?: return Result.failure(Exception("Пайдаланушы авторизацияланбаған"))

        val targetEmail = email.ifBlank { currentUser.email }
        val updatedName = name.ifBlank { currentUser.name }
        val updatedPhone = phone.ifBlank { currentUser.phone }

        if (isFirebaseAvailable) {
            try {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val firestore = FirebaseFirestore.getInstance()

                if (firebaseUser != null) {
                    if (targetEmail.isNotBlank() && !targetEmail.equals(firebaseUser.email, ignoreCase = true)) {
                        try {
                            firebaseUser.updateEmail(targetEmail).await()
                        } catch (e: Exception) {
                            Log.e("AuthRepository", "Failed to update email in Firebase Auth: ${e.message}")
                        }
                    }

                    val updates = mapOf(
                        "name" to updatedName,
                        "phone" to updatedPhone,
                        "email" to targetEmail
                    )

                    withTimeoutOrNull(2500) {
                        try {
                            firestore.collection("users")
                                .document(firebaseUser.uid)
                                .set(updates, SetOptions.merge())
                                .await()
                        } catch (e: Exception) {
                            Log.e("AuthRepository", "Failed to save user to Firestore: ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthRepository", "Failed to update profile in Firebase: ${e.message}")
            }
        }

        val updatedUser = LocalUser(
            email = targetEmail,
            passwordHash = currentUser.passwordHash,
            name = updatedName,
            phone = updatedPhone
        )
        localUserStore.save(updatedUser)
        return Result.success(updatedUser)
    }

    suspend fun sendEmailVerification(): Result<Unit> {
        if (!isFirebaseAvailable) return Result.success(Unit)
        return try {
            val user = FirebaseAuth.getInstance().currentUser
            user?.sendEmailVerification()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        if (!isFirebaseAvailable) return Result.failure(Exception("Firebase недоступен"))
        return try {
            val auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Firebase password reset error: ${e.message}")
            val msg = when {
                e.message?.contains("no user record", ignoreCase = true) == true ||
                e.message?.contains("user-not-found", ignoreCase = true) == true ->
                    "Бұл email бойынша аккаунт табылмады. Алдымен «Тіркелу» арқылы тіркеліңіз."
                else -> e.localizedMessage ?: e.message ?: "Сброс пароля не удался"
            }
            Result.failure(Exception(msg))
        }
    }

    fun isEmailVerified(): Boolean {
        if (!isFirebaseAvailable) return true
        val user = FirebaseAuth.getInstance().currentUser ?: return true
        return user.isEmailVerified
    }

    fun getCurrentUser(): LocalUser? = localUserStore.getCurrentUser()

    fun logout() {
        if (isFirebaseAvailable) {
            try {
                FirebaseAuth.getInstance().signOut()
            } catch (_: Exception) {}
        }
        localUserStore.clearCurrentUser()
    }

    private fun registerLocal(
        email: String,
        password: String,
        name: String,
        phone: String
    ): Result<LocalUser> {
        if (localUserStore.findByEmail(email) != null) {
            return Result.failure(Exception("Бұл email арқылы пайдаланушы тіркеліп қойған"))
        }

        val user = LocalUser(
            email = email,
            passwordHash = password,
            name = name,
            phone = phone
        )
        localUserStore.save(user)
        return Result.success(user)
    }

    private fun loginLocal(email: String, password: String): Result<LocalUser> {
        val user = localUserStore.findByEmail(email)
            ?: return Result.failure(Exception("Пайдаланушы табылмады"))

        return if (user.passwordHash == password || user.passwordHash.isEmpty()) {
            localUserStore.setCurrentUser(user)
            Result.success(user)
        } else {
            Result.failure(Exception("Құпия сөз қате"))
        }
    }
}
