package cr.una.pawsitive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.repository.PostRepository
import cr.una.pawsitive.service.PostService

/**
 * if we need to pass some input data to the constructor of the viewModel,
 * we need to create a factory class for viewModel.
 */

@Suppress("UNCHECKED_CAST")
class PostViewModelFactory : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            PostViewModel(
                postRepository = PostRepository(
                    postService = PostService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}