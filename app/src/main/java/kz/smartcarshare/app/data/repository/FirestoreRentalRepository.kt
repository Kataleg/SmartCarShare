package kz.smartcarshare.app.data.repository

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kz.smartcarshare.app.data.model.FirestoreRental

class FirestoreRentalRepository(private val context: Context) {

    private val isFirebaseAvailable: Boolean
        get() = try {
            FirebaseApp.initializeApp(context)
            FirebaseAuth.getInstance()
            true
        } catch (_: Exception) {
            false
        }

    suspend fun saveRental(rental: FirestoreRental): Result<String> {
        if (!isFirebaseAvailable) return Result.success("local_rental_${System.currentTimeMillis()}")

        return try {
            val firestore = FirebaseFirestore.getInstance()
            val docRef = firestore.collection("rentals").document()
            val rentalWithId = rental.copy(id = docRef.id)
            docRef.set(rentalWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun finishRental(rentalId: String, totalPrice: Int): Result<Unit> {
        if (!isFirebaseAvailable) return Result.success(Unit)

        return try {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("rentals")
                .document(rentalId)
                .update(
                    mapOf(
                        "endTime" to System.currentTimeMillis(),
                        "totalPrice" to totalPrice,
                        "status" to "FINISHED"
                    )
                ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRentalsForUser(userId: String): Result<List<FirestoreRental>> {
        if (!isFirebaseAvailable) return Result.success(emptyList())

        return try {
            val firestore = FirebaseFirestore.getInstance()
            val snapshot = firestore.collection("rentals")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(FirestoreRental::class.java)
            }.sortedByDescending { it.startTime }

            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
