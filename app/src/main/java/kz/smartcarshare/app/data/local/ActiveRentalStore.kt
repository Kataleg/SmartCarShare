package kz.smartcarshare.app.data.local

import android.content.Context
import android.content.SharedPreferences

data class ActiveRentalState(
    val isActive: Boolean = false,
    val startTimeMillis: Long = 0L,
    val carId: Int = 0,
    val tariffName: String = "HOUR",
    val userId: String = "",
    val activeRentalId: String = ""
)

class ActiveRentalStore(context: Context) {

    private val prefs: SharedPreferences? = try {
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    } catch (_: Exception) {
        null
    }

    fun saveActiveRental(
        startTimeMillis: Long,
        carId: Int,
        tariffName: String,
        userId: String,
        activeRentalId: String = ""
    ) {
        try {
            prefs?.edit()
                ?.putBoolean(KEY_IS_ACTIVE, true)
                ?.putLong(KEY_START_TIME, startTimeMillis)
                ?.putInt(KEY_CAR_ID, carId)
                ?.putString(KEY_TARIFF, tariffName)
                ?.putString(KEY_USER_ID, userId)
                ?.putString(KEY_RENTAL_ID, activeRentalId)
                ?.apply()
        } catch (_: Exception) {}
    }

    fun getActiveRental(): ActiveRentalState {
        return try {
            val p = prefs ?: return ActiveRentalState()
            val isActive = p.getBoolean(KEY_IS_ACTIVE, false)
            if (!isActive) return ActiveRentalState()

            ActiveRentalState(
                isActive = true,
                startTimeMillis = p.getLong(KEY_START_TIME, 0L),
                carId = p.getInt(KEY_CAR_ID, 0),
                tariffName = p.getString(KEY_TARIFF, "HOUR") ?: "HOUR",
                userId = p.getString(KEY_USER_ID, "") ?: "",
                activeRentalId = p.getString(KEY_RENTAL_ID, "") ?: ""
            )
        } catch (_: Exception) {
            ActiveRentalState()
        }
    }

    fun clearActiveRental() {
        try {
            prefs?.edit()
                ?.putBoolean(KEY_IS_ACTIVE, false)
                ?.remove(KEY_START_TIME)
                ?.remove(KEY_CAR_ID)
                ?.remove(KEY_TARIFF)
                ?.remove(KEY_USER_ID)
                ?.remove(KEY_RENTAL_ID)
                ?.apply()
        } catch (_: Exception) {}
    }

    companion object {
        private const val PREFS_NAME = "smartcarshare_active_rental"
        private const val KEY_IS_ACTIVE = "is_active"
        private const val KEY_START_TIME = "start_time_millis"
        private const val KEY_CAR_ID = "car_id"
        private const val KEY_TARIFF = "tariff"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_RENTAL_ID = "rental_id"
    }
}
