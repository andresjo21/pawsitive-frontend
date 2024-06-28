package cr.una.pawsitive.model

data class NewPost(
    var show: Boolean? = null,
    var postType: PostType? = null,
    var pet: PetNewPost? = null
)
