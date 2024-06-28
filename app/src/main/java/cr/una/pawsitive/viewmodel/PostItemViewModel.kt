package cr.una.pawsitive.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.una.pawsitive.model.AiResponse
import cr.una.pawsitive.model.PetAiRequest
import cr.una.pawsitive.model.PostModelOutputFavourite
import cr.una.pawsitive.repository.FavouriteRepository
import cr.una.pawsitive.repository.PostRepository
import kotlinx.coroutines.*

class PostItemViewModel constructor(
    private val postRepository: PostRepository,
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {
    private val _aiResponse = MutableLiveData<AiResponse>()
    val aiResponse: LiveData<AiResponse> get() = _aiResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _error.postValue(throwable.message)
    }

    fun getAiInformation(petAiRequest: PetAiRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = postRepository.getAiInformation(petAiRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _aiResponse.value = response.body()
                    } else {
                        _error.postValue("Error: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }


    fun addFavouritePost(id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = favouriteRepository.addFavouritePost(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // handle success
                    } else {
                        _error.postValue("Error: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun deleteFavouritePost(favouriteId: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                favouriteRepository.deleteFavouritePost(favouriteId)
                withContext(Dispatchers.Main) {
                    // handle success
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}