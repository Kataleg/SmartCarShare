package kz.smartcarshare.app.data

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kz.smartcarshare.app.data.model.FirestoreCard
import kz.smartcarshare.app.data.model.FirestoreRental
import kz.smartcarshare.app.data.model.PaymentMethod
import kz.smartcarshare.app.data.model.PaymentType
import kz.smartcarshare.app.data.repository.FirestoreRentalRepository

enum class Tariff { HOUR, DAY }

class RentalViewModel(application: Application) : AndroidViewModel(application) {

    private val rentalRepository = FirestoreRentalRepository(application)

    private val customCars = mutableStateListOf<Car>()

    val cars: List<Car>
        get() = sampleCars + customCars

    var currentLang by mutableStateOf(Lang.KZ)
        private set

    var selectedPaymentMethodId by mutableStateOf("kaspi")
        private set

    var paymentMethods by mutableStateOf(
        listOf(
            PaymentMethod("kaspi", "Kaspi.kz / Kaspi Pay", "Kaspi Автоплатеж", PaymentType.KASPI_PAY, isDefault = true)
        )
    )

    private val localTripHistory = mutableStateListOf<FirestoreRental>()
    private var firestoreTripHistory by mutableStateOf<List<FirestoreRental>>(emptyList())

    val tripHistory: List<FirestoreRental>
        get() {
            val combined = (localTripHistory + firestoreTripHistory).distinctBy { it.id.ifBlank { it.startTime.toString() } }
            return combined.sortedByDescending { it.startTime }
        }

    private val ownerEarnings = mutableStateMapOf<String, Int>()

    fun getOwnerEarnings(email: String): Int = ownerEarnings[email.trim().lowercase()] ?: 0

    fun loadUserData(userId: String) {
        if (userId.isBlank()) return
        loadTripHistory(userId)
        viewModelScope.launch {
            try {
                val authUser = FirebaseAuth.getInstance().currentUser
                if (authUser != null) {
                    val doc = FirebaseFirestore.getInstance().collection("users").document(authUser.uid).get().await()
                    if (doc.exists()) {
                        @Suppress("UNCHECKED_CAST")
                        val cardsList = doc.get("paymentMethods") as? List<Map<String, Any>>
                        if (!cardsList.isNullOrEmpty()) {
                            paymentMethods = cardsList.map { map ->
                                PaymentMethod(
                                    id = map["id"] as? String ?: "card",
                                    title = map["title"] as? String ?: "Visa",
                                    subtitle = map["subtitle"] as? String ?: "",
                                    type = try { PaymentType.valueOf(map["type"] as? String ?: "VISA_MASTERCARD") } catch(_:Exception){ PaymentType.VISA_MASTERCARD },
                                    isDefault = map["isDefault"] as? Boolean ?: false
                                )
                            }
                            selectedPaymentMethodId = paymentMethods.firstOrNull { it.isDefault }?.id ?: paymentMethods.first().id
                        }
                    }
                }
            } catch (_: Exception) {}
        }
    }

    fun loadTripHistory(userId: String) {
        if (userId.isBlank()) return
        viewModelScope.launch {
            val res = rentalRepository.getRentalsForUser(userId)
            res.onSuccess { list ->
                firestoreTripHistory = list
            }
        }
    }

    private fun saveCardsToFirestore() {
        val authUser = FirebaseAuth.getInstance().currentUser
        if (authUser != null) {
            val firestoreCards = paymentMethods.map {
                FirestoreCard(
                    id = it.id,
                    title = it.title,
                    subtitle = it.subtitle,
                    type = it.type.name,
                    isDefault = it.isDefault
                )
            }
            FirebaseFirestore.getInstance().collection("users")
                .document(authUser.uid)
                .set(mapOf("paymentMethods" to firestoreCards), SetOptions.merge())
        }
    }

    fun addCustomCar(
        name: String,
        pricePerHour: Int,
        pricePerDay: Int?,
        location: String,
        imageUrl: String,
        ownerName: String,
        ownerEmail: String,
        ownerKaspiNumber: String = ""
    ) {
        val newId = cars.maxOfOrNull { it.id }?.plus(1) ?: 100
        val newCar = Car(
            id = newId,
            name = name,
            company = "Жеке иесі: $ownerName",
            location = location.ifBlank { "Астана" },
            imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1590362891991-f776e747a588?q=80&w=800" },
            tags = listOf("Жеке авто", "P2P", "Каршеринг"),
            seats = "5 орын",
            transmission = "Автомат",
            fuel = "Бензин AI-95",
            pricePerHour = pricePerHour,
            pricePerDay = pricePerDay,
            isLux = false,
            ownerEmail = ownerEmail.trim().lowercase(),
            ownerName = ownerName,
            ownerKaspiNumber = ownerKaspiNumber.ifBlank { "+7 (777) 000-00-00" }
        )
        customCars.add(newCar)
    }

    val selectedPaymentMethod: PaymentMethod
        get() = paymentMethods.firstOrNull { it.id == selectedPaymentMethodId } ?: paymentMethods.first()

    fun selectPaymentMethod(id: String) {
        selectedPaymentMethodId = id
        paymentMethods = paymentMethods.map { it.copy(isDefault = (it.id == id)) }
        saveCardsToFirestore()
    }

    fun addCard(number: String, holder: String) {
        val digitsOnly = number.filter { it.isDigit() }
        val last4 = if (digitsOnly.length >= 4) digitsOnly.takeLast(4) else "0000"
        val newId = "card_${System.currentTimeMillis()}"
        val newCard = PaymentMethod(
            id = newId,
            title = "Visa •••• $last4",
            subtitle = holder.ifBlank { "Карта иесі" },
            type = PaymentType.VISA_MASTERCARD,
            isDefault = true
        )
        paymentMethods = paymentMethods.map { it.copy(isDefault = false) } + newCard
        selectedPaymentMethodId = newId
        saveCardsToFirestore()
    }

    fun deleteCard(id: String) {
        if (paymentMethods.size <= 1) return
        val updated = paymentMethods.filterNot { it.id == id }
        paymentMethods = updated
        if (selectedPaymentMethodId == id) {
            selectedPaymentMethodId = updated.first().id
        }
        saveCardsToFirestore()
    }

    var selectedCarId by mutableStateOf<Int?>(null)
        private set
    var tariff by mutableStateOf(Tariff.HOUR)
        private set
    var insuranceOn by mutableStateOf(false)
    var autopayOn by mutableStateOf(true)
    var elapsedSeconds by mutableStateOf(0)
        private set
    var rentalActive by mutableStateOf(false)
        private set
    var rating by mutableStateOf(0)

    var frozenFinalPrice by mutableStateOf<Int?>(null)
        private set

    private var activeRentalId: String? = null
    private var timerJob: Job? = null

    val selectedCar: Car?
        get() = cars.firstOrNull { it.id == selectedCarId }

    fun setLanguage(lang: Lang) {
        currentLang = lang
    }

    fun toggleLanguage() {
        currentLang = if (currentLang == Lang.KZ) Lang.RU else Lang.KZ
    }

    fun selectCar(id: Int) {
        selectedCarId = id
        tariff = Tariff.HOUR
        insuranceOn = false
    }

    fun changeTariff(t: Tariff) {
        val car = selectedCar ?: return
        if (t == Tariff.DAY && car.pricePerDay == null) return
        tariff = t
    }

    fun currentPrice(): Int {
        val car = selectedCar ?: return 0
        return if (tariff == Tariff.HOUR) car.pricePerHour else (car.pricePerDay ?: car.pricePerHour)
    }

    fun startRental(userId: String = "guest_user") {
        elapsedSeconds = 0
        frozenFinalPrice = null
        rentalActive = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val car = selectedCar
            if (car != null) {
                val firestoreRental = FirestoreRental(
                    userId = userId,
                    carId = car.id,
                    carName = car.name,
                    startTime = System.currentTimeMillis(),
                    status = "ACTIVE"
                )
                val res = rentalRepository.saveRental(firestoreRental)
                activeRentalId = res.getOrNull()
                loadUserData(userId)
            }

            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    fun stopTripAccrual() {
        timerJob?.cancel()
        if (frozenFinalPrice == null) {
            frozenFinalPrice = liveAccruedPrice()
        }
    }

    fun liveAccruedPrice(): Int {
        val frozen = frozenFinalPrice
        if (frozen != null) return frozen

        val base = currentPrice()
        val divisor = if (tariff == Tariff.HOUR) 3600.0 else 86400.0
        return ((base / divisor) * 60.0 * elapsedSeconds).toInt()
    }

    fun finishRental(userId: String = "guest_user") {
        stopTripAccrual()
        rentalActive = false
        val rId = activeRentalId
        val finalPrice = liveAccruedPrice()
        val car = selectedCar

        val completedRental = FirestoreRental(
            id = rId ?: "rental_${System.currentTimeMillis()}",
            userId = userId,
            carId = car?.id ?: 0,
            carName = car?.name ?: "Көлік",
            startTime = System.currentTimeMillis() - (elapsedSeconds * 1000L),
            endTime = System.currentTimeMillis(),
            totalPrice = finalPrice,
            status = "FINISHED"
        )
        localTripHistory.add(0, completedRental)

        if (car != null && car.ownerEmail.isNotBlank()) {
            val key = car.ownerEmail.trim().lowercase()
            val current = ownerEarnings[key] ?: 0
            ownerEarnings[key] = current + finalPrice
        }

        if (rId != null) {
            viewModelScope.launch {
                try {
                    rentalRepository.finishRental(rId, finalPrice)
                    loadUserData(userId)
                } catch (_: Exception) {}
            }
        }
    }

    fun elapsedFormatted(): String {
        val m = (elapsedSeconds / 60).toString().padStart(2, '0')
        val s = (elapsedSeconds % 60).toString().padStart(2, '0')
        return "$m:$s"
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
