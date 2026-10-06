package kz.smartcarshare.app.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object EmailSender {
    private const val EMAILJS_URL = "https://api.emailjs.com/api/v1.0/email/send"
    private const val SERVICE_ID = "service_ff08jab"
    private const val TEMPLATE_ID = "template_fjsfzpd"
    private const val PUBLIC_KEY = "BlsfbU2JarLBhpnsl"

    suspend fun sendOtpEmail(toEmail: String, code: String, recipientName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(EMAILJS_URL)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.doOutput = true

                val jsonParam = JSONObject().apply {
                    put("service_id", SERVICE_ID)
                    put("template_id", TEMPLATE_ID)
                    put("user_id", PUBLIC_KEY)
                    put("template_params", JSONObject().apply {
                        put("email", toEmail)
                        put("to_email", toEmail)
                        put("passcode", code)
                        put("pass_code", code)
                        put("to_name", recipientName)
                        put("message", code)
                    })
                }

                conn.outputStream.use { os ->
                    os.write(jsonParam.toString().toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                Log.d("EmailSender", "EmailJS response code: $responseCode")
                if (responseCode !in 200..299) {
                    val errorBody = conn.errorStream?.bufferedReader()?.use { it.readText() }
                    Log.e("EmailSender", "EmailJS error body: $errorBody")
                    false
                } else {
                    true
                }
            } catch (e: Exception) {
                Log.e("EmailSender", "Failed to send email exception: ${e.message}")
                false
            }
        }
    }
}
