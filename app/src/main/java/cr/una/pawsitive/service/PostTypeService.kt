package cr.una.pawsitive.service

import cr.una.pawsitive.model.PostType
import retrofit2.Response
import retrofit2.http.GET

interface PostTypeService {
    @GET("v1/postTypes")
    suspend fun getAllPostTypes(): Response<List<PostType>>

    companion object {
        fun getInstance(): PostTypeService {
            return ServiceManager.getService(PostTypeService::class.java)
        }
    }
}