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
enum class SortOrder { NONE, PRICE_ASC, PRICE_DESC }

class RentalViewModel(application: Application) : AndroidViewModel(application) {

    private val rentalRepository = FirestoreRentalRepository(application)

    private val customCars = mutableStateListOf<Car>()

    val cars: List<Car>
        get() = sampleCars + customCars

    var currentLang by mutableStateOf(Lang.KZ)
        private set

    var searchQuery by mutableStateOf("")
    var selectedCategory by mutableStateOf("Барлығы")
    var selectedSeatsFilter by mutableStateOf("Барлығы")
    var selectedBrand by mutableStateOf("Барлығы")
    var selectedBodyType by mutableStateOf("Барлығы")
    var selectedClass by mutableStateOf("Барлығы")
    var sortOrder by mutableStateOf(SortOrder.NONE)
    var isFilterDialogVisible by mutableStateOf(false)

    val allLabel: String
        get() = if (currentLang == Lang.KZ) "Барлығы" else "Все"

    val categories: List<String>
        get() = listOf(allLabel, "CashAuto", "Мото", "Эконом", "Кроссовер", "Бизнес", "Минивэн", "Lux")

    private fun matchesCat(car: Car, cat: String): Boolean = when (cat) {
        "CashAuto" -> car.company.contains("CashAuto", ignoreCase = true)
        "Мото" -> car.tags.contains("Мото")
        "Эконом" -> car.tags.contains("Эконом")
        "Кроссовер" -> car.tags.contains("Кроссовер")
        "Бизнес" -> car.tags.contains("Бизнес") || car.tags.contains("Седан")
        "Минивэн" -> car.tags.contains("Минивэн")
        "Lux" -> car.isLux || car.tags.contains("Lux")
        else -> true
    }

    private fun matchesBr(car: Car, brand: String): Boolean =
        car.name.startsWith(brand, ignoreCase = true) || car.name.contains(brand, ignoreCase = true)

    private fun matchesBody(car: Car, body: String): Boolean =
        car.tags.any { it.contains(body, ignoreCase = true) } || car.name.contains(body, ignoreCase = true)

    private fun matchesCls(car: Car, cls: String): Boolean =
        if (cls == "Lux") car.isLux || car.tags.contains("Lux") else car.tags.any { it.contains(cls, ignoreCase = true) }

    private fun matchesSeat(car: Car, seat: String): Boolean = when (seat) {
        "2 орын", "2 места" -> car.seats.contains("2")
        "4–5 орын", "4-5 мест", "4–5 мест" -> car.seats.contains("4") || car.seats.contains("5")
        "7+ орын", "7+ мест" -> car.seats.contains("6") || car.seats.contains("7") || car.seats.contains("8") || car.seats.contains("11")
        else -> true
    }

    val brands: List<String>
        get() {
            val matching = cars.filter { car ->
                val matchesCategory = selectedCategory == "Барлығы" || selectedCategory == "Все" || matchesCat(car, selectedCategory)
                val matchesBodyType = selectedBodyType == "Барлығы" || selectedBodyType == "Все" || matchesBody(car, selectedBodyType)
                val matchesClass = selectedClass == "Барлығы" || selectedClass == "Все" || matchesCls(car, selectedClass)
                val matchesSeats = selectedSeatsFilter == "Барлығы" || selectedSeatsFilter == "Все" || matchesSeat(car, selectedSeatsFilter)
                matchesCategory && matchesBodyType && matchesClass && matchesSeats
            }
            val extracted = matching.map { it.name.trim().substringBefore(" ") }.distinct().filter { it.isNotBlank() }.sorted()
            return listOf(allLabel) + extracted
        }

    val bodyTypes: List<String>
        get() {
            val matching = cars.filter { car ->
                val matchesCategory = selectedCategory == "Барлығы" || selectedCategory == "Все" || matchesCat(car, selectedCategory)
                val matchesBrand = selectedBrand == "Барлығы" || selectedBrand == "Все" || matchesBr(car, selectedBrand)
                val matchesClass = selectedClass == "Барлығы" || selectedClass == "Все" || matchesCls(car, selectedClass)
                val matchesSeats = selectedSeatsFilter == "Барлығы" || selectedSeatsFilter == "Все" || matchesSeat(car, selectedSeatsFilter)
                matchesCategory && matchesBrand && matchesClass && matchesSeats
            }
            val allTypes = listOf("Седан", "Хэтчбек", "Кроссовер", "Внедорожник", "Кабриолет", "Пикап", "Минивэн", "Мото")
            val available = allTypes.filter { type ->
                matching.any { car -> matchesBody(car, type) }
            }
            return listOf(allLabel) + available
        }

    val vehicleClasses: List<String>
        get() {
            val matching = cars.filter { car ->
                val matchesCategory = selectedCategory == "Барлығы" || selectedCategory == "Все" || matchesCat(car, selectedCategory)
                val matchesBrand = selectedBrand == "Барлығы" || selectedBrand == "Все" || matchesBr(car, selectedBrand)
                val matchesBodyType = selectedBodyType == "Барлығы" || selectedBodyType == "Все" || matchesBody(car, selectedBodyType)
                val matchesSeats = selectedSeatsFilter == "Барлығы" || selectedSeatsFilter == "Все" || matchesSeat(car, selectedSeatsFilter)
                matchesCategory && matchesBrand && matchesBodyType && matchesSeats
            }
            val allClasses = listOf("Эконом", "Комфорт", "Комфорт+", "Бизнес", "Lux")
            val available = allClasses.filter { cls ->
                matching.any { car -> matchesCls(car, cls) }
            }
            return listOf(allLabel) + available
        }

    val filteredCars: List<Car>
        get() {
            val list = cars.filter { car ->
                val matchesSearch = searchQuery.isBlank() ||
                    car.name.contains(searchQuery, ignoreCase = true) ||
                    car.company.contains(searchQuery, ignoreCase = true) ||
                    car.tags.any { it.contains(searchQuery, ignoreCase = true) }

                val matchesCategory = selectedCategory == "Барлығы" || selectedCategory == "Все" || matchesCat(car, selectedCategory)
                val matchesSeats = selectedSeatsFilter == "Барлығы" || selectedSeatsFilter == "Все" || matchesSeat(car, selectedSeatsFilter)
                val matchesBrand = selectedBrand == "Барлығы" || selectedBrand == "Все" || matchesBr(car, selectedBrand)
                val matchesBodyType = selectedBodyType == "Барлығы" || selectedBodyType == "Все" || matchesBody(car, selectedBodyType)
                val matchesClass = selectedClass == "Барлығы" || selectedClass == "Все" || matchesCls(car, selectedClass)

                matchesSearch && matchesCategory && matchesSeats && matchesBrand && matchesBodyType && matchesClass
            }

            return when (sortOrder) {
                SortOrder.PRICE_ASC -> list.sortedBy { it.pricePerHour }
                SortOrder.PRICE_DESC -> list.sortedByDescending { it.pricePerHour }
                SortOrder.NONE -> list
            }
        }

    fun resetFilters() {
        searchQuery = ""
        selectedCategory = allLabel
        selectedSeatsFilter = allLabel
        selectedBrand = allLabel
        selectedBodyType = allLabel
        selectedClass = allLabel
        sortOrder = SortOrder.NONE
    }

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
        val oldAll = allLabel
        currentLang = lang
        val newAll = allLabel
        if (selectedCategory == oldAll) selectedCategory = newAll
        if (selectedSeatsFilter == oldAll) selectedSeatsFilter = newAll
        if (selectedBrand == oldAll) selectedBrand = newAll
        if (selectedBodyType == oldAll) selectedBodyType = newAll
        if (selectedClass == oldAll) selectedClass = newAll
    }

    fun toggleLanguage() {
        setLanguage(if (currentLang == Lang.KZ) Lang.RU else Lang.KZ)
    }

    fun selectCar(id: Int) {
        selectedCarId = id
        tariff = Tariff.HOUR
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
