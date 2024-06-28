package cr.una.pawsitive.service

import cr.una.pawsitive.model.BreedNewPost
import retrofit2.Response
import retrofit2.http.GET

interface BreedService {
    @GET("v1/breeds")
    suspend fun getAllBreeds(): Response<List<BreedNewPost>>

    companion object {
        fun getInstance(): BreedService {
            return ServiceManager.getService(BreedService::class.java)
        }
    }
}