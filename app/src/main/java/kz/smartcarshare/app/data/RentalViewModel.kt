package kz.smartcarshare.app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class Tariff { HOUR, DAY }

class RentalViewModel : ViewModel() {

    val cars = sampleCars

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

    private var timerJob: Job? = null

    val selectedCar: Car?
        get() = cars.firstOrNull { it.id == selectedCarId }

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

    fun startRental() {
        elapsedSeconds = 0
        rentalActive = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    // Демо-режим: цена растёт быстрее реальной, чтобы было видно на защите
    fun liveAccruedPrice(): Int {
        val base = currentPrice()
        val divisor = if (tariff == Tariff.HOUR) 3600.0 else 86400.0
        return ((base / divisor) * 60.0 * elapsedSeconds).toInt()
    }

    fun finishRental() {
        timerJob?.cancel()
        rentalActive = false
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