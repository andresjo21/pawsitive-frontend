package cr.una.pawsitive.repository

import cr.una.pawsitive.service.AnimalTypeService

class AnimalTypeRepository constructor(
    private val animalTypeService: AnimalTypeService
) {
    suspend fun getAllAnimalTypes() = animalTypeService.getAllAnimalTypes()
}