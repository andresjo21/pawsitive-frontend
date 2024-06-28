package cr.una.pawsitive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.repository.BreedRepository
import cr.una.pawsitive.service.BreedService

@Suppress("UNCHECKED_CAST")
class BreedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BreedViewModel::class.java)) {
            BreedViewModel(
                breedRepository = BreedRepository(
                    breedService = BreedService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}