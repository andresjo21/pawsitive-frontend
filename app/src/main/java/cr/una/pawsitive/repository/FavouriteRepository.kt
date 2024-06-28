package cr.una.pawsitive.repository

import cr.una.pawsitive.model.PostModel
import cr.una.pawsitive.model.PostModelOutputFavourite
import cr.una.pawsitive.service.FavouriteService
import retrofit2.Response

class FavouriteRepository constructor(
    private val favouriteService: FavouriteService
){
    suspend fun getFavouritePosts() = favouriteService.getFavouritePosts()

    suspend fun addFavouritePost(id: Int): Response<PostModel> {
        return favouriteService.addFavouritePost(id)
    }

    suspend fun deleteFavouritePost(favouriteId:Int) = favouriteService.deleteFavouritePost(favouriteId)
}