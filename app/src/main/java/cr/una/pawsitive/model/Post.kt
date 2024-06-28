package cr.una.pawsitive.model

data class Post(
    var postId: Long? = null,
    var show: Boolean? = null,
    var postType: Int? = null,
    var imageId: String? = null,
    var user: User? = null, // Representa el usuario asociado al post
    var pet: Pet? = null // Representa la mascota asociada al post
)