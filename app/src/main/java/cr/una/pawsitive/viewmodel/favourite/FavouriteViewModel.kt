package cr.una.pawsitive.viewmodel.favourite

import androidx.lifecycle.ViewModel
import cr.una.pawsitive.repository.FavouriteRepository
import kotlinx.coroutines.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cr.una.pawsitive.model.PostModel

sealed class StateFavourite {
    object Loading : StateFavourite()
    data class Success(val favourite: PostModel?) : StateFavourite()
    data class SuccessList(val favouriteList: List<PostModel>?) : StateFavourite()
    data class Error(val message: String) : StateFavourite()
}

class FavouriteViewModel constructor(
    private val favouriteRepository: FavouriteRepository
): ViewModel(){
    private val _state = MutableLiveData<StateFavourite>()
    val state: LiveData<StateFavourite> get() = _state

    private var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun findAllFavouritePosts() {
        _state.value = StateFavourite.Loading
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            try {
                val response = favouriteRepository.getFavouritePosts()
                withContext(Dispatchers.Main) {
                    _state.postValue(
                        if (response.isSuccessful) StateFavourite.SuccessList(response.body())
                        else StateFavourite.Error("Error : ${response.message()} ")
                    )
                }
            } catch (e: Exception) {
                onError("Exception: ${e.message}")
            }
        }
    }


    private fun onError(message: String) {
        _state.value = StateFavourite.Error(message)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}