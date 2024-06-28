package cr.una.pawsitive.service

import cr.una.pawsitive.model.AiResponse
import cr.una.pawsitive.model.NewPost
import cr.una.pawsitive.model.PetAiRequest
import cr.una.pawsitive.model.PostModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PostService {
    @GET("v1/posts")
    suspend fun getAllPosts() : Response<List<PostModel>>

    @POST("v1/pets/ai")
    suspend fun getAiInformation(@Body petAiRequest: PetAiRequest) : Response<AiResponse>

    @POST("v1/posts")
    suspend fun createPost(@Body newPost: NewPost) : Response<PostModel>

    companion object{
        fun getInstance() : PostService {
            return ServiceManager.getService(PostService::class.java)
        }
    }
}