package cr.una.pawsitive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.repository.AnimalTypeRepository
import cr.una.pawsitive.service.AnimalTypeService

@Suppress("UNCHECKED_CAST")
class AnimalTypeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AnimalTypeViewModel::class.java)) {
            AnimalTypeViewModel(
                animalTypeRepository = AnimalTypeRepository(
                    animalTypeService = AnimalTypeService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}