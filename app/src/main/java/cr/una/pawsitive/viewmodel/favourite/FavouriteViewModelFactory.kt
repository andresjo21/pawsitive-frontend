package cr.una.pawsitive.viewmodel.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.repository.FavouriteRepository
import cr.una.pawsitive.service.FavouriteService

@Suppress("UNCHECKED_CAST")
class FavouriteViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            FavouriteViewModel(
                favouriteRepository = FavouriteRepository(
                    favouriteService = FavouriteService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}