package cr.una.pawsitive.repository

import cr.una.pawsitive.service.BreedService

class BreedRepository constructor(
    private val breedService: BreedService
) {
    suspend fun getAllBreeds() = breedService.getAllBreeds()
}