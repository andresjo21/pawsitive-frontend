package cr.una.pawsitive.service

import cr.una.pawsitive.model.PostModel
import cr.una.pawsitive.model.PostModelOutputFavourite
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FavouriteService {
    @GET("v1/posts/favourites/me")
    suspend fun getFavouritePosts() : Response<List<PostModel>>
    // add favourite post
    @PUT("v1/posts/favourites/me/{favourite_id}")
    suspend fun addFavouritePost(@Path("favourite_id") favouriteId: Int) : Response<PostModel>

    // delete favourite post using path parameter favourite_id and no response
    @DELETE("v1/posts/favourites/{favourite_id}")
    suspend fun deleteFavouritePost(@Path("favourite_id") favouriteId:Int)



    companion object{
        fun getInstance() : FavouriteService {
            return ServiceManager.getService(FavouriteService::class.java)
        }
    }
}