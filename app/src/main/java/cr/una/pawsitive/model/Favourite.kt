package cr.una.pawsitive.model

data class Favourite(
    var user: User? = null, // Representa el usuario que marcó como favorito
    var posts: List<Post>? = null // Lista de posts marcados como favoritos
)