package cr.una.pawsitive.model

data class AnimalType(
    var animalTypeId: Long? = null,
    var name: String? = null,
    var breed: Breed? = null // Representa la raza asociada al tipo de animal
)
data class AnimalTypeAiRequest(
    var name: String? = null,
    var breed: BreedAiRequest? = null // Representa la raza asociada al tipo de animal
)

data class AnimalTypeNewPost (
    var id: Int,
    var name: String
) {
    override fun toString(): String {
        return name
    }
}