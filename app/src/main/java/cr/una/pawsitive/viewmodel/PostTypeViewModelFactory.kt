package cr.una.pawsitive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.repository.PostTypeRepository
import cr.una.pawsitive.service.PostTypeService

@Suppress("UNCHECKED_CAST")
class PostTypeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PostTypeViewModel::class.java)) {
            PostTypeViewModel(
                postTypeRepository = PostTypeRepository(
                    postTypeService = PostTypeService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}