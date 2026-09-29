package kz.smartcarshare.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.smartcarshare.app.data.model.LocalUser

class LocalUserStore(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    private fun readAll(): MutableList<LocalUser> {
        val json = prefs.getString(KEY_USERS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<LocalUser>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun writeAll(users: List<LocalUser>) {
        prefs.edit().putString(KEY_USERS, gson.toJson(users)).apply()
    }

    fun findByEmail(email: String): LocalUser? =
        readAll().find { it.email.equals(email, ignoreCase = true) }

    fun save(user: LocalUser) {
        val users = readAll()
        val index = users.indexOfFirst { it.email.equals(user.email, ignoreCase = true) }
        if (index >= 0) {
            users[index] = user
        } else {
            users.add(user)
        }
        writeAll(users)
        setCurrentUser(user)
    }

    fun getCurrentUser(): LocalUser? {
        val email = prefs.getString(KEY_CURRENT_USER_EMAIL, null) ?: return null
        return findByEmail(email)
    }

    fun setCurrentUser(user: LocalUser?) {
        if (user == null) {
            prefs.edit().remove(KEY_CURRENT_USER_EMAIL).apply()
        } else {
            prefs.edit().putString(KEY_CURRENT_USER_EMAIL, user.email).apply()
        }
    }

    fun clearCurrentUser() {
        setCurrentUser(null)
    }

    companion object {
        private const val PREFS_NAME = "smartcarshare_users"
        private const val KEY_USERS = "users_json"
        private const val KEY_CURRENT_USER_EMAIL = "current_user_email"
    }
}
