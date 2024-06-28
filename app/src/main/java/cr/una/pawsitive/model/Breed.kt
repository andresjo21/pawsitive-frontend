package cr.una.pawsitive.model

data class Breed(
    var breedId: Long? = null,
    var name: String? = null
)
data class BreedAiRequest(
    var name: String? = null
)

data class BreedNewPost (
    var id: Int,
    var name: String,
    var animalType: AnimalTypeNewPost
) {
    override fun toString(): String {
        return name
    }
}