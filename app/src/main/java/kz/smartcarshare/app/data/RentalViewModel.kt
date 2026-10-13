package kz.smartcarshare.app.data

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.smartcarshare.app.data.model.FirestoreRental
import kz.smartcarshare.app.data.repository.FirestoreRentalRepository

enum class Tariff { HOUR, DAY }

class RentalViewModel(application: Application) : AndroidViewModel(application) {

    private val rentalRepository = FirestoreRentalRepository(application)

    private val customCars = mutableStateListOf<Car>()

    val cars: List<Car>
        get() = sampleCars + customCars

    var currentLang by mutableStateOf(Lang.KZ)
        private set

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

    var selectedCarId by mutableStateOf<Int?>(null)
        private set
    var tariff by mutableStateOf(Tariff.HOUR)
        private set
    var insuranceOn by mutableStateOf(false)
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
