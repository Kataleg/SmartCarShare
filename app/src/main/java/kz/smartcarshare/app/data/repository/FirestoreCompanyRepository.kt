package kz.smartcarshare.app.data.repository

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kz.smartcarshare.app.data.CompanyData
import kz.smartcarshare.app.data.sampleCars

class FirestoreCompanyRepository(private val context: Context) {

    private val isFirebaseAvailable: Boolean
        get() = try {
            FirebaseApp.initializeApp(context)
            FirebaseAuth.getInstance()
            true
        } catch (_: Exception) {
            false
        }

    suspend fun syncCompanyAndCarsToFirestore(): Result<Unit> {
        if (!isFirebaseAvailable) return Result.success(Unit)

        return try {
            val firestore = FirebaseFirestore.getInstance()

            CompanyData.allCompanies.forEach { company ->
                val companyCars = sampleCars.filter { car ->
                    company.fleetCarIds.contains(car.id) || car.company.contains(company.name, ignoreCase = true)
                }

                val carsMapList = companyCars.map { car ->
                    mapOf(
                        "id" to car.id,
                        "name" to car.name,
                        "company" to car.company,
                        "location" to car.location,
                        "imageUrl" to car.imageUrl,
                        "displayImageUrl" to car.displayImageUrl,
                        "tags" to car.tags,
                        "seats" to car.seats,
                        "transmission" to car.transmission,
                        "fuel" to car.fuel,
                        "pricePerHour" to car.pricePerHour,
                        "pricePerDay" to (car.pricePerDay ?: 0),
                        "isLux" to car.isLux,
                        "ownerEmail" to car.ownerEmail,
                        "ownerName" to car.ownerName,
                        "ownerKaspiNumber" to car.ownerKaspiNumber
                    )
                }

                val companyDocumentMap = mapOf(
                    "id" to company.id,
                    "name" to company.name,
                    "legalName" to company.legalName,
                    "description" to company.description,
                    "logoUrl" to company.logoUrl,
                    "websiteUrl" to company.websiteUrl,
                    "rating" to company.rating,
                    "is247" to company.is247,
                    "map2GisUrl" to company.map2GisUrl,
                    "additionalMapUrls" to company.additionalMapUrls,
                    "phones" to company.phones.map { mapOf("number" to it.number, "label" to it.label, "isPrimary" to it.isPrimary) },
                    "locations" to company.locations.map { mapOf("title" to it.title, "address" to it.address, "city" to it.city, "mapUrl" to it.mapUrl, "isAirport" to it.isAirport) },
                    "tariffsInfo" to company.tariffsInfo.map { mapOf("name" to it.name, "description" to it.description, "minPriceTenge" to it.minPriceTenge) },
                    "transportCount" to companyCars.size,
                    "cars" to carsMapList
                )

                // Save Document into "companies" collection
                firestore.collection("companies")
                    .document(company.id)
                    .set(companyDocumentMap)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
