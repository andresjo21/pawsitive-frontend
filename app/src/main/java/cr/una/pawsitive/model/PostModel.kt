package cr.una.pawsitive.model

import androidx.lifecycle.MutableLiveData
import java.util.Date

data class PostModel(
    val id: Int,
    val show: Boolean,
    val postType: PostType,
    val image: Image,
    val user: UserPost,
    val pet: PetPost,
    // para manejar el card de IA
    var isCardVisible: Boolean = false,
    // Respuestas de la IA
    var aiResponse: AiResponse? = null,
    // flag para manejar el like
    var isLiked: Boolean
)
//output for the favourite petitions
data class PostModelOutputFavourite(
    val id: Int,
    val show: Boolean,
    val postType: PostType,
    val image: Image? = null,
    val user: UserPost,
    val pet: PetPost
)

data class PostType(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}

data class Image(
    val id: Int,
    val name: String
)

data class UserPost(
    val id: Int,
    val email: String
)

data class PetPost(
    val id: Int,
    val name: String,
    val address: String,
    val about: String,
    val age: Date,
    val breed: BreedPost,
)

data class AnimalTypePost(
    val id: Int,
    val name: String,
    val breed: BreedPost
)

data class BreedPost(
    val id: Int,
    val name: String,
    val animalType: AnimalTypePost
)