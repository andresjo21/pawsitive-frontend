package cr.una.pawsitive.model

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

data class Pet(
    var petId: Long? = null,
    var name: String? = null,
    var address: String? = null,
    var about: String? = null,
    var age: Long? = null,
    var animalType: AnimalType? = null // Representa el tipo de animal asociado a la mascota
)

data class PetAiRequest(
    var name: String,
    var address: String,
    var about: String,
    var age: String,
    var animalType: AnimalTypeAiRequest // Representa el tipo de animal asociado a la mascota
) {
    constructor(name: String, address: String, about: String, age: Date, animalType: AnimalTypeAiRequest) : this(
        name,
        address,
        about,
        convertDateToIso8601(age),
        animalType
    )

    companion object {
        fun convertDateToIso8601(date: Date): String {
            val instant = date.toInstant()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .withZone(ZoneOffset.UTC)
            return formatter.format(instant)
        }
    }
}

data class PetNewPost (
    var name: String,
    var address: String,
    var about: String,
    var age: Date,
    var breed: BreedNewPost
)