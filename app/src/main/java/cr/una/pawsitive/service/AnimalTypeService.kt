package cr.una.pawsitive.service

import cr.una.pawsitive.model.AnimalTypeNewPost
import retrofit2.Response
import retrofit2.http.GET

interface AnimalTypeService {
    @GET("v1/animalTypes")
    suspend fun getAllAnimalTypes(): Response<List<AnimalTypeNewPost>>

    companion object {
        fun getInstance(): AnimalTypeService {
            return ServiceManager.getService(AnimalTypeService::class.java)
        }
    }
}